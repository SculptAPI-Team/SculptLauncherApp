package com.mojang.minecraftpe;

import android.os.Looper;
import android.util.Log;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/NotificationListenerService.class */
public class NotificationListenerService {
    private static String sDeviceRegistrationToken = "";

    public NotificationListenerService() {
        retrieveDeviceToken();
    }

    public static String getDeviceRegistrationToken() {
        if (sDeviceRegistrationToken.isEmpty()) {
            retrieveDeviceToken();
        }
        return sDeviceRegistrationToken;
    }

    private static void retrieveDeviceToken() {
        if (Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
            Log.e("Minecraft", "NotificationListenerService.retrieveDeviceToken() should not run on main thread.");
        }
    }

    native void nativePushNotificationReceived(int i, String str, String str2, String str3);

}
