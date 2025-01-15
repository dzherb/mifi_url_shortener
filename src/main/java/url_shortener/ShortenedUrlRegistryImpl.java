package main.java.url_shortener;

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
        // todo чекать соответствие домена
        return url.substring(url.indexOf('/') + 1);  // todo а если передали с https:// ??? Можно чистить еще до этого
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
