package main.java.cli;

import main.java.AppConfiguration;
import main.java.users.Authentication;
import main.java.users.BaseUser;
import main.java.users.User;
import main.java.users.UserRegistry;

import java.util.Scanner;

public class CLI {
    private final UserRegistry userRegistry = AppConfiguration.getInstance().getUserRegistry();
    private final Authentication authentication = AppConfiguration.getInstance().getAuthentication();
    private User sessionUser;
    private final Scanner scanner = new Scanner(System.in);

    public static void runApp() {
        CLI cli = new CLI();
        cli.greet();
        cli.authenticateUser(cli.scanner.nextLine().trim());
        cli.handleCommand();
    }

    private void greet() {
        System.out.println("""
        Добро пожаловать в приложение для сокращения ссылок!
        Чтобы войти в аккаунт, введите ваш UUID. Для создания нового пользователя введите любой символ
        """);
    }

    private void authenticateUser(String uuid) {
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
        // todo
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

        char command = scanner.next().trim().charAt(0);

        switch (command) {
            case '1':
                break;
            case '2':
                break;
            case '3':
                break;
            case '4':
                break;
            case '5':
                break;
            case 'q':
                System.exit(0);
        }
    }

    private void onUrlNavigationCommand() {

    }

    private void onUrlCreationCommand() {

    }
}
