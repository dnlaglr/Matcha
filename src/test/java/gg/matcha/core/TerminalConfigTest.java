package gg.matcha.core;

import gg.matcha.config.Theme;
import gg.matcha.util.SystemUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class TerminalConfigTest {
    private TerminalConfig _termConfig;
    private final String _configPath = SystemUtil.getConfigPath();

    @BeforeEach
    void setup() {
        _termConfig = new TerminalConfig();
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(new File(_configPath).toPath());
    }

    @Test
    void constructorShouldCreateDefaultConfigTheme() {
        Theme theme = _termConfig.getTheme();
        assertEquals("JetBrains Mono", theme.getFont());
        assertEquals(12, theme.getFontSize());
        assertEquals("#000000", theme.getBackgroundColor());
        assertEquals("#FFFFFF", theme.getForegroundColor());
    }

    @Test
    void saveConfigShouldCreateFile() {
        _termConfig.saveConfigToFile();
        File configFile = new File(_configPath);
        assertTrue(configFile.exists(), "Config file should be created after saving.");
    }

    @Test
    void loadConfigShouldUpdateValues() throws IOException {
        File configFile = new File(_configPath);
        configFile.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("config.terminal_width = \"80\"\n");
            writer.write("config.terminal_height = \"24\"\n");
            writer.write("config.font = \"Courier New\"\n");
            writer.write("config.font_size = \"14\"\n");
            writer.write("config.background_color = \"#1E1E1E\"\n");
            writer.write("config.foreground_color = \"#C0C0C0\"\n");
        }

        _termConfig.loadConfigFromFile();
        Theme theme = _termConfig.getTheme();

        assertEquals(80, theme.getWidth());
        assertEquals(24, theme.getHeight());
        assertEquals("Courier New", theme.getFont());
        assertEquals(14, theme.getFontSize());
        assertEquals("#1E1E1E", theme.getBackgroundColor());
        assertEquals("#C0C0C0", theme.getForegroundColor());
    }

    @Test
    void invalidConfigShouldLoadDefaultValues() throws IOException {
        File configFile = new File(_configPath);
        configFile.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("Invalid Config\n");
        }

        _termConfig.loadConfigFromFile();
        Theme theme = _termConfig.getTheme();

        assertEquals("JetBrains Mono", theme.getFont());
        assertEquals(12, theme.getFontSize());
        assertEquals("#000000", theme.getBackgroundColor());
        assertEquals("#FFFFFF", theme.getForegroundColor());
    }

    @Test
    void configShouldBeSingleton() {
        TerminalConfig instance1 = TerminalConfig.getInstance();
        TerminalConfig instance2 = TerminalConfig.getInstance();

        assertSame(instance1, instance2, "getInstance() should return the same instance.");
    }
}
