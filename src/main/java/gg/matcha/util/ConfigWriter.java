package gg.matcha.util;

import gg.matcha.terminal.TerminalConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class ConfigWriter {
    public void saveConfig(TerminalConfig config) {
        StringBuilder luaConfig = new StringBuilder();

        luaConfig.append("-- Matcha Terminal Configuration\n\n");
        luaConfig.append("config = {}\n\n");

        luaConfig.append(String.format("config.terminal_width = %d\n", config.getWidth()));
        luaConfig.append(String.format("config.terminal_height = %d\n", config.getHeight()));
        luaConfig.append(String.format("config.font = \"%s\"\n", config.getFont()));
        luaConfig.append(String.format("config.font_size = %d\n", config.getFontSize()));
        luaConfig.append(String.format("config.background_color = \"%s\"\n", config.getBackgroundColor()));
        luaConfig.append(String.format("config.foreground_color = \"%s\"\n", config.getForegroundColor()));

        writeToFile(luaConfig.toString());
    }

    private String getConfigPath() {
        // TODO: Move OS detection to different class for reusability.
        String osType = System.getProperty("os.name").toLowerCase();

        if (osType.contains("win")) {
            return System.getenv("APPDATA") + "\\Matcha\\matcha.lua";
        } else {
            return System.getProperty("user.home") + "/.matcha.lua";
        }
    }

    private void writeToFile(String configContent) {
        File configFile = new File(getConfigPath());
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(configContent);

            // TODO: Add logging for successful config writes.
        } catch (IOException err) {
            System.err.println("Error saving config to file: " + err.getMessage());
        }
    }
}
