package com.mojang.minecraftpe;

import android.content.Intent;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/ActivityListener.class */
public interface ActivityListener {
    void onActivityResult(int i, int i2, Intent intent);

    void onDestroy();

    void onResume();

    void onStop();
}
