package main.java.cli;

import main.java.AppConfiguration;
import main.java.url_shortener.ShortenedUrl;
import main.java.url_shortener.ShortenedUrlCleaner;
import main.java.url_shortener.ShortenedUrlImpl;
import main.java.url_shortener.ShortenedUrlRegistry;
import main.java.users.Authentication;
import main.java.users.BaseUser;
import main.java.users.User;
import main.java.users.UserRegistry;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CLI {
    private final UserRegistry userRegistry = AppConfiguration.getInstance().getUserRegistry();
    private final Authentication authentication = AppConfiguration.getInstance().getAuthentication();
    private final ShortenedUrlRegistry shortenedUrlRegistry = AppConfiguration.getInstance().getShortenedUrlRegistry();
    private final ShortenedUrlCleaner shortenedUrlCleaner = AppConfiguration.getInstance().getShortenedUrlCleaner();
    private User sessionUser;
    private final Scanner scanner = new Scanner(System.in);

    public static void runApp() {
        CLI cli = new CLI();
        cli.runInactiveUrlsCleaner();
        cli.greet();
        cli.authenticateUser();
        while (true) {
            cli.handleCommand();
        }
    }

    private void greet() {
        System.out.print("""
        \nДобро пожаловать в приложение для сокращения ссылок!
        Чтобы войти в аккаунт, введите ваш UUID.
        Для создания нового пользователя введите любой символ
        """);
    }

    private void authenticateUser() {
        System.out.print("\n>>> ");
        String uuid = scanner.nextLine().trim();

        if (!authentication.logIn(uuid)) {
            System.out.println("\nНе удалось пройти аутентификацию");
            sessionUser = createNewUser();
            authentication.logIn(sessionUser.getUUID());
            return;
        }
        System.out.println(TerminalColors.GREEN + "\nУра! Вы успешно вошли" + TerminalColors.RESET);
        sessionUser = userRegistry.getUser(uuid).get();
    }

    private User createNewUser() {
        User user = new BaseUser();
        System.out.println("\nНовый профиль был создан, ваш UUID:\n" + TerminalColors.GREEN + user.getUUID() + TerminalColors.RESET);
        return user;
    }

    private void handleCommand() {
        System.out.println("""
        \nВведите одну из доступных команд:
        1 - переход по сокращенной ссылке
        2 - создание ссылки
        3 - редактирование одной из ранее созданных ссылок
        4 - вывод всех ваших ссылок
        5 - вывести свой UUID
        6 - выход из аккаунта
        q - выход из приложения""");

        switch (getCommand()) {
            case '1':
                onUrlNavigateCommand();
                break;
            case '2':
                onUrlCreateCommand();
                break;
            case '3':
                onUrlEditCommand();
                break;
            case '4':
                onUrlListCommand();
                break;
            case '5':
                onPrintUUIDCommand();
                break;
            case '6':
                onLogoutCommand();
                break;
            case 'q':
                onExitCommand();
        }
    }

    private char getCommand() {
        System.out.print("\n>>> ");
        return scanner.nextLine().trim().charAt(0);
    }

    private void onUrlNavigateCommand() {
        final String notFoundMessage = TerminalColors.RED + "\nТакой ссылки не существует" + TerminalColors.RESET;
        final String urlNotActiveMessage = TerminalColors.RED +  "\nНе удалось перейти, ссылка больше недоступна" + TerminalColors.RESET;

        Optional<ShortenedUrl> shortenedUrl = getShortenedUrlByName();
        try {
            shortenedUrl.ifPresentOrElse(ShortenedUrl::navigate, () -> System.out.println(notFoundMessage));
        }
        catch (ShortenedUrl.UrlNotActiveException e) {
            System.out.println(urlNotActiveMessage);
        }
    }

    private void onUrlCreateCommand() {
        System.out.print("\nВведите ссылку, которую хотите сократить:\n>>> ");
        String fullUrl = scanner.nextLine().trim();
        try {
            ShortenedUrlImpl.validateUrl(fullUrl);
        }
        catch (ShortenedUrlImpl.UrlNotValidException e) {
            System.out.println(TerminalColors.RED + "Похоже, что вы ввели невалидную ссылку :с" + TerminalColors.RESET);
            return;
        }
        System.out.print("Введите лимит переходов:\n>>> ");
        Integer maxNavigations = scanner.nextInt();
        System.out.print("Введите время жизни ссылки в секундах:\n>>> ");
        Integer timeToLiveInSeconds = scanner.nextInt();
        scanner.nextLine();

        ShortenedUrl shortenedUrl = new ShortenedUrlImpl(fullUrl, sessionUser, maxNavigations, timeToLiveInSeconds);
        System.out.println(TerminalColors.GREEN + "\nСсылка успешно создана: " + TerminalColors.RESET + shortenedUrl.getUrl());
        shortenedUrl.printCharacteristics();
    }

    private void onUrlEditCommand() {
        final String notFoundMessage = TerminalColors.RED + "\nТакой ссылки не существует, убедитесь, что вы создавали ее ранее" + TerminalColors.RESET;

        Optional<ShortenedUrl> shortenedUrl = getShortenedUrlByName();
        shortenedUrl.ifPresentOrElse(url -> {
            ShortenedUrlImpl urlToEdit = (ShortenedUrlImpl) url;
            System.out.print("Введите максимальное число переходов:\n>>> ");
            urlToEdit.updateMaxNavigations(scanner.nextInt());
            System.out.print("Введите новое время жизни ссылки в секундах:\n>>> ");
            urlToEdit.updateExpirationDate(scanner.nextInt());
            System.out.println(TerminalColors.GREEN + "Ссылка успешно обновлена!" + TerminalColors.RESET);
            urlToEdit.printCharacteristics();
            scanner.nextLine();
        }, () -> System.out.println(notFoundMessage));
    }

    private void onUrlListCommand() {
        List<ShortenedUrl> urls = shortenedUrlRegistry.getShortenedUrlsForUser(sessionUser);
        if (urls.isEmpty()) {
            System.out.println("\nПока пусто...");
            return;
        }
        System.out.println("\nВаши ссылки:");
        for (ShortenedUrl shortenedUrl : urls) {
            final String url = shortenedUrl.getUrl();
            System.out.println();
            if (shortenedUrl.isActive()) {
                System.out.println(TerminalColors.BLUE + shortenedUrl + TerminalColors.RESET);
            } else {
                System.out.println(TerminalColors.RED + shortenedUrl + TerminalColors.RESET);
            }
            shortenedUrl.printCharacteristics();
        }
    }

    private void onPrintUUIDCommand() {
        System.out.println("\nВаш UUID:\n" + TerminalColors.BLUE + sessionUser.getUUID() + TerminalColors.RESET);
    }

    private void onLogoutCommand() {
        sessionUser = null;
        greet();
        authenticateUser();
    }

    private void onExitCommand() {
        System.exit(0);
    }

    private Optional<ShortenedUrl> getShortenedUrlByName() {
        System.out.print("\nВведите сокращенную ссылку:\n>>> ");
        String shortenedUrlName = scanner.nextLine().trim();
        return shortenedUrlRegistry.getShortenedUrlByName(shortenedUrlName);
    }

    private void runInactiveUrlsCleaner() {
        final int checkPeriodInSeconds = 5;
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(checkPeriodInSeconds * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                shortenedUrlCleaner.cleanInactiveUrls();
            }
        }).start();
    }
}
