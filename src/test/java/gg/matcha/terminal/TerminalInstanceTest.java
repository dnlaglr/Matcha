package gg.matcha.terminal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TerminalInstanceTest {
    private TerminalInstance _termInstance;

    @BeforeEach
    public void setup() {
        _termInstance = new TerminalInstance(1);
    }

    @Test
    public void startedTerminalShouldBeRunning() {
        _termInstance.start();
        assertTrue(_termInstance.isRunning(), "Terminal should be running after start.");
    }

    @Test
    public void stoppedTerminalShouldNotBeRunning() {
        _termInstance.start();
        assertTrue(_termInstance.isRunning(), "Terminal should be running initially.");

        _termInstance.stop();
        assertFalse(_termInstance.isRunning(), "Terminal should not be running after stop.");
    }

    @Test
    public void defaultShellShouldNotBeNull() {
        String shell = _termInstance.getDefaultShell();

        assertNotNull(shell, "Default shell should not be null.");
        assertTrue(shell.equals("cmd.exe") || shell.equals("bash"), "Shell should be either cmd.exe or bash");
    }
}
