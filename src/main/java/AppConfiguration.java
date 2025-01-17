package main.java;

import org.json.JSONException;
import org.json.JSONObject;

import main.java.url_shortener.*;
import main.java.users.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AppConfiguration {
    static private AppConfiguration configInstance;  // Config acts as a Singleton

    static private HashGenerator hashGeneratorInstance;
    static private UserRegistry userRegistryInstance;
    static private ShortenedUrlRegistry shortenedUrlRegistryInstance;
    static private UserNotifier userNotifierInstance;
    static private ShortenedUrlCleaner shortenedUrlCleanerInstance;
    static private Authentication authenticationInstance;

    private final Integer urlMinNavigations;
    private final Integer urlMaxTimeToLiveInSeconds;
    private final String appDomain;

    public static class ConfigNotInitializedException extends RuntimeException {}
    public static class InvalidConfigStructureException extends RuntimeException {}

    private AppConfiguration(Integer urlMinNavigations, Integer urlMaxTimeToLiveInSeconds, String appDomain) {
        this.urlMinNavigations = urlMinNavigations;
        this.urlMaxTimeToLiveInSeconds = urlMaxTimeToLiveInSeconds;
        this.appDomain = appDomain;
    }

    public static void initializeFromJsonFile(Path configFile) throws IOException {
        JSONObject configJson;
        String configContent = Files.readString(configFile);
        configJson = new JSONObject(configContent);

        try {
            configInstance = new AppConfiguration(
                configJson.getInt("urlMinNavigations"),
                configJson.getInt("urlMaxTimeToLiveInSeconds"),
                configJson.getString("appDomain")
            );
        } catch (JSONException e) {
            throw new InvalidConfigStructureException();
        }
    }

    public static AppConfiguration getInstance() {
        if (configInstance == null) {
            throw new ConfigNotInitializedException();
        }
        return configInstance;
    }

    public Integer getUrlMinNavigations() {
        return urlMinNavigations;
    }

    public Integer getUrlMaxTimeToLiveInSeconds() {
        return urlMaxTimeToLiveInSeconds;
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
