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

    @BeforeEach
    public void setup() {
        _terminalConfig = new TerminalConfig();
    }

    @AfterEach
    public void cleanup() {
        File configFile = new File(_terminalConfig.getConfigPath());
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
        assertEquals("JetBrains Mono", _terminalConfig.getStyles().getFont());
        assertEquals(12, _terminalConfig.getStyles().getFontSize());
        assertEquals("#000000", _terminalConfig.getStyles().getBackgroundColor());
        assertEquals("#FFFFFF", _terminalConfig.getStyles().getForegroundColor());
    }

    @Test
    public void createsConfigFile() {
        _terminalConfig.saveConfigToFile();
        assertTrue(new File(_terminalConfig.getConfigPath()).exists(), "Config File should be created.");
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
            Files.write(Paths.get(_terminalConfig.getConfigPath()), testLuaConfig.getBytes());
        } catch (IOException err) {
            System.err.println("Could not write to test config file: " + err.getMessage());
        }
        TerminalConfig newConfig = new TerminalConfig();

        assertEquals(1024, newConfig.getWidth());
        assertEquals(768, newConfig.getHeight());
        assertEquals("Monospace", newConfig.getStyles().getFont());
        assertEquals(14, newConfig.getStyles().getFontSize());
        assertEquals("#1E1E1E", newConfig.getStyles().getBackgroundColor());
        assertEquals("#C0C0C0", newConfig.getStyles().getForegroundColor());
    }

    @Test
    public void settingValueChangesConfig() {
        _terminalConfig.setWidth(1280);
        _terminalConfig.setHeight(720);
        _terminalConfig.getStyles().setFont("Monospace");
        _terminalConfig.getStyles().setFontSize(16);
        _terminalConfig.getStyles().setBackgroundColor("#222222");
        _terminalConfig.getStyles().setForegroundColor("#DDDDDD");

        assertEquals(1280, _terminalConfig.getWidth());
        assertEquals(720, _terminalConfig.getHeight());
        assertEquals("Monospace", _terminalConfig.getStyles().getFont());
        assertEquals(16, _terminalConfig.getStyles().getFontSize());
        assertEquals("#222222", _terminalConfig.getStyles().getBackgroundColor());
        assertEquals("#DDDDDD", _terminalConfig.getStyles().getForegroundColor());
    }

    @Test
    public void saveConfigShouldWriteToFile() throws IOException {
        _terminalConfig.setWidth(1920);
        _terminalConfig.setHeight(1080);
        _terminalConfig.getStyles().setFont("Monospace");
        _terminalConfig.getStyles().setFontSize(14);
        _terminalConfig.getStyles().setBackgroundColor("#1E1E1E");
        _terminalConfig.getStyles().setForegroundColor("#C0C0C0");

        _terminalConfig.saveConfigToFile();
        assertTrue(new File(_terminalConfig.getConfigPath()).exists(), "Config file should be created.");

        String fileContent = Files.readString(Paths.get(_terminalConfig.getConfigPath()));
        assertTrue(fileContent.contains("config.terminal_width = \"1920\""));
        assertTrue(fileContent.contains("config.terminal_height = \"1080\""));
        assertTrue(fileContent.contains("config.font = \"Monospace\""));
        assertTrue(fileContent.contains("config.font_size = \"14\""));
        assertTrue(fileContent.contains("config.background_color = \"#1E1E1E\""));
        assertTrue(fileContent.contains("config.foreground_color = \"#C0C0C0\""));
    }
}
