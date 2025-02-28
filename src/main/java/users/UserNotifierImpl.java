package main.java.users;

import main.java.cli.TerminalColors;

public class UserNotifierImpl implements UserNotifier {
    @Override
    public void notify(User user, String message) {
        System.out.println("\n\nУведомление для пользователя с UUID " + TerminalColors.BLUE + user.getUUID() + TerminalColors.RESET + ":\n" + message);
        System.out.print("\n>>> ");
    }
}
