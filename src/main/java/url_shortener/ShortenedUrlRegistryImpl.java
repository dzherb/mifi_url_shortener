package main.java.url_shortener;

import main.java.AppConfiguration;
import main.java.users.User;

import java.util.*;

public class ShortenedUrlRegistryImpl implements ShortenedUrlRegistry {
    private final Map<String, ShortenedUrl> shortenedUrls;

    public ShortenedUrlRegistryImpl() {
        shortenedUrls = new HashMap<>();
    }

    @Override
    public void addUrl(ShortenedUrl url) {
        shortenedUrls.put(url.getHash(), url);
    }

    @Override
    public void removeUrl(ShortenedUrl url) {
        shortenedUrls.remove(url.getHash());
    }

    @Override
    public Optional<ShortenedUrl> getShortenedUrlByName(String url) {
        String hash = parseHash(url);
        return Optional.ofNullable(shortenedUrls.get(hash));
    }

    private String parseHash(String url) {
        if (url == null || url.isEmpty() || !url.startsWith(AppConfiguration.getInstance().getAppDomain())) {
            return null;
        }
        return url.substring(url.lastIndexOf('/') + 1);
    }

    @Override
    public List<ShortenedUrl> getAllShortenedUrls() {
        return new ArrayList<>(shortenedUrls.values());
    }

    @Override
    public List<ShortenedUrl> getShortenedUrlsForUser(User user) {
        return shortenedUrls.values().stream().filter(url -> url.getAuthor().equals(user)).toList();
    }
}
