package com.myunit.log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class FileLogger extends LoggerAdapter {
    private PrintStream stream;
    private String logFile;
    private String extraLogs;
    private boolean autoOpenLog;
    private boolean writeEventLog;

    public FileLogger(String file) {
        try {
            stream = new PrintStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open log file");
        }
        logFile = file;
        extraLogs = "";
        autoOpenLog = true;
        writeEventLog = false;
    }

    public FileLogger openLogAfterTests(boolean autoOpenLog) {
        this.autoOpenLog = autoOpenLog;
        return this;
    }

    public FileLogger writeEventLog(boolean writeEventLog) {
        this.writeEventLog = writeEventLog;
        return this;
    }

    public void log(String message) {
        stream.print(message);
    }

    public void logln(String message) {
        stream.println(message);
    }

    public void logEvent(String message) {
        extraLogs += message;
    }

    public void loglnEvent(String message) {
        extraLogs += message + "\n";
    }

    public void end() {
        if (writeEventLog) {
            stream.print(extraLogs);
        }
        stream.close();
        if (autoOpenLog) {
            try {
                Runtime.getRuntime().exec("cmd.exe /c " + logFile);
            } catch (IOException exception) {
                System.out.println("Error: Cannot open log file.");
            }
        }
    }
}
