package com.xbox.httpclient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClientRequest {
    private static final byte[] NO_BODY = new byte[0];
    private static final OkHttpClient OK_CLIENT = new OkHttpClient.Builder().retryOnConnectionFailure(false).build();
    private final Context appContext;
    private Request.Builder requestBuilder = new Request.Builder();

    /* JADX INFO: Access modifiers changed from: private */
    public native void OnRequestCompleted(long j, HttpClientResponse httpClientResponse);

    /* JADX INFO: Access modifiers changed from: private */
    public native void OnRequestFailed(long j, String str, String str2, String str3, boolean z);

    public HttpClientRequest(Context context) {
        this.appContext = context;
    }

    public void setHttpUrl(String str) {
        this.requestBuilder = this.requestBuilder.url(str);
    }

    public void setHttpMethodAndBody(String str, long j, String str2, long j2) {
        RequestBody httpClientRequestBody;
        if (j2 == 0) {
            httpClientRequestBody = null;
            if ("POST".equals(str) || "PUT".equals(str)) {
                httpClientRequestBody = RequestBody.create(NO_BODY, str2 != null ? MediaType.parse(str2) : null);
            }
        } else {
            httpClientRequestBody = new HttpClientRequestBody(j, str2, j2);
        }
        this.requestBuilder.method(str, httpClientRequestBody);
    }

    public void setHttpHeader(String str, String str2) {
        this.requestBuilder = this.requestBuilder.addHeader(str, str2);
    }

    public void doRequestAsync(final long j) {
        OK_CLIENT.newCall(this.requestBuilder.build()).enqueue(new Callback() { // from class: com.xbox.httpclient.HttpClientRequest.1
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                StringWriter stringWriter = new StringWriter();
                iOException.printStackTrace(new PrintWriter(stringWriter));
                HttpClientRequest.this.OnRequestFailed(j, iOException.getClass().getCanonicalName(), stringWriter.toString(), HttpClientRequest.this.GetAllNetworksInfo(), iOException instanceof UnknownHostException);
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) {
                HttpClientRequest.this.OnRequestCompleted(j, new HttpClientResponse(j, response));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String GetAllNetworksInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        StringBuilder sb = new StringBuilder("Has default proxy: ");
        sb.append(connectivityManager.getDefaultProxy() != null).append("\nHas active network: ");
        sb.append(connectivityManager.getActiveNetwork() != null).append('\n');
        Network[] allNetworks = connectivityManager.getAllNetworks();
        for (int i = 0; i < allNetworks.length; i++) {
            if (i > 0) {
                sb.append("\n");
            }
            sb.append(NetworkObserver.NetworkDetails.getNetworkDetails(allNetworks[i], connectivityManager));
        }
        return sb.toString();
    }
}
