package com.microsoft.xbox.idp.interop;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class Interop {
    private static final String DNET_SCOPE = "open-user.auth.dnet.xboxlive.com";
    private static final String PACKAGE_NAME_TO_REMOVE = "com.microsoft.onlineid.sample";
    private static final String POLICY = "mbi_ssl";
    private static final String PROD_SCOPE = "open-user.auth.xboxlive.com";
    private static final String TAG = "Interop";
    private static Context s_context;

    public interface ErrorCallback {
        void onError(int i, int i2, String str);
    }

    public interface EventInitializationCallback extends ErrorCallback {
        void onSuccess();
    }

    public static native boolean deinitializeInterop();

    public static native boolean initializeInterop(Context context);

    private static native void notificiation_registration_callback(String str);

    public static String getSystemProxy() {
        String property;
        String property2 = System.getProperty("http.proxyHost");
        if (property2 == null || (property = System.getProperty("http.proxyPort")) == null) {
            return "";
        }
        String str = "http://" + property2 + ":" + property;
        Log.i(TAG, str);
        return str;
    }

    public static String getLocale() {
        String string = Locale.getDefault().toString();
        Log.i(TAG, "locale is: " + string);
        return string;
    }

    public static String ReadConfigFile(Context context) throws Resources.NotFoundException, IOException {
        s_context = context;
        InputStream inputStreamOpenRawResource = context.getResources().openRawResource(context.getResources().getIdentifier("xboxservices", "raw", context.getPackageName()));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            try {
                int i = inputStreamOpenRawResource.read(bArr);
                if (i == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, i);
            } catch (IOException unused) {
            }
        }
        byteArrayOutputStream.close();
        inputStreamOpenRawResource.close();
        return byteArrayOutputStream.toString();
    }

    public static String GetLocalStoragePath(Context context) {
        return context.getFilesDir().getPath();
    }

    public static Context getApplicationContext() {
        return s_context;
    }

    public static void NotificationRegisterCallback(String str) {
        Log.i(TAG, "NotificationRegisterCallback, token:" + str);
        notificiation_registration_callback(str);
    }

    public static void RegisterWithGNS(Context context) {
        Log.i("XSAPI.Android", "trying to register..");
    }

    public enum AuthFlowScreenStatus {
        NO_ERROR(0),
        ERROR_USER_CANCEL(1),
        PROVIDER_ERROR(2);

        private final int id;

        AuthFlowScreenStatus(int i) {
            this.id = i;
        }

        public int getId() {
            return this.id;
        }
    }

    public enum ErrorType {
        BAN(0),
        CREATION(1),
        OFFLINE(2),
        CATCHALL(3);

        private final int id;

        ErrorType(int i) {
            this.id = i;
        }

        public int getId() {
            return this.id;
        }
    }

    public enum ErrorStatus {
        TRY_AGAIN(0),
        CLOSE(1);

        private final int id;

        ErrorStatus(int i) {
            this.id = i;
        }

        public int getId() {
            return this.id;
        }
    }
}
