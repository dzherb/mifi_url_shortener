package main.java.users;

import main.java.AppConfiguration;

import java.util.Objects;
import java.util.UUID;

public class BaseUser implements User {
    private final String uuid;

    public BaseUser() {
        this.uuid = UUID.randomUUID().toString();
        onUserCreated();
    }

    private void onUserCreated() {
        AppConfiguration config = AppConfiguration.getInstance();
        config.getUserRegistry().addUser(this);
    }

    @Override
    public String getUUID() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseUser baseUser)) return false;
        return Objects.equals(uuid, baseUser.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
