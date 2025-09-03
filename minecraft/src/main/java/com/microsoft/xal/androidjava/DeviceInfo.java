package com.microsoft.xal.androidjava;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

public class DeviceInfo {
    public static String GetOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String GetDeviceId(Context context) {
        @SuppressLint("HardwareIds")
        String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
        int[] iArr = {8, 4, 4, 4, 12};
        int length = string.length();
        int i = 0;
        if (length < 32) {
            string = String.format("%0" + (32 - length) + "d", 0) + string;
        }
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        while (i < 5) {
            if (i != 0) {
                sb.append("-");
            }
            int i3 = iArr[i] + i2;
            sb.append(string.substring(i2, i3));
            i++;
            i2 = i3;
        }
        return sb.toString();
    }
}
