package main.java.users;

import java.util.Optional;

public interface UserRegistry {
    void addUser(User user);
    Optional<User> getUser(String uuid);
}
