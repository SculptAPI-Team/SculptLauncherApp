package com.mojang.minecraftpe.Webview;

import android.webkit.JavascriptInterface;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/Webview/WebviewHostInterface.class */
class WebviewHostInterface {
    private MinecraftWebview mView;

    public WebviewHostInterface(MinecraftWebview minecraftWebview) {
        this.mView = minecraftWebview;
    }

    @JavascriptInterface
    public void sendToHost(String str, String str2) {
        sendToHost(str, str2, "");
    }

    @JavascriptInterface
    public void sendToHost(String str, String str2, String str3) {
        System.out.println("SendToHost " + str + ", " + str2 + ", " + str3);
        this.mView.sendToHost(str, str2, str3);
    }
}
