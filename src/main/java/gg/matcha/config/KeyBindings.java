package gg.matcha.config;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyBindings {
    public KeyBindings() {
        _keyMap = new HashMap<>();
        loadDefaultKeybinds();
    }

    private void loadDefaultKeybinds() {
        _keyMap.put(KeyEvent.VK_C | KeyEvent.CTRL_DOWN_MASK, TerminalAction.COPY);
        _keyMap.put(KeyEvent.VK_V | KeyEvent.CTRL_DOWN_MASK, TerminalAction.PASTE);
        _keyMap.put(KeyEvent.VK_ENTER, TerminalAction.EXECUTE);
        _keyMap.put(KeyEvent.VK_BACK_SPACE, TerminalAction.DELETE_CHAR);
        _keyMap.put(KeyEvent.VK_UP, TerminalAction.PREVIOUS_COMMAND);
        _keyMap.put(KeyEvent.VK_DOWN, TerminalAction.NEXT_COMMAND);
        _keyMap.put(KeyEvent.VK_L | KeyEvent.CTRL_DOWN_MASK, TerminalAction.CLEAR_SCREEN);
    }

    public TerminalAction getActionForKey(int keyCode) {
        return _keyMap.getOrDefault(keyCode, TerminalAction.NONE);
    }

    public void setKeyBinding(int keyCode, TerminalAction action) {
        _keyMap.put(keyCode, action);
    }

    public void removeKeyBinding(int keyCode) {
        _keyMap.remove(keyCode);
    }

    public Map<Integer, TerminalAction> getAllKeybinds() {
        return new HashMap<>(_keyMap);
    }

    public static KeyBindings getInstance() {
        if (_instance == null) {
            synchronized (KeyBindings.class) {
                if (_instance == null) {
                    _instance = new KeyBindings();
                }
            }
        }
        return _instance;
    }

    private final Map<Integer, TerminalAction> _keyMap;
    public static KeyBindings _instance;
}
