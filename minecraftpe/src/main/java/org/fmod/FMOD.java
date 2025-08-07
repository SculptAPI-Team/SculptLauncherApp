package org.fmod;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import java.io.FileNotFoundException;

/* loaded from: classes2-dex2jar.jar:org/fmod/FMOD.class */
public class FMOD {
    private static Context gContext;
    private static PluginBroadcastReceiver gPluginBroadcastReceiver = new PluginBroadcastReceiver();

    /* loaded from: classes2-dex2jar.jar:org/fmod/FMOD$PluginBroadcastReceiver.class */
    static class PluginBroadcastReceiver extends BroadcastReceiver {
        PluginBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            FMOD.OutputAAudioHeadphonesChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native void OutputAAudioHeadphonesChanged();

    public static boolean checkInit() {
        return gContext != null;
    }

    public static void close() {
        Context context = gContext;
        if (context != null) {
            context.unregisterReceiver(gPluginBroadcastReceiver);
        }
        gContext = null;
    }

    public static int fileDescriptorFromUri(String str) {
        if (gContext == null) {
            return -1;
        }
        try {
            return gContext.getContentResolver().openFileDescriptor(Uri.parse(str), "r").detachFd();
        } catch (FileNotFoundException e) {
            return -1;
        }
    }

    public static AssetManager getAssetManager() {
        Context context = gContext;
        return context != null ? context.getAssets() : null;
    }

    public static int getOutputBlockSize() {
        String property;
        if (gContext == null || (property = ((AudioManager) gContext.getSystemService(Context.AUDIO_SERVICE)).getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER")) == null) {
            return 0;
        }
        return Integer.parseInt(property);
    }

    public static int getOutputSampleRate() {
        String property;
        if (gContext == null || (property = ((AudioManager) gContext.getSystemService(Context.AUDIO_SERVICE)).getProperty("android.media.property.OUTPUT_SAMPLE_RATE")) == null) {
            return 0;
        }
        return Integer.parseInt(property);
    }

    public static void init(Context context) {
        gContext = context;
        if (context != null) {
            gContext.registerReceiver(gPluginBroadcastReceiver, new IntentFilter("android.intent.action.HEADSET_PLUG"));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean isBluetoothOn() {
        /*
            android.content.Context r0 = org.fmod.FMOD.gContext
            r5 = r0
            r0 = 0
            r4 = r0
            r0 = r4
            r3 = r0
            r0 = r5
            if (r0 == 0) goto L28
            r0 = r5
            java.lang.String r1 = "audio"
            java.lang.Object r0 = r0.getSystemService(r1)
            android.media.AudioManager r0 = (android.media.AudioManager) r0
            r5 = r0
            r0 = r5
            boolean r0 = r0.isBluetoothA2dpOn()
            if (r0 != 0) goto L26
            r0 = r4
            r3 = r0
            r0 = r5
            boolean r0 = r0.isBluetoothScoOn()
            if (r0 == 0) goto L28
        L26:
            r0 = 1
            r3 = r0
        L28:
            r0 = r3
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.fmod.FMOD.isBluetoothOn():boolean");
    }

    public static boolean lowLatencyFlag() {
        if (gContext != null) {
            return gContext.getPackageManager().hasSystemFeature("android.hardware.audio.low_latency");
        }
        return false;
    }

    public static boolean proAudioFlag() {
        if (gContext != null) {
            return gContext.getPackageManager().hasSystemFeature("android.hardware.audio.pro");
        }
        return false;
    }

    public static boolean supportsAAudio() {
        return Build.VERSION.SDK_INT >= 27;
    }

    public static boolean supportsLowLatency() {
        int outputBlockSize = getOutputBlockSize();
        boolean zLowLatencyFlag = lowLatencyFlag();
        boolean zProAudioFlag = proAudioFlag();
        boolean z = outputBlockSize > 0 && outputBlockSize <= 1024;
        boolean zIsBluetoothOn = isBluetoothOn();
        Log.i("fmod", "FMOD::supportsLowLatency                 : Low latency = " + zLowLatencyFlag + ", Pro Audio = " + zProAudioFlag + ", Bluetooth On = " + zIsBluetoothOn + ", Acceptable Block Size = " + z + " (" + outputBlockSize + ")");
        return z && zLowLatencyFlag && !zIsBluetoothOn;
    }
}
