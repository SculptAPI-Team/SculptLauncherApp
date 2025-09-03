package com.microsoft.xal.logging;

import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class XalLogger implements AutoCloseable {
    private static final SimpleDateFormat LogDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    private static final String TAG = "XALJAVA";
    private final String m_subArea;
    private final ArrayList<LogEntry> m_logs = new ArrayList<>();
    private LogLevel m_leastVerboseLevel = LogLevel.Verbose;

    private static native void nativeLogBatch(int i, LogEntry[] logEntryArr);

    public enum LogLevel {
        Error(1, 'E'),
        Warning(2, 'W'),
        Important(3, 'P'),
        Information(4, 'I'),
        Verbose(5, 'V');

        private final char m_levelChar;
        private final int m_val;

        LogLevel(int i, char c) {
            this.m_val = i;
            this.m_levelChar = c;
        }

        public int ToInt() {
            return this.m_val;
        }

        public char ToChar() {
            return this.m_levelChar;
        }
    }

    public XalLogger(String str) {
        this.m_subArea = str;
        Verbose("XalLogger created.");
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        Flush();
    }

    public synchronized void Flush() {
        if (this.m_logs.isEmpty()) {
            return;
        }
        try {
            int iToInt = this.m_leastVerboseLevel.ToInt();
            ArrayList<LogEntry> arrayList = this.m_logs;
            nativeLogBatch(iToInt, (LogEntry[]) arrayList.toArray(new LogEntry[arrayList.size()]));
            this.m_logs.clear();
            this.m_leastVerboseLevel = LogLevel.Verbose;
        } catch (Exception e) {
            Log.e(TAG, "Failed to flush logs: " + e.toString());
        } catch (UnsatisfiedLinkError e2) {
            Log.e(TAG, "Failed to flush logs: " + e2.toString());
        }
    }

    public synchronized void Log(LogLevel logLevel, String str) {
        this.m_logs.add(new LogEntry(logLevel, String.format("[%c][%s][%s] %s", Character.valueOf(logLevel.ToChar()), Timestamp(), this.m_subArea, str)));
        if (this.m_leastVerboseLevel.ToInt() > logLevel.ToInt()) {
            this.m_leastVerboseLevel = logLevel;
        }
    }

    public void Error(String str) {
        Log.e(TAG, String.format("[%s] %s", this.m_subArea, str));
        Log(LogLevel.Error, str);
    }

    public void Warning(String str) {
        Log.w(TAG, String.format("[%s] %s", this.m_subArea, str));
        Log(LogLevel.Warning, str);
    }

    public void Important(String str) {
        Log.w(TAG, String.format("[%c][%s] %s", Character.valueOf(LogLevel.Important.ToChar()), this.m_subArea, str));
        Log(LogLevel.Important, str);
    }

    public void Information(String str) {
        Log.i(TAG, String.format("[%s] %s", this.m_subArea, str));
        Log(LogLevel.Information, str);
    }

    public void Verbose(String str) {
        Log.v(TAG, String.format("[%s] %s", this.m_subArea, str));
        Log(LogLevel.Verbose, str);
    }

    private String Timestamp() {
        return LogDateFormat.format(GregorianCalendar.getInstance().getTime());
    }
}
