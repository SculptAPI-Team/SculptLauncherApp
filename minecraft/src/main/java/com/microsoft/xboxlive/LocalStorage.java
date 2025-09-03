package com.microsoft.xboxlive;

import android.content.Context;

public class LocalStorage {
    public static String getPath(Context context) {
        return context.getFilesDir().getPath();
    }
}
