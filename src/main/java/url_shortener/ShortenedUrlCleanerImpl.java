package main.java.url_shortener;

import main.java.AppConfiguration;

import java.util.List;

public class ShortenedUrlCleanerImpl implements ShortenedUrlCleaner {
    @Override
    public void cleanInactiveUrls() {
        AppConfiguration config = AppConfiguration.getInstance();
        ShortenedUrlRegistry urlRegistry = config.getShortenedUrlRegistry();
        List<ShortenedUrl> allUrls = urlRegistry.getAllShortenedUrls();
        for (ShortenedUrl shortenedUrl : allUrls) {
            if (!shortenedUrl.isActive()) {
                String message = "Ссылка " + shortenedUrl.getUrl() + " больше неактивна";
                config.getUserNotifier().notify(shortenedUrl.getAuthor(), message);
                urlRegistry.removeUrl(shortenedUrl);
            }
        }
    }
}
