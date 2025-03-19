package gg.matcha.terminal;

import java.io.*;

public class TerminalConfig {
    public static String _configPath = null;

    public TerminalConfig() {
        setDefaultConfig();

        if (configFileExists()) {
            loadConfig();
        } else {
            saveConfig();
        }
    }

    private void setDefaultConfig() {
        this._width = 800;
        this._height = 600;
        this._styles = new TerminalStyling("JetBrains Mono", 12, "#000000", "#FFFFFF");
    }

    public boolean configFileExists() {
        if (_configPath == null) {
            String osType = System.getProperty("os.name").toLowerCase();

            if (osType.contains("win")) {
                _configPath = System.getenv("APPDATA") + "\\Matcha\\matcha.lua";
            } else {
                _configPath = System.getProperty("user.home") + "/.matcha.lua";
            }
        }

        File configFile = new File(_configPath);
        return configFile.exists();
    }

    private void loadConfig() {
        File configFile = new File(_configPath);
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
        } catch (IOException err) {
            System.err.println("Error loading config from file: " + err.getMessage());
        }
    }

    private void applyConfigValue(String key, String value) {
        switch (key) {
            case "terminal_width":
                _width = Integer.parseInt(value);
                break;
            case "terminal_height":
                _height = Integer.parseInt(value);
                break;
            case "font":
                _styles._font = value;
                break;
            case "font_size":
                _styles._fontSize = Integer.parseInt(value);
                break;
            case "background_color":
                _styles._backgroundColor = value;
                break;
            case "foreground_color":
                _styles._foregroundColor = value;
                break;
            default:
                System.out.println("Unknown config key: " + key);
        }
    }

    public void saveConfig() {
        File configFile = new File(_configPath);
        configFile.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-- Matcha Terminal Configuration\n\n");
            writer.write("config = {}\n\n");
            writer.write(String.format("config.terminal_width = \"%d\"\n", _width));
            writer.write(String.format("config.terminal_height = \"%d\"\n", _height));
            writer.write(String.format("config.font = \"%s\"\n", _styles._font));
            writer.write(String.format("config.font_size = \"%d\"\n", _styles._fontSize));
            writer.write(String.format("config.background_color = \"%s\"\n", _styles._backgroundColor));
            writer.write(String.format("config.foreground_color = \"%s\"\n", _styles._foregroundColor));
        } catch (IOException err) {
            System.err.println("Error saving config to file: " + err.getMessage());
        }
    }

    public int getWidth() { return _width; }
    public int getHeight() { return _height; }
    public String getFont() { return _styles._font; }
    public int getFontSize() { return _styles._fontSize; }
    public String getBackgroundColor() { return _styles._backgroundColor; }
    public String getForegroundColor() { return _styles._foregroundColor; }
    public String getConfigPath() { return _configPath; }

    public void setWidth(int newWidth) { this._width = newWidth; }
    public void setHeight(int newHeight) { this._height = newHeight; }
    public void setFont(String newFont) { this._styles._font = newFont; }
    public void setFontSize(int newFontSize) { this._styles._fontSize = newFontSize; }
    public void setBackgroundColor(String newBgColor) { this._styles._backgroundColor = newBgColor; }
    public void setForegroundColor(String newFgColor) { this._styles._foregroundColor = newFgColor; }

    private static class TerminalStyling {
        public TerminalStyling(String font, int fontSize, String backgroundColor, String foregroundColor) {
            this._font = font;
            this._fontSize = fontSize;
            this._backgroundColor = backgroundColor;
            this._foregroundColor = foregroundColor;
        }

        private String _font;
        private int _fontSize;
        private String _backgroundColor;
        private String _foregroundColor;
    }

    private int _width;
    private int _height;
    private TerminalStyling _styles;
}
