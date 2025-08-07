package com.mojang.minecraftpe.Webview;

import android.R;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.mojang.minecraftpe.MainActivity;
import com.mojang.minecraftpe.PopupView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/Webview/MinecraftWebview.class */
public class MinecraftWebview {
    private MainActivity mActivity;
    private int mId;
    private WebView mWebView;
    private PopupView mWebViewPopup;

    public MinecraftWebview(int i) {
        this.mId = i;
        MainActivity mainActivity = MainActivity.mInstance;
        this.mActivity = mainActivity;
        // from class: com.mojang.minecraftpe.Webview.MinecraftWebview.1
// java.lang.Runnable
        mainActivity.runOnUiThread(this::_createWebView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void _createWebView() {
        if (!MainActivity.mInstance.isPublishBuild()) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        WebView webView = new WebView(this.mActivity);
        this.mWebView = webView;
        webView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.mWebView.setWebViewClient(new MinecraftWebViewClient(this));
        this.mWebView.setWebChromeClient(new MinecraftChromeClient(this));
        this.mWebView.addJavascriptInterface(new WebviewHostInterface(this), "codeBuilderHostInterface");
        WebSettings settings = this.mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        this.mWebViewPopup = new PopupView(this.mActivity);
        View rootView = this.mActivity.findViewById(R.id.content).getRootView();
        this.mWebViewPopup.setContentView(this.mWebView);
        this.mWebViewPopup.setParentView(rootView);
    }

    private Boolean _hideSystemBars(View view) {
        if (view == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 30) {
            view.setSystemUiVisibility(5894);
            return true;
        }
        WindowInsetsController windowInsetsController = view.getWindowInsetsController();
        if (windowInsetsController == null) {
            return false;
        }
        windowInsetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsController.hide(WindowInsets.Type.systemBars());
        return true;
    }

    private void _hideSystemBars() {
        MainActivity mainActivity = this.mActivity;
        if (mainActivity == null) {
            return;
        }
        // from class: com.mojang.minecraftpe.Webview.MinecraftWebview$$ExternalSyntheticLambda0
// java.lang.Runnable
        mainActivity.runOnUiThread(this::m256xa7fbd3d6);
    }

    private String _readResource(int i) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream inputStreamOpenRawResource = this.mActivity.getResources().openRawResource(i);
        try {
            byte[] bArr = new byte[256];
            while (true) {
                int i2 = inputStreamOpenRawResource.read(bArr);
                if (i2 <= 0) {
                    inputStreamOpenRawResource.close();
                    return byteArrayOutputStream.toString();
                }
                byteArrayOutputStream.write(bArr, 0, i2);
            }
        } catch (IOException e) {
            System.out.println("Failed to read resource " + i + " with error " + e.toString());
            return null;
        }
    }

    private native void nativeOnWebError(int i, int i2, String str);

    private native void nativeSendToHost(int i, String str, String str2, String str3);

    public void _injectApi() {
        MainActivity mainActivity = this.mActivity;
        if (mainActivity == null) {
            onWebError(0, "_injectApi called after teardown");
            return;
        }
        String str_readResource = _readResource(mainActivity.getResources().getIdentifier("code_builder_hosted_editor", "raw", this.mActivity.getPackageName()));
        if (str_readResource != null) {
            this.mWebView.evaluateJavascript(str_readResource, null);
        } else {
            onWebError(0, "Unable to inject api");
        }
    }

    /* renamed from: lambda$_hideSystemBars$0$com-mojang-minecraftpe-Webview-MinecraftWebview, reason: not valid java name */
    /* synthetic */ void m255xa87239d5() {
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        _hideSystemBars();
    }

    /* renamed from: lambda$_hideSystemBars$1$com-mojang-minecraftpe-Webview-MinecraftWebview, reason: not valid java name */
    /* synthetic */ void m256xa7fbd3d6() {
        WebView webView = this.mWebView;
        if (webView == null || _hideSystemBars(webView).booleanValue()) {
            return;
        }
        // from class: com.mojang.minecraftpe.Webview.MinecraftWebview$$ExternalSyntheticLambda1
// java.lang.Runnable
        new Thread(this::m255xa87239d5).start();
    }

    public void onWebError(int i, String str) {
        nativeOnWebError(this.mId, i, str);
    }

    public void sendToHost(String str, String str2, String str3) {
        nativeSendToHost(this.mId, str, str2, str3);
    }

    public void sendToWebView(String str) {
        // from class: com.mojang.minecraftpe.Webview.MinecraftWebview.5
// java.lang.Runnable
        this.mActivity.runOnUiThread(() -> mWebView.evaluateJavascript(str, null));
    }

    public void setMuted(boolean z) {
    }

    public void setPropagatedAlpha(float f) {
        setShowView(((double) f) == 1.0d);
    }

    public void setRect(float f, float f2, float f3, float f4) {
        // from class: com.mojang.minecraftpe.Webview.MinecraftWebview.2
// java.lang.Runnable
        this.mActivity.runOnUiThread(() -> {
            mWebViewPopup.setRect((int)f, (int) f2, (int) f3, (int) f4);
            mWebViewPopup.update();
        });
    }

    public void setShowView(boolean z) {
        if (z) {
            _hideSystemBars();
        }
        // from class: com.mojang.minecraftpe.Webview.MinecraftWebview.4
// java.lang.Runnable
        this.mActivity.runOnUiThread(() -> {
            mWebViewPopup.setVisible(z);
            sendToWebView(String.format("window.ipcCodeScreenRenderer.%s();", z ? "onShow" : "onHide"));
        });
    }

    public void setUrl(String str) {
        // from class: com.mojang.minecraftpe.Webview.MinecraftWebview.3
// java.lang.Runnable
        this.mActivity.runOnUiThread(() -> mWebView.loadUrl(str));
    }

    public void teardown() {
        this.mWebViewPopup.dismiss();
        this.mWebViewPopup = null;
        this.mWebView = null;
        this.mActivity = null;
        this.mId = -1;
    }
}
