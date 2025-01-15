package main.java.cli;

import main.java.AppConfiguration;
import main.java.url_shortener.ShortenedUrl;
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
    private User sessionUser;
    private final Scanner scanner = new Scanner(System.in);

    public static void runApp() {
        CLI cli = new CLI();
        cli.greet();
        cli.authenticateUser();
        while (cli.scanner.hasNext()) {
            cli.handleCommand();
        }
    }

    private void greet() {
        System.out.println("""
        Добро пожаловать в приложение для сокращения ссылок!
        Чтобы войти в аккаунт, введите ваш UUID. Для создания нового пользователя введите любой символ
        """);
    }

    private void authenticateUser() {
        String uuid = scanner.nextLine().trim();

        if (!authentication.logIn(uuid)) {
            System.out.println("Не удалось пройти аутентификацию\n");
            sessionUser = createNewUser();
            authentication.logIn(sessionUser.getUUID());
            return;
        }
        System.out.println("Ура! Вы успешно вошли");
        sessionUser = userRegistry.getUser(uuid).get();
    }

    private User createNewUser() {
        User user = new BaseUser();
        System.out.println("Новый профиль был создан, ваш UUID:\n" + user.getUUID() + "\n");
        return user;
    }

    private void handleCommand() {
        System.out.println("""
        \n
        Введите одну из доступных команд:
        1 - переход по сокращенной ссылке
        2 - создание ссылки
        3 - редактирование одной из ранее созданных ссылок
        4 - вывод всех ваших ссылок
        5 - выход из аккаунта
        q - выход из приложения
        \n
        """);

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
                onLogoutCommand();
                break;
            case 'q':
                onExitCommand();
        }
    }

    private char getCommand() {
        return scanner.nextLine().trim().charAt(0);
    }

    private void onUrlNavigateCommand() {
        final String notFoundMessage = "Такой ссылки не существует";
        final String urlNotActiveMessage = "Не удалось перейти, ссылка больше недоступна";

        Optional<ShortenedUrl> shortenedUrl = getShortenedUrlByName();
        try {
            shortenedUrl.ifPresentOrElse(ShortenedUrl::navigate, () -> System.out.println(notFoundMessage));
        }
        catch (ShortenedUrl.UrlNotActiveException e) {
            System.out.println(urlNotActiveMessage);
        }
    }

    private void onUrlCreateCommand() {
        System.out.println("Введите ссылку, которую хотите сократить:\n>>> ");
        String fullUrl = scanner.nextLine().trim();
        try {
            ShortenedUrlImpl.validateUrl(fullUrl);
        }
        catch (ShortenedUrlImpl.UrlNotValidException e) {
            System.out.println("Похоже, что вы ввели невалидную ссылку :с");
            return;
        }
        System.out.println("Введите лимит переходов:\n>>> ");
        Integer maxNavigations = scanner.nextInt();
        System.out.println("Введите время жизни ссылки в секундах:\n>>> ");
        Integer timeToLiveInSeconds = scanner.nextInt();

        ShortenedUrl shortenedUrl = new ShortenedUrlImpl(fullUrl, sessionUser, maxNavigations, timeToLiveInSeconds);
        System.out.println("\nСсылка успешно создана: " + shortenedUrl.getUrl() + "\n");
    }

    private void onUrlEditCommand() {
        final String notFoundMessage = "Такой ссылки не существует, убедитесь, что вы создавали ее ранее";

        Optional<ShortenedUrl> shortenedUrl = getShortenedUrlByName();
        shortenedUrl.ifPresentOrElse(url -> {
            // todo edit
        }, () -> System.out.println(notFoundMessage));
    }

    private void onUrlListCommand() {
        List<ShortenedUrl> urls = shortenedUrlRegistry.getShortenedUrlsForUser(sessionUser);
        System.out.println("\nВаши ссылки:\n");
        for (ShortenedUrl shortenedUrl : urls) {
            System.out.println(shortenedUrl);
        }
        System.out.println("\n");
    }

    private void onLogoutCommand() {
        sessionUser = null;
        authenticateUser();
    }

    private void onExitCommand() {
        System.exit(0);
    }

    private Optional<ShortenedUrl> getShortenedUrlByName() {
        String shortenedUrlName = scanner.nextLine().trim();
        return shortenedUrlRegistry.getShortenedUrlByName(shortenedUrlName);
    }
}
