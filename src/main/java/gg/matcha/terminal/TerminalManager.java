package gg.matcha.terminal;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TerminalManager {
    public TerminalManager() {
        this._terminals = new ConcurrentHashMap<>();
        this._terminalIDCounter = new AtomicInteger(1);
        this._terminalExecutor = Executors.newCachedThreadPool();
        this._activeTerminalID = -1;
    }

    /**
     * Creates a new terminal instance in a separate thread.
     *
     * @return The ID of the new terminal instance.
     * */
    public int createTerminal() {
        int terminalID = _terminalIDCounter.getAndIncrement();
        TerminalInstance terminal = new TerminalInstance(terminalID);

        _terminals.put(terminalID, terminal);
        _terminalExecutor.submit(terminal::start);

        _activeTerminalID = terminalID;
        return terminalID;
    }

    /**
     * Closes and removes the specified terminal instance.
     *
     * @param terminalID The ID of the terminal to close and remove.
     * */
    public void closeTerminal(int terminalID) {
        TerminalInstance terminal = _terminals.remove(terminalID);
        if (terminal != null) {
            terminal.stop();
        }

        if (terminalID == _activeTerminalID) {
            _activeTerminalID = _terminals.isEmpty() ? -1 : _terminals.keySet().iterator().next();
        }
    }

    /**
     * Switches the current active terminal.
     *
     * @param terminalID The ID of the terminal to switch to.
     */
    public void switchTerminal(int terminalID) {
        if (_terminals.containsKey(terminalID)) {
            _activeTerminalID = terminalID;
        } else {
            throw new IllegalArgumentException("Invalid terminal ID: " + terminalID);
        }
    }

    /**
     * Retrieves the current terminal instance.
     *
     * @return The currently active terminal, or null if none exists.
     * */
    public TerminalInstance getActiveTerminal() {
        return _terminals.get(_activeTerminalID);
    }

    /**
     * Retrieves the list of all active terminal instances.
     *
     * @return The collection of active terminal instances.
     * */
    public Collection<TerminalInstance> getAllTerminals() {
        return _terminals.values();
    }

    /**
     * Shuts down all active terminal instances.
     * */
    public void shutdown() {
        for (TerminalInstance terminal : _terminals.values()) {
            terminal.stop();
        }

        _terminals.clear();
        _activeTerminalID = -1;
        _terminalExecutor.shutdown();
    }

    private final Map<Integer, TerminalInstance> _terminals;
    private final AtomicInteger _terminalIDCounter;
    private final ExecutorService _terminalExecutor;
    private int _activeTerminalID;
}
