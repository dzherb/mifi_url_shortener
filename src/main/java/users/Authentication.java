package main.java.users;

public interface Authentication {
    boolean isLoggedIn(User user);
    boolean logIn(String uuid);
    void logOut(User user);
}
