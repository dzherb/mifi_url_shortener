package main.java.users;

import java.util.HashMap;
import java.util.Optional;

public class UserRegistryImpl implements UserRegistry {
    private final HashMap<String, User> users;

    public UserRegistryImpl() {
        this.users = new HashMap<>();
    }

    @Override
    public void addUser(User user) {
        users.put(user.getUUID(), user);
    }

    @Override
    public Optional<User> getUser(String uuid) {
        return Optional.ofNullable(users.get(uuid));
    }
}
