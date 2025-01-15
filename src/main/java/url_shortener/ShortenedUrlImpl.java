package main.java.url_shortener;

import main.java.AppConfiguration;
import main.java.users.User;

import java.util.Date;
import java.util.Objects;

public class ShortenedUrlImpl implements ShortenedUrl {
    private final String fullUrl;
    private final User author;
    private final String hash;
    private Date expiresAt;
    private Integer totalNavigations;
    private Integer maxNavigations;

    static public class UrlNotValidException extends RuntimeException {}

    public ShortenedUrlImpl(String fullUrl, User author, Integer maxNavigations, Integer timeToLiveInSeconds) {
        AppConfiguration config = AppConfiguration.getInstance();

        this.fullUrl = fullUrl;
        this.author = author;
        this.hash = config.getHashGenerator().generate(fullUrl);
        this.maxNavigations = Math.max(maxNavigations, config.getUrlDefaultMaxNavigations());
        this.expiresAt = new Date(System.currentTimeMillis() + Math.min(timeToLiveInSeconds, config.getUrlDefaultTimeToLiveInSeconds()) * 1000L);
        config.getShortenedUrlRegistry().addUrl(this);
    }

    public static void validateUrl(String url) throws UrlNotValidException {
        // todo
    }

    @Override
    public String getUrl() {
        return AppConfiguration.getInstance().getAppDomain() + "/" + hash;
    }

    @Override
    public String getHash() {
        return hash;
    }

    @Override
    public User getAuthor() {
        return this.author;
    }

    @Override
    public void navigate() throws UrlNotActiveException {
        if (!isActive()) throw new UrlNotActiveException();
        System.out.println("ПЕРЕХОД ПО ССЫЛКЕ " + fullUrl);
        // todo navigate
        onNavigationHappened();
    }

    private void onNavigationHappened() {
        totalNavigations++;
    }

    @Override
    public boolean isActive() {
        return expiresAt.after(new Date()) && totalNavigations < maxNavigations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShortenedUrlImpl that)) return false;
        return Objects.equals(fullUrl, that.fullUrl) && Objects.equals(author, that.author) && Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullUrl, author, hash);
    }

    @Override
    public String toString() {
        return getUrl();
    }
}
