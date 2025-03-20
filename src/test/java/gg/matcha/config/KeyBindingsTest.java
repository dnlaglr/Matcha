package gg.matcha.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class KeyBindingsTest {
    private KeyBindings _keyBinds;

    @BeforeEach
    void setup() {
        _keyBinds = new KeyBindings();
    }

    @AfterEach
    void cleanup() {
        KeyBindings._instance = null;
    }

    @Test
    void constructorShouldCreateDefaultKeybinds() {
        assertEquals(TerminalAction.COPY, _keyBinds.getActionForKey(KeyEvent.VK_C | KeyEvent.CTRL_DOWN_MASK));
        assertEquals(TerminalAction.PASTE, _keyBinds.getActionForKey(KeyEvent.VK_V | KeyEvent.CTRL_DOWN_MASK));
        assertEquals(TerminalAction.EXECUTE, _keyBinds.getActionForKey(KeyEvent.VK_ENTER));
        assertEquals(TerminalAction.DELETE_CHAR, _keyBinds.getActionForKey(KeyEvent.VK_BACK_SPACE));
        assertEquals(TerminalAction.PREVIOUS_COMMAND, _keyBinds.getActionForKey(KeyEvent.VK_UP));
        assertEquals(TerminalAction.NEXT_COMMAND, _keyBinds.getActionForKey(KeyEvent.VK_DOWN));
        assertEquals(TerminalAction.CLEAR_SCREEN, _keyBinds.getActionForKey(KeyEvent.VK_L | KeyEvent.CTRL_DOWN_MASK));
    }

    @Test
    void getUnknownKeyShouldReturnNone() {
        assertEquals(TerminalAction.NONE, _keyBinds.getActionForKey(KeyEvent.VK_X));
    }

    @Test
    void setKeybindShouldUpdateKeybinds() {
        _keyBinds.setKeyBinding(KeyEvent.VK_Z, TerminalAction.PASTE);
        assertEquals(TerminalAction.PASTE, _keyBinds.getActionForKey(KeyEvent.VK_Z));
    }

    @Test
    void removeKeybindShouldUpdateKeybinds() {
        _keyBinds.setKeyBinding(KeyEvent.VK_E, TerminalAction.CLEAR_SCREEN);
        assertEquals(TerminalAction.CLEAR_SCREEN, _keyBinds.getActionForKey(KeyEvent.VK_E));

        _keyBinds.removeKeyBinding(KeyEvent.VK_E);
        assertEquals(TerminalAction.NONE, _keyBinds.getActionForKey(KeyEvent.VK_E));
    }

    @Test
    void getAllKeybindsShouldReturnKeymap() {
        Map<Integer, TerminalAction> keybinds = _keyBinds.getAllKeybinds();
        assertNotNull(keybinds);
        assertTrue(keybinds.containsValue(TerminalAction.COPY));
        assertTrue(keybinds.containsValue(TerminalAction.PASTE));
        assertTrue(keybinds.containsValue(TerminalAction.EXECUTE));
    }

    @Test
    void keybindsShouldBeSingleton() {
        KeyBindings instance1 = KeyBindings.getInstance();
        KeyBindings instance2 = KeyBindings.getInstance();

        assertSame(instance1, instance2, "getInstance() should return the same instance.");
    }
}
