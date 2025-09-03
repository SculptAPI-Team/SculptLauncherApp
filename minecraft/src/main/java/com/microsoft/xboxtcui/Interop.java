package com.microsoft.xboxtcui;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.microsoft.xbox.toolkit.ui.ActivityParameters;
import com.microsoft.xbox.xle.app.activity.Profile.ProfileScreen;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class Interop {
    private static final String TAG = "Interop";
    private static final XboxTcuiWindowDialog.DetachedCallback detachedCallback = new XboxTcuiWindowDialog.DetachedCallback() { // from class: com.microsoft.xboxtcui.Interop.1
        @Override // com.microsoft.xboxtcui.XboxTcuiWindowDialog.DetachedCallback
        public void onDetachedFromWindow() {
            Interop.tcui_completed_callback(0);
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public static native void tcui_completed_callback(int i);

    public static void ShowProfileCardUI(Activity activity, String str, String str2, String str3) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        String str4 = TAG;
        Log.i(str4, "TCUI- ShowProfileCardUI: meXuid:" + str);
        Log.i(str4, "TCUI- ShowProfileCardUI: targeProfileXuid:" + str2);
        Log.i(str4, "TCUI- ShowProfileCardUI: privileges:" + str3);
        Activity foregroundActivity = getForegroundActivity();
        if (foregroundActivity == null) {
            foregroundActivity = activity;
        }
        final ActivityParameters activityParameters = new ActivityParameters();
        activityParameters.putMeXuid(str);
        activityParameters.putSelectedProfile(str2);
        activityParameters.putPrivileges(str3);
        Activity finalForegroundActivity = foregroundActivity;
        activity.runOnUiThread(new Runnable() { // from class: com.microsoft.xboxtcui.Interop.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    XboxTcuiWindowDialog xboxTcuiWindowDialog = new XboxTcuiWindowDialog(finalForegroundActivity, ProfileScreen.class, activityParameters);
                    xboxTcuiWindowDialog.setDetachedCallback(Interop.detachedCallback);
                    xboxTcuiWindowDialog.show();
                } catch (Exception e) {
                    Log.i(Interop.TAG, Log.getStackTraceString(e));
                    Interop.tcui_completed_callback(1);
                }
            }
        });
    }

    public static void ShowUserProfile(Context context, String str) {
        Log.i(TAG, "Deeplink - ShowUserProfile");
        if (XboxAppDeepLinker.showUserProfile(context, str)) {
            tcui_completed_callback(0);
        } else {
            tcui_completed_callback(1);
        }
    }

    public static void ShowTitleHub(Context context, String str) {
        Log.i(TAG, "Deeplink - ShowTitleHub");
        if (XboxAppDeepLinker.showTitleHub(context, str)) {
            tcui_completed_callback(0);
        } else {
            tcui_completed_callback(1);
        }
    }

    public static void ShowTitleAchievements(Context context, String str) {
        Log.i(TAG, "Deeplink - ShowTitleAchievements");
        if (XboxAppDeepLinker.showTitleAchievements(context, str)) {
            tcui_completed_callback(0);
        } else {
            tcui_completed_callback(1);
        }
    }

    public static void ShowUserSettings(Context context) {
        Log.i(TAG, "Deeplink - ShowUserSettings");
        if (XboxAppDeepLinker.showUserSettings(context)) {
            tcui_completed_callback(0);
        } else {
            tcui_completed_callback(1);
        }
    }

    public static void ShowAddFriends(Context context) {
        Log.i(TAG, "Deeplink - ShowAddFriends");
        if (XboxAppDeepLinker.showAddFriends(context)) {
            tcui_completed_callback(0);
        } else {
            tcui_completed_callback(1);
        }
    }

    private static Activity getForegroundActivity() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Object objInvoke = cls.getMethod("currentActivityThread", new Class[0]).invoke(null, new Object[0]);
            Field declaredField = cls.getDeclaredField("mActivities");
            declaredField.setAccessible(true);
            for (Object obj : ((Map) declaredField.get(objInvoke)).values()) {
                Class<?> cls2 = obj.getClass();
                Field declaredField2 = cls2.getDeclaredField("paused");
                declaredField2.setAccessible(true);
                if (!declaredField2.getBoolean(obj)) {
                    Field declaredField3 = cls2.getDeclaredField("activity");
                    declaredField3.setAccessible(true);
                    return (Activity) declaredField3.get(obj);
                }
            }
        } catch (Exception e) {
            Log.i(TAG, Log.getStackTraceString(e));
        }
        return null;
    }
}
