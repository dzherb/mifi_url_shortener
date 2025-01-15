package main.java.users;

import main.java.AppConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthenticationImpl implements Authentication {
    Map<String, User> loggedUsers = new HashMap<>();

    @Override
    public boolean isLoggedIn(User user) {
        return loggedUsers.containsKey(user.getUUID());
    }

    @Override
    public boolean logIn(String uuid) {
        AppConfiguration config = AppConfiguration.getInstance();
        UserRegistry userRegistry = config.getUserRegistry();
        Optional<User> user = userRegistry.getUser(uuid);
        user.ifPresent(u -> loggedUsers.put(u.getUUID(), u));
        return user.isPresent() && isLoggedIn(user.get());
    }

    @Override
    public void logOut(User user) {
        loggedUsers.remove(user.getUUID());
    }
}
