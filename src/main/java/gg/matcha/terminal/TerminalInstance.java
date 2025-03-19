package gg.matcha.terminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TerminalInstance implements Runnable {
    public TerminalInstance(int terminalID) {
        this._terminalID = terminalID;
        this._processBuilder = new ProcessBuilder();
        this._executorService = Executors.newSingleThreadExecutor();
        this._isRunning = false;
    }

    /**
     * Starts terminal process and begins IO processing.
     * */
    public void start() {
        _isRunning = true;
        _terminalThread = new Thread(this);
        _terminalThread.start();
    }

    /**
     * Sets up terminal and begins handling terminal output.
     * */
    @Override
    public void run() {
        try {
            _terminalProcess = _processBuilder.command(getDefaultShell()).start();
            _inputReader = new BufferedReader(new InputStreamReader(_terminalProcess.getInputStream()));
            _outputWriter = new PrintWriter(_terminalProcess.getOutputStream(), true);

            String line;
            while (_isRunning && (line = _inputReader.readLine()) != null) {
                handleOutput(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            stop();
        }
    }

    /**
     * Sends a command to the terminal.
     *
     * @param command The command to execute.
     * */
    public void sendCommand(String command) {
        if (_isRunning && _terminalProcess != null && _outputWriter != null) {
            _outputWriter.println(command);
        }
    }

    /**
     * Handles output from terminal process.
     * */
    private void handleOutput(String output) {
        System.out.println("Terminal " + _terminalID + ": " + output);
    }

    /**
     * Stops terminal process and cleans up resources.
     * */
    public void stop() {
        _isRunning = false;
        if (_terminalProcess != null) {
            _terminalProcess.destroy();
        }

        _executorService.shutdownNow();
    }

    public String getDefaultShell() {
        String osType = System.getProperty("os.name").toLowerCase();
        if (osType.contains("win")) {
            return "cmd.exe";
        } else {
            return "bash";
        }
    }

    public int getTerminalID() { return _terminalID; }
    public BufferedReader getInputReader() { return _inputReader; }
    public PrintWriter getOutputWriter() { return _outputWriter; }
    public boolean isRunning() { return _isRunning; }

    public void setTerminalProcess(Process terminalProcess) { this._terminalProcess = terminalProcess; }


    private Process _terminalProcess;
    private BufferedReader _inputReader;
    private PrintWriter _outputWriter;
    private Thread _terminalThread;
    private volatile boolean _isRunning;

    private final int _terminalID;
    private final ProcessBuilder _processBuilder;
    private final ExecutorService _executorService;
}
