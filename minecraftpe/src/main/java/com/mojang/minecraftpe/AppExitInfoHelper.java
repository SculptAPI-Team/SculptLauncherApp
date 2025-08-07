package com.mojang.minecraftpe;

import android.app.ActivityManager;
import android.app.ApplicationExitInfo;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.util.List;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/AppExitInfoHelper.class */
public class AppExitInfoHelper {
    private Context mContext;

    AppExitInfoHelper(Context context) {
        this.mContext = context;
    }

    private static native void nativeSendApplicationExitInfo(String str, int i, int i2, int i3, long j, long j2, String str2, boolean z);

    public void readyForAppExitInfo() {
        if (Build.VERSION.SDK_INT < 30) {
            return;
        }
        List<ApplicationExitInfo> historicalProcessExitReasons = ((ActivityManager) this.mContext.getSystemService(Context.ACTIVITY_SERVICE)).getHistoricalProcessExitReasons(null, 0, 1);
        if (historicalProcessExitReasons.isEmpty()) {
            return;
        }
        ApplicationExitInfo applicationExitInfo = historicalProcessExitReasons.get(0);
        byte[] processStateSummary = applicationExitInfo.getProcessStateSummary();
        String str = processStateSummary != null ? new String(processStateSummary, StandardCharsets.UTF_8) : "";
        Log.i("AppExitInfoHelper", "Received session ID from ApplicationExitInfo: ".concat(str));
        nativeSendApplicationExitInfo(applicationExitInfo.getDescription(), applicationExitInfo.getReason(), applicationExitInfo.getStatus(), applicationExitInfo.getImportance(), applicationExitInfo.getRss(), applicationExitInfo.getPss(), str, ActivityManager.isLowMemoryKillReportSupported());
    }

    public void registerSessionIdForApplicationExitInfo(String str) {
        if (this.mContext == null || str == null || Build.VERSION.SDK_INT < 30) {
            return;
        }
        ((ActivityManager) this.mContext.getSystemService(Context.ACTIVITY_SERVICE)).setProcessStateSummary(str.getBytes(StandardCharsets.UTF_8));
        Log.i("AppExitInfoHelper", "Registering session ID for ApplicationExitInfo: " + str);
    }
}
