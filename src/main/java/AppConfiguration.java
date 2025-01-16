package main.java;

import main.java.url_shortener.*;
import main.java.users.*;

import java.nio.file.Path;

public class AppConfiguration {
    static private AppConfiguration configInstance;  // Config acts as a Singleton

    static private HashGenerator hashGeneratorInstance;
    static private UserRegistry userRegistryInstance;
    static private ShortenedUrlRegistry shortenedUrlRegistryInstance;
    static private UserNotifier userNotifierInstance;
    static private ShortenedUrlCleaner shortenedUrlCleanerInstance;
    static private Authentication authenticationInstance;

    private final Integer urlDefaultMaxNavigations;
    private final Integer urlDefaultTimeToLiveInSeconds;
    private final String appDomain;

    private static class ConfigNotInitializedException extends RuntimeException {}

    private AppConfiguration(Integer urlDefaultMaxNavigations, Integer urlDefaultTimeToLiveInSeconds, String appDomain) {
        this.urlDefaultMaxNavigations = urlDefaultMaxNavigations;
        this.urlDefaultTimeToLiveInSeconds = urlDefaultTimeToLiveInSeconds;
        this.appDomain = appDomain;
    }

    public static void initializeFromJsonFile(Path configFile) {
        // todo
        configInstance = new AppConfiguration(5, 500, "shrt.com");
    }

    public static AppConfiguration getInstance() {
        if (configInstance == null) {
            throw new ConfigNotInitializedException();
        }
        return configInstance;
    }

    public Integer getUrlDefaultMaxNavigations() {
        return urlDefaultMaxNavigations;
    }

    public Integer getUrlDefaultTimeToLiveInSeconds() {
        return urlDefaultTimeToLiveInSeconds;
    }

    public String getAppDomain() {
        return appDomain;
    }

    public HashGenerator getHashGenerator() {
        if (hashGeneratorInstance == null) {
            hashGeneratorInstance = new HashGeneratorImpl();
        }
        return hashGeneratorInstance;
    }

    public UserRegistry getUserRegistry() {
        if (userRegistryInstance == null) {
            userRegistryInstance = new UserRegistryImpl();
        }
        return userRegistryInstance;
    }

    public ShortenedUrlRegistry getShortenedUrlRegistry() {
        if (shortenedUrlRegistryInstance == null) {
            shortenedUrlRegistryInstance = new ShortenedUrlRegistryImpl();
        }
        return shortenedUrlRegistryInstance;
    }

    public UserNotifier getUserNotifier() {
        if (userNotifierInstance == null) {
            userNotifierInstance = new UserNotifierImpl();
        }
        return userNotifierInstance;
    }

    public ShortenedUrlCleaner getShortenedUrlCleaner() {
        if (shortenedUrlCleanerInstance == null) {
            shortenedUrlCleanerInstance = new ShortenedUrlCleanerImpl();
        }
        return shortenedUrlCleanerInstance;
    }

    public Authentication getAuthentication() {
        if (authenticationInstance == null) {
            authenticationInstance = new AuthenticationImpl();
        }
        return authenticationInstance;
    }
}
