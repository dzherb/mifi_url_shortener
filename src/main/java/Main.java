package main.java;

import main.java.cli.CLI;
import main.java.cli.TerminalColors;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static final Path CONFIG_FILE_PATH = Path.of("src/main/resources/config.json");

    public static void main(String[] args) {
        try {
            AppConfiguration.initializeFromJsonFile(CONFIG_FILE_PATH);
        } catch (AppConfiguration.InvalidConfigStructureException | IOException e) {
            System.out.println(TerminalColors.RED + "Убедитесь, что конфигурационный файл существует и корректно настроен" + TerminalColors.RESET);
            System.exit(1);
        }

        CLI.runApp();
    }
}
