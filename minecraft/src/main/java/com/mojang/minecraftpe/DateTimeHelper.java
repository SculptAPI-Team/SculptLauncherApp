package com.mojang.minecraftpe;

import android.text.format.DateFormat;
import android.util.Log;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/DateTimeHelper.class */
public class DateTimeHelper {
    public static boolean Is24HourTimeFormat() {
        try {
            return DateFormat.is24HourFormat(MainActivity.mInstance.getApplicationContext());
        } catch (Exception e) {
            Log.d("DateTimeHelper", e.getMessage());
            return true;
        }
    }
}
