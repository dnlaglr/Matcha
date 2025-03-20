package gg.matcha.core;

import gg.matcha.config.Theme;
import gg.matcha.util.SystemUtil;

import java.io.*;

public class TerminalConfig {
    public TerminalConfig() {
        this._theme = new Theme();

        loadDefaultValues();
    }

    private void loadDefaultValues() {
        _theme.setDefaultTheme();
    }

    public void loadConfigFromFile() {
        File configFile = new File(SystemUtil.getConfigPath());
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("config.")) {
                    String[] parts = line.replace("config.", "").split("=");
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String val = parts[1].trim().replace("\"", "");

                        applyConfigValue(key, val);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load config from file: " + e.getMessage());
            loadDefaultValues();
        }
    }

    public void saveConfigToFile() {
        File configFile = new File(SystemUtil.getConfigPath());
        configFile.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-- MatchaTerm Config\n\n");
            writer.write("config = {}\n\n");
            writer.write(String.format("config.terminal_width = \"%d\"\n", Theme.getInstance().getWidth()));
            writer.write(String.format("config.terminal_height = \"%d\"\n", Theme.getInstance().getHeight()));
            writer.write(String.format("config.font = \"%s\"\n", Theme.getInstance().getFont()));
            writer.write(String.format("config.font_size = \"%d\"\n", Theme.getInstance().getFontSize()));
            writer.write(String.format("config.background_color = \"%s\"\n", Theme.getInstance().getBackgroundColor()));
            writer.write(String.format("config.foreground_color = \"%s\"\n", Theme.getInstance().getForegroundColor()));
        } catch (IOException e) {
            System.err.println("Failed to save config to file: " + e.getMessage());
        }
    }

    private void applyConfigValue(String key, String val) {
        switch (key) {
            case "terminal_width" -> _theme.setWidth(Integer.parseInt(val));
            case "terminal_height" -> _theme.setHeight(Integer.parseInt(val));
            case "font" -> _theme.setFont(val);
            case "font_size" -> _theme.setFontSize(Integer.parseInt(val));
            case "background_color" -> _theme.setBackgroundColor(val);
            case "foreground_color" -> _theme.setForegroundColor(val);
            default -> System.err.println("Unknown config key: " + key);
        }
    }

    public Theme getTheme() { return this._theme; }

    public static TerminalConfig getInstance() {
        if (_instance == null) {
            synchronized (TerminalConfig.class) {
                if (_instance == null) {
                    _instance = new TerminalConfig();
                }
            }
        }
        return _instance;
    }

    private final Theme _theme;
    private static TerminalConfig _instance;
}
