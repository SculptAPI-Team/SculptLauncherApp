package com.mojang.minecraftpe.Webview;

import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.mojang.minecraftpe.MainActivity;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/Webview/MinecraftWebViewClient.class */
class MinecraftWebViewClient extends WebViewClient {
    private MinecraftWebview mView;

    public MinecraftWebViewClient(MinecraftWebview minecraftWebview) {
        this.mView = minecraftWebview;
    }

    @Override // android.webkit.WebViewClient
    public void onPageFinished(WebView webView, String str) {
        System.out.println("Finished loading " + str);
        super.onPageFinished(webView, str);
    }

    @Override // android.webkit.WebViewClient
    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        System.out.println("Started loading " + str);
        super.onPageStarted(webView, str, bitmap);
    }

    @Override // android.webkit.WebViewClient
    public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
        System.out.println(String.format("Error %s loading url %s", webResourceError.getDescription().toString(), webResourceRequest.getUrl().toString()));
        if (webResourceError.getErrorCode() == -2 && webResourceRequest.getUrl().toString().contains("minecraftresource")) {
            return;
        }
        this.mView.onWebError(webResourceError.getErrorCode(), webResourceError.getDescription().toString());
        super.onReceivedError(webView, webResourceRequest, webResourceError);
    }

    @Override // android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
        Uri url = webResourceRequest.getUrl();
        Uri uri = Uri.parse(webView.getUrl());
        if (!webResourceRequest.hasGesture() || uri.getHost().equals(url.getHost())) {
            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
        }
        MainActivity.mInstance.launchUri(webResourceRequest.getUrl().toString());
        return true;
    }
}
