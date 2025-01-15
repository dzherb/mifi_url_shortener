package main.java.url_shortener;

import main.java.users.User;

public interface ShortenedUrl {
    String getUrl();
    String getHash();
    User getAuthor();
    void navigate();
    boolean isActive();
}
