package com.mojang.minecraftpe;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

public class CrashManager {
    private String mCrashDumpFolder;
    private String mCrashUploadURI;
    private String mCrashUploadURIWithSentryKey;
    private String mCurrentSessionId;
    private String mExceptionUploadURI;
    private Thread.UncaughtExceptionHandler mPreviousUncaughtExceptionHandler = null;

    private static native String nativeNotifyUncaughtException();

    private String uploadCrashFile(String filePath, String sessionID, String sentryParametersJSON){
        return "";
    }

    public CrashManager(String crashDumpFolder, String currentSessionId, SentryEndpointConfig sentryEndpointConfig) {
        this.mCrashUploadURI = null;
        this.mCrashUploadURIWithSentryKey = null;
        this.mExceptionUploadURI = null;
        this.mCrashDumpFolder = null;
        this.mCurrentSessionId = null;
        this.mCrashDumpFolder = crashDumpFolder;
        this.mCurrentSessionId = currentSessionId;
        this.mCrashUploadURI = sentryEndpointConfig.url + "/api/" + sentryEndpointConfig.projectId + "/minidump/";
        StringBuilder sb = new StringBuilder();
        sb.append(this.mCrashUploadURI);
        sb.append("?sentry_key=");
        sb.append(sentryEndpointConfig.publicKey);
        this.mCrashUploadURIWithSentryKey = sb.toString();
        this.mExceptionUploadURI = sentryEndpointConfig.url + "/api/" + sentryEndpointConfig.projectId + "/store/?sentry_version=7&sentry_key=" + sentryEndpointConfig.publicKey;
    }

    public void installGlobalExceptionHandler() {
        this.mPreviousUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() { // from class: com.mojang.minecraftpe.CrashManager.1
            @Override // java.lang.Thread.UncaughtExceptionHandler
            public void uncaughtException(Thread t, Throwable e) {
                var activity = MainActivity.mInstance;
                var content = new StringBuilder();
                content.append(t.toString()).append("\n");
                content.append(e.toString()).append("\n");
                content.append(e.getMessage()).append("\n");
                var file = activity.getApplicationContext().getExternalFilesDir("crash");
                var time = String.valueOf(System.currentTimeMillis());
                var thisFile = new File(file, time + ".txt");
                try {
                    Files.write(thisFile.toPath(), content.toString().getBytes(StandardCharsets.UTF_8));
                    Log.i("日志返回", "将日志保存到了" + thisFile.getAbsolutePath());
                } catch (IOException ex) {
                    Log.w("日志返回", Objects.requireNonNull(ex.getMessage()));
                }
            }
        });
    }

    public String getCrashUploadURI() {
        return this.mCrashUploadURI;
    }

    public String getExceptionUploadURI() {
        return this.mExceptionUploadURI;
    }
}