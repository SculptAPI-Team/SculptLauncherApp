package com.mojang.minecraftpe.Webview;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.mojang.minecraftpe.MainActivity;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/Webview/MinecraftChromeClient.class */
class MinecraftChromeClient extends WebChromeClient {
    private MinecraftWebview mView;

    public MinecraftChromeClient(MinecraftWebview minecraftWebview) {
        this.mView = minecraftWebview;
    }

    @Override // android.webkit.WebChromeClient
    public void onProgressChanged(WebView webView, int i) {
        super.onProgressChanged(webView, i);
        // from class: com.mojang.minecraftpe.Webview.MinecraftChromeClient.1
// java.lang.Runnable
        MainActivity.mInstance.runOnUiThread(() -> mView._injectApi());
    }
}
