package com.microsoft.xal.logging;

/**
 * 02.10.2020
 *
 * @author Тимашков Иван
 * @author https://github.com/TimScriptov
 */

public class LogEntry {
    private final XalLogger.LogLevel m_level;
    private final String m_message;

    public LogEntry(XalLogger.LogLevel level, String message) {
        m_level = level;
        m_message = message;
    }

    public String Message() {
        return m_message;
    }

    public int Level() {
        return m_level.ToInt();
    }
}