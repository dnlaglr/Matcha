package gg.matcha.terminal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TerminalConfig {
    public TerminalConfig() {
        this._osName = System.getProperty("os.name").toLowerCase();
        this._defaultShell = detectDefaultShell();
        this._configPath = detectConfigPath();

        loadDefaultValues();

        if (configFileExists()) {
            loadConfigFromFile();
        } else {
            saveConfigToFile();
        }
    }

    private void loadDefaultValues() {
        this._width = 800;
        this._height = 600;
        this._styles = new TerminalStyling("JetBrains Mono", 12, "#000000", "#FFFFFF");
    }

    private String detectConfigPath() {
        String configPath;
        if (_osName.contains("win")) {
            configPath = System.getenv("APPDATA") + "\\Matcha\\matcha.lua";
        } else {
            configPath = System.getProperty("user.home") + "/.matcha.lua";
        }

        return configPath;
    }

    private String detectDefaultShell() {
        if (_osName.contains("win")) {
            return System.getenv("ComSpec");
        } else {
            return System.getenv("SHELL");
        }
    }

    private boolean configFileExists() {
        return Files.exists(Paths.get(_configPath));
    }

    private void loadConfigFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(_configPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("config.")) {
                    String[] parts = line.replace("config.", "").split("=");
                    if (parts.length == 2) {
                        applyConfigValue(parts[0].trim(), parts[1].trim().replace("\"", ""));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[ ERROR ] Failed to load config from file: " + e.getMessage());
        }
    }

    private void applyConfigValue(String key, String val) {
        switch (key) {
            case "terminal_width" -> _width = Integer.parseInt(val);
            case "terminal_height" -> _height = Integer.parseInt(val);
            case "font" -> _styles.setFont(val);
            case "font_size" -> _styles.setFontSize(Integer.parseInt(val));
            case "background_color" -> _styles.setBackgroundColor(val);
            case "foreground_color" -> _styles.setForegroundColor(val);
            default -> System.err.println("[ WARNING ] Unkown config key: " + key);
        }
    }

    public void saveConfigToFile() {
        try {
            Files.createDirectories(Paths.get(_configPath).getParent());
            try (FileWriter writer = new FileWriter(_configPath)) {
                writer.write("-- Matcha Terminal Configuration\n\n");
                writer.write("config = {}\n\n");
                writer.write(String.format("config.terminal_width = \"%d\"\n", _width));
                writer.write(String.format("config.terminal_height = \"%d\"\n", _height));
                writer.write(String.format("config.font = \"%s\"\n", _styles.getFont()));
                writer.write(String.format("config.font_size = \"%d\"\n", _styles.getFontSize()));
                writer.write(String.format("config.background_color = \"%s\"\n", _styles.getBackgroundColor()));
                writer.write(String.format("config.foreground_color = \"%s\"\n", _styles.getForegroundColor()));
            }
        } catch (IOException e) {
            System.err.println("[ ERROR ] Failed to save config to file: " + e.getMessage());
        }
    }

    public int getWidth() { return _width; }
    public int getHeight() { return _height; }
    public TerminalStyling getStyles() { return _styles; }
    public String getConfigPath() { return _configPath; }
    public String getOSName() { return _osName; }
    public String getDefaultShell() { return _defaultShell; }

    public void setWidth(int newWidth) { _width = newWidth; }
    public void setHeight(int newHeight) { _height = newHeight; }
    public void setStyles(TerminalStyling newStyles) { _styles = newStyles; }

    private final String _configPath;
    private final String _osName;
    private final String _defaultShell;

    private int _width;
    private int _height;
    private TerminalStyling _styles;
}