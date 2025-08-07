package com.mojang.minecraftpe;

import android.content.Intent;
import android.net.Uri;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/Minecraft_Market_Demo.class */
public class Minecraft_Market_Demo extends MainActivity {
    @Override // com.mojang.minecraftpe.MainActivity
    public void buyGame() {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.mojang.minecraftpe")));
    }

    @Override // com.mojang.minecraftpe.MainActivity
    protected boolean isDemo() {
        return true;
    }
}
