package main.java;

import main.java.cli.CLI;

import java.nio.file.Path;

public class Main {
    public static final Path CONFIG_FILE_PATH = Path.of("src/main/resources/config.json");

    public static void main(String[] args) {
        AppConfiguration.initializeFromJsonFile(CONFIG_FILE_PATH);
        CLI.runApp();
    }
}
