package com.mojang.minecraftpe;

import android.content.pm.PackageManager;
import android.os.Process;
import android.util.Log;
/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/BrazeManager.class */
public class BrazeManager {
    private MainActivity mActivity;

    public BrazeManager(MainActivity mainActivity) {
        this.mActivity = mainActivity;
    }

    public void configureBrazeAtRuntime() {
        if (this.mActivity.isBrazeEnabled()) {
            //BrazeLogger.setLogLevel(2);
            //Braze.configure(this.mActivity.getApplicationContext(), new BrazeConfig.Builder().setApiKey(!this.mActivity.isPublishBuild() ? "d9430d08-2985-458e-9ed9-ef769d7e7e69" : "7e90f2bd-d27b-4010-a501-a8e30021418a").setAdmMessagingRegistrationEnabled(false).setSessionTimeout(10).setHandlePushDeepLinksAutomatically(true).setTriggerActionMinimumTimeIntervalSeconds(5).setNewsfeedVisualIndicatorOn(false).setBadNetworkDataFlushInterval(120).setGoodNetworkDataFlushInterval(60).setGreatNetworkDataFlushInterval(10).setGeofencesEnabled(false).setIsLocationCollectionEnabled(false).setIsFirebaseCloudMessagingRegistrationEnabled(false).setIsFirebaseMessagingServiceOnNewTokenRegistrationEnabled(false).setIsPushWakeScreenForNotificationEnabled(false).setSmallNotificationIcon("drawable/notification_icon_small").setDefaultNotificationAccentColor(5415989).build());
            this.mActivity.getApplication().registerActivityLifecycleCallbacks(new MinecraftActivityLifecycleCallbackListener());
        }
    }

    public void disableBrazeSDK() {
        if (this.mActivity.isBrazeEnabled()) {
            //Braze.disableSdk(this.mActivity.getApplicationContext());
        }
    }

    public void enableBrazeSDK() {
        if (this.mActivity.isBrazeEnabled()) {
            //Braze.enableSdk(this.mActivity.getApplicationContext());
        }
    }

    public boolean isBrazeSDKDisabled() {
        return true;
    }

    public void requestImmediateDataFlush() {
        //Braze.getInstance(this.mActivity.getApplicationContext()).requestImmediateDataFlush();
    }

    public void requestPushPermission() {
        Log.i("MinecraftPlatform", "MainActivity::requestPushPermission");
        if (this.mActivity.checkPermission("android.permission.POST_NOTIFICATIONS", Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED) {
            // from class: com.mojang.minecraftpe.BrazeManager.1
// java.lang.Runnable
            this.mActivity.runOnUiThread(() -> {
                mActivity.suspendGameplayUpdates();
                MainActivity mainActivity = mActivity;
                MainActivity unused = mActivity;
                mainActivity.requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 2);
            });
        }
    }

    public void setBrazeID(String str) {
        if (this.mActivity.isBrazeEnabled()) {
            //Braze.getInstance(this.mActivity.getApplicationContext()).changeUser(str);
        }
    }
}
