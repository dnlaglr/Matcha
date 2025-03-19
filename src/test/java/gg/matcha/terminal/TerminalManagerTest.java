package gg.matcha.terminal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TerminalManagerTest {
    private TerminalManager _termManager;

    @BeforeEach
    public void setup() {
        _termManager = new TerminalManager();
    }

    @Test
    public void createdTerminalShouldBeRunningAndActive() {
        int terminalID = _termManager.createTerminal();
        TerminalInstance terminal = _termManager.getActiveTerminal();

        assertNotNull(terminal, "Terminal should not be null.");
        assertEquals(terminalID, terminal.getTerminalID(), "Created terminal ID should match active terminal ID.");
    }

    @Test
    public void switchingTerminalShouldChangeActiveTerminal() {
        int terminalID1 = _termManager.createTerminal();
        _termManager.createTerminal(); // Create a second terminal.

        _termManager.switchTerminal(terminalID1); // Switch back to first terminal.
        TerminalInstance activeTerminal = _termManager.getActiveTerminal();
        assertEquals(terminalID1, activeTerminal.getTerminalID(), "Switched terminal ID and active terminal ID should match.");
    }

    @Test
    public void activeTerminalShouldBeNullAfterClose() {
        int terminalID = _termManager.createTerminal();
        TerminalInstance terminal = _termManager.getActiveTerminal();

        _termManager.closeTerminal(terminalID);
        assertNull(_termManager.getActiveTerminal(), "After closing the terminal, there should be no active terminal.");
    }

    @Test
    public void switchingToClosedTerminalShouldGoToNextActive() {
        int terminalID1 = _termManager.createTerminal();
        int terminalID2 = _termManager.createTerminal();

        _termManager.closeTerminal(terminalID1);

        assertThrows(IllegalArgumentException.class, () -> _termManager.switchTerminal(terminalID1));

        TerminalInstance activeTerminal = _termManager.getActiveTerminal();

        assertNotNull(activeTerminal, "There should still be an active terminal after switching.");
        assertEquals(terminalID2, activeTerminal.getTerminalID(), "The active terminal should be the second one after closing the first.");
    }

    @Test
    public void managerShutdownShouldCloseAllTerminals() {
        int terminalID = _termManager.createTerminal();
        TerminalInstance terminal = _termManager.getActiveTerminal();

        _termManager.shutdown();
        assertTrue(_termManager.getAllTerminals().isEmpty(), "After shutdown, no terminals should remain.");
        assertNull(_termManager.getActiveTerminal(), "After shutdown, active terminal should be null.");
    }
}
