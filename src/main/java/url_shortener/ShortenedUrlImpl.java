package main.java.url_shortener;

import main.java.AppConfiguration;
import main.java.cli.TerminalColors;
import main.java.users.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ShortenedUrlImpl implements ShortenedUrl {
    private final String fullUrl;
    private final User author;
    private final String hash;
    private Date expiresAt;
    private Integer totalNavigations = 0;
    private Integer maxNavigations;
    private Boolean isDeleted = false;

    static public class UrlNotValidException extends RuntimeException {}

    public ShortenedUrlImpl(String fullUrl, User author, Integer maxNavigations, Integer timeToLiveInSeconds) {
        AppConfiguration config = AppConfiguration.getInstance();

        this.fullUrl = fullUrl;
        this.author = author;
        this.hash = config.getHashGenerator().generate(fullUrl);
        this.maxNavigations = getFinalMaxNavigations(maxNavigations);
        this.expiresAt = getExpirationDate(timeToLiveInSeconds);

        config.getShortenedUrlRegistry().addUrl(this);
    }

    private Integer getFinalMaxNavigations(Integer maxNavigations) {
        return Math.max(maxNavigations, AppConfiguration.getInstance().getUrlMinNavigations());
    }

    private Date getExpirationDate(Integer timeToLiveInSeconds) {
        return new Date(
            System.currentTimeMillis() + Math.min(timeToLiveInSeconds,
            AppConfiguration.getInstance().getUrlMaxTimeToLiveInSeconds()) * 1000L
        );
    }

    public void updateMaxNavigations(Integer maxNavigations) {
        this.maxNavigations = getFinalMaxNavigations(maxNavigations);
    }

    public void updateExpirationDate(Integer timeToLiveInSeconds) {
        this.expiresAt = getExpirationDate(timeToLiveInSeconds);
    }

    public static void validateUrl(String url) throws UrlNotValidException {
        if (url == null || !url.trim().startsWith("http")) {
            throw new UrlNotValidException();
        }
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
        return !isDeleted && expiresAt.after(new Date()) && totalNavigations < maxNavigations;
    }

    @Override
    public void delete() {
        isDeleted = true;
    }

    @Override
    public boolean isDeleted() {
        return isDeleted;
    }

    @Override
    public void printCharacteristics() {
        final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");;

        System.out.println(
            TerminalColors.YELLOW +
            "\nСсылка активна до: " + dateFormat.format(expiresAt) +
            "\nМаксимальное количество переходов: " + maxNavigations +
            "\nСовершенных переходов: " + totalNavigations +
            "\nАктивна ли ссылка: " + (isActive() ? TerminalColors.GREEN + "да" : TerminalColors.RED + "нет") + TerminalColors.RESET
        );
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
