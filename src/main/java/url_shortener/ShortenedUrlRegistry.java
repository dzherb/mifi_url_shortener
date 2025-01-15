package main.java.url_shortener;

import main.java.users.User;

import java.util.List;
import java.util.Optional;

public interface ShortenedUrlRegistry {
    void addUrl(ShortenedUrl url);
    void removeUrl(ShortenedUrl url);
    Optional<ShortenedUrl> getShortenedUrlByName(String url);
    List<ShortenedUrl> getAllShortenedUrls();
    List<ShortenedUrl> getShortenedUrlsForUser(User user);
}
