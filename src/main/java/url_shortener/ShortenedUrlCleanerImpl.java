package main.java.url_shortener;

import main.java.AppConfiguration;
import main.java.cli.TerminalColors;

import java.util.List;

public class ShortenedUrlCleanerImpl implements ShortenedUrlCleaner {
    @Override
    public void cleanInactiveUrls() {
        AppConfiguration config = AppConfiguration.getInstance();
        ShortenedUrlRegistry urlRegistry = config.getShortenedUrlRegistry();
        List<ShortenedUrl> allUrls = urlRegistry.getAllShortenedUrls();
        for (ShortenedUrl shortenedUrl : allUrls) {
            if (!shortenedUrl.isActive() && !shortenedUrl.isDeleted()) {
                final String message = TerminalColors.RED + "Ссылка " + shortenedUrl.getUrl() + " больше неактивна" + TerminalColors.RESET;
                config.getUserNotifier().notify(shortenedUrl.getAuthor(), message);
                shortenedUrl.delete();
            }
        }
    }
}
