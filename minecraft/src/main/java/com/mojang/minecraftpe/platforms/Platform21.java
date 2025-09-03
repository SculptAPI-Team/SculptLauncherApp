package com.mojang.minecraftpe.platforms;

import android.os.Build;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/platforms/Platform21.class */
public class Platform21 extends Platform19 {
    public Platform21(boolean z) {
        super(z);
    }

    @Override // com.mojang.minecraftpe.platforms.Platform9, com.mojang.minecraftpe.platforms.Platform
    public String getABIS() {
        return Build.SUPPORTED_ABIS.toString();
    }
}
