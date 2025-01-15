package main.java.users;

public class UserNotifierImpl implements UserNotifier {
    @Override
    public void notify(User user, String message) {
        System.out.println("Уведомление для пользователя с UUID " + user.getUUID() + ":\n" + message);
    }
}
