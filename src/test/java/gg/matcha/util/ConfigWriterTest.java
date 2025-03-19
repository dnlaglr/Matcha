package gg.matcha.util;

import gg.matcha.terminal.TerminalConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConfigWriterTest {
    private TerminalConfig _terminalConfig;
    private String _configPath;

    @BeforeEach
    public void setup() {
        _terminalConfig = new TerminalConfig();
        _configPath = _terminalConfig.getConfigPath();
    }

    @AfterEach
    public void cleanup() {
        File configFile = new File(_configPath);
        if (configFile.exists()) {
            try {
                configFile.delete();
            } catch (SecurityException err) {
                System.err.println("Could not delete test config file: " + err.getMessage());
            }
        }
    }

    @Test
    public void saveConfigShouldWriteToFile() throws IOException {
        _terminalConfig.setWidth(1024);
        _terminalConfig.setHeight(768);
        _terminalConfig.setFont("Fira Code");
        _terminalConfig.setFontSize(14);
        _terminalConfig.setBackgroundColor("#1E1E1E");
        _terminalConfig.setForegroundColor("#C0C0C0");

        _terminalConfig.saveConfig();
        assertTrue(new File(_configPath).exists(), "Config file should be created.");

        String content = Files.readString(Paths.get(_configPath));
        assertTrue(content.contains("config.terminal_width = \"1024\""));
        assertTrue(content.contains("config.terminal_height = \"768\""));
        assertTrue(content.contains("config.font = \"Fira Code\""));
        assertTrue(content.contains("config.font_size = \"14\""));
        assertTrue(content.contains("config.background_color = \"#1E1E1E\""));
        assertTrue(content.contains("config.foreground_color = \"#C0C0C0\""));
    }
}
