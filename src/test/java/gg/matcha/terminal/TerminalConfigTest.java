package gg.matcha.terminal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TerminalConfigTest {
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
    public void createsDefaultConfigValues() {
        assertEquals(800, _terminalConfig.getWidth());
        assertEquals(600, _terminalConfig.getHeight());
        assertEquals("JetBrains Mono", _terminalConfig.getFont());
        assertEquals(12, _terminalConfig.getFontSize());
        assertEquals("#000000", _terminalConfig.getBackgroundColor());
        assertEquals("#FFFFFF", _terminalConfig.getForegroundColor());
    }

    @Test
    public void createsConfigFile() {
        _terminalConfig.saveConfig();
        assertTrue(new File(_configPath).exists(), "Config File should be created.");
    }

    @Test
    public void loadingFileChangesConfig() {
        String testLuaConfig = """
            config = {}
            config.terminal_width = "1024"
            config.terminal_height = "768"
            config.font = "Monospace"
            config.font_size = "14"
            config.background_color = "#1E1E1E"
            config.foreground_color = "#C0C0C0"
            """;

        try {
            Files.write(Paths.get(_configPath), testLuaConfig.getBytes());
        } catch (IOException err) {
            System.err.println("Could not write to test config file: " + err.getMessage());
        }
        TerminalConfig newConfig = new TerminalConfig();

        assertEquals(1024, newConfig.getWidth());
        assertEquals(768, newConfig.getHeight());
        assertEquals("Monospace", newConfig.getFont());
        assertEquals(14, newConfig.getFontSize());
        assertEquals("#1E1E1E", newConfig.getBackgroundColor());
        assertEquals("#C0C0C0", newConfig.getForegroundColor());
    }

    @Test
    public void settingValueChangesConfig() {
        _terminalConfig.setWidth(1280);
        _terminalConfig.setHeight(720);
        _terminalConfig.setFont("Monospace");
        _terminalConfig.setFontSize(16);
        _terminalConfig.setBackgroundColor("#222222");
        _terminalConfig.setForegroundColor("#DDDDDD");

        assertEquals(1280, _terminalConfig.getWidth());
        assertEquals(720, _terminalConfig.getHeight());
        assertEquals("Monospace", _terminalConfig.getFont());
        assertEquals(16, _terminalConfig.getFontSize());
        assertEquals("#222222", _terminalConfig.getBackgroundColor());
        assertEquals("#DDDDDD", _terminalConfig.getForegroundColor());
    }
}
