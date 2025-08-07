package com.mojang.minecraftpe.platforms;

import android.view.View;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/platforms/Platform.class */
public abstract class Platform {
    public static Platform createPlatform(boolean z) {
        return new Platform19(z);
    }

    public abstract String getABIS();

    public abstract void onAppStart(View view);

    public abstract void onViewFocusChanged(boolean z);

    public abstract void onVolumePressed();
}
