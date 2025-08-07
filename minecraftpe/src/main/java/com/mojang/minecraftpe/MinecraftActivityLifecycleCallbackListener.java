package com.mojang.minecraftpe;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/MinecraftActivityLifecycleCallbackListener.class */
public class MinecraftActivityLifecycleCallbackListener implements Application.ActivityLifecycleCallbacks {
    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
        //BrazeInAppMessageManager.getInstance().ensureSubscribedToInAppMessageEvents(activity.getApplicationContext());
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
        //BrazeInAppMessageManager.getInstance().unregisterInAppMessageManager(activity);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
        //BrazeInAppMessageManager.getInstance().registerInAppMessageManager(activity);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        //Braze.Companion.getInstance(activity.getApplicationContext()).closeSession(activity);
    }
}
