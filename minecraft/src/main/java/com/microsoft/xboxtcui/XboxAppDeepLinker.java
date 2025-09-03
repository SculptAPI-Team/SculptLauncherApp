package com.microsoft.xboxtcui;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import com.microsoft.xbox.telemetry.helpers.UTCDeepLink;
import com.microsoft.xbox.telemetry.utc.model.UTCNames;
import com.microsoft.xbox.toolkit.XLEAssert;

public class XboxAppDeepLinker {
    public static final String ACTION_FIND_PEOPLE = "com.microsoft.xbox.action.ACTION_FIND_PEOPLE";
    private static final String ACTION_VIEW_ACHIEVEMENTS = "com.microsoft.xbox.action.ACTION_VIEW_ACHIEVEMENTS";
    private static final String ACTION_VIEW_GAME_PROFILE = "com.microsoft.xbox.action.ACTION_VIEW_GAME_PROFILE";
    private static final String ACTION_VIEW_SETTINGS = "com.microsoft.xbox.action.ACTION_VIEW_SETTINGS";
    private static final String ACTION_VIEW_USER_PROFILE = "com.microsoft.xbox.action.ACTION_VIEW_USER_PROFILE";
    private static final String AMAZON_FIRE_TV_MODEL_PREFIX = "AFT";
    private static final String AMAZON_MANUFACTURER = "Amazon";
    private static final String AMAZON_STORE_URI = "amzn://apps/android?p=";
    private static final String AMAZON_TABLET_STORE_PACKAGE = "com.amazon.venezia";
    private static final String AMAZON_UNDERGROUND_PACKAGE = "com.amazon.mShop.android";
    private static final String EXTRA_IS_XBOX360_GAME = "com.microsoft.xbox.extra.IS_XBOX360_GAME";
    private static final String EXTRA_TITLEID = "com.microsoft.xbox.extra.TITLEID";
    private static final String EXTRA_XUID = "com.microsoft.xbox.extra.XUID";
    private static final String OCULUS_STORE_WEB_URI = "oculus.store://link/products?referrer=manual&item_id=";
    private static final String OCULUS_XBOXAPP_APP_ID = "1193603937358048";
    private static final String PLAY_STORE_PACKAGE = "com.android.vending";
    private static final String PLAY_STORE_URI = "market://details?id=";
    private static final String PLAY_STORE_WEB_URI = "https://play.google.com/store/apps/details?id=";
    private static final String XBOXAPP_BETA_PACKAGE = "com.microsoft.xboxone.smartglass.beta";
    private static final String XBOXAPP_PACKAGE = "com.microsoft.xboxone.smartglass";
    private static boolean betaAppInstalled;
    private static boolean mainAppInstalled;

    private static String getActivityTitle() {
        return "DeepLink";
    }

    private XboxAppDeepLinker() {
    }

    public static boolean appDeeplinkingSupported() {
        return !(Build.MANUFACTURER.equalsIgnoreCase(AMAZON_MANUFACTURER) && Build.MODEL.startsWith(AMAZON_FIRE_TV_MODEL_PREFIX));
    }

    public static boolean showUserProfile(Context context, String str) {
        if (!appDeeplinkingSupported()) {
            return false;
        }
        String packageName = context == null ? "" : context.getPackageName();
        if (xboxAppIsInstalled(context)) {
            String strTrackUserProfileLink = UTCDeepLink.trackUserProfileLink(getActivityTitle(), packageName, str);
            Intent xboxAppLaunchIntent = getXboxAppLaunchIntent(context);
            xboxAppLaunchIntent.setAction(ACTION_VIEW_USER_PROFILE);
            xboxAppLaunchIntent.putExtra(EXTRA_XUID, str);
            xboxAppLaunchIntent.putExtra(UTCDeepLink.DEEPLINK_KEY_NAME, strTrackUserProfileLink);
            xboxAppLaunchIntent.putExtra(UTCDeepLink.CALLING_APP_KEY, packageName);
            context.startActivity(xboxAppLaunchIntent);
            return true;
        }
        UTCDeepLink.trackUserSendToStore(getActivityTitle(), packageName, UTCNames.PageAction.DeepLink.UserProfile);
        launchXboxAppStorePage(context);
        return true;
    }

    public static boolean showTitleHub(Context context, String str) {
        if (!appDeeplinkingSupported()) {
            return false;
        }
        String packageName = context == null ? "" : context.getPackageName();
        if (xboxAppIsInstalled(context)) {
            String strTrackGameHubLink = UTCDeepLink.trackGameHubLink(getActivityTitle(), packageName, str);
            Intent xboxAppLaunchIntent = getXboxAppLaunchIntent(context);
            xboxAppLaunchIntent.setAction(ACTION_VIEW_GAME_PROFILE);
            xboxAppLaunchIntent.putExtra(EXTRA_TITLEID, str);
            xboxAppLaunchIntent.putExtra(UTCDeepLink.DEEPLINK_KEY_NAME, strTrackGameHubLink);
            xboxAppLaunchIntent.putExtra(UTCDeepLink.CALLING_APP_KEY, packageName);
            context.startActivity(xboxAppLaunchIntent);
            return true;
        }
        UTCDeepLink.trackUserSendToStore(getActivityTitle(), packageName, UTCNames.PageAction.DeepLink.TitleHub);
        launchXboxAppStorePage(context);
        return true;
    }

    public static boolean showTitleAchievements(Context context, String str) {
        if (!appDeeplinkingSupported()) {
            return false;
        }
        String packageName = context == null ? "" : context.getPackageName();
        if (xboxAppIsInstalled(context)) {
            String strTrackGameHubAchievementsLink = UTCDeepLink.trackGameHubAchievementsLink(getActivityTitle(), packageName, str);
            Intent xboxAppLaunchIntent = getXboxAppLaunchIntent(context);
            xboxAppLaunchIntent.setAction(ACTION_VIEW_ACHIEVEMENTS);
            xboxAppLaunchIntent.putExtra(EXTRA_TITLEID, str);
            xboxAppLaunchIntent.putExtra(UTCDeepLink.DEEPLINK_KEY_NAME, strTrackGameHubAchievementsLink);
            xboxAppLaunchIntent.putExtra(UTCDeepLink.CALLING_APP_KEY, packageName);
            context.startActivity(xboxAppLaunchIntent);
            return true;
        }
        UTCDeepLink.trackUserSendToStore(getActivityTitle(), packageName, UTCNames.PageAction.DeepLink.TitleAchievements);
        launchXboxAppStorePage(context);
        return true;
    }

    public static boolean showUserSettings(Context context) {
        if (!appDeeplinkingSupported()) {
            return false;
        }
        String packageName = context == null ? "" : context.getPackageName();
        if (xboxAppIsInstalled(context)) {
            String strTrackUserSettingsLink = UTCDeepLink.trackUserSettingsLink(getActivityTitle(), packageName);
            Intent xboxAppLaunchIntent = getXboxAppLaunchIntent(context);
            xboxAppLaunchIntent.setAction(ACTION_VIEW_SETTINGS);
            xboxAppLaunchIntent.putExtra(UTCDeepLink.DEEPLINK_KEY_NAME, strTrackUserSettingsLink);
            xboxAppLaunchIntent.putExtra(UTCDeepLink.CALLING_APP_KEY, packageName);
            context.startActivity(xboxAppLaunchIntent);
            return true;
        }
        UTCDeepLink.trackUserSendToStore(getActivityTitle(), packageName, UTCNames.PageAction.DeepLink.UserSettings);
        launchXboxAppStorePage(context);
        return true;
    }

    public static boolean showAddFriends(Context context) {
        if (!appDeeplinkingSupported()) {
            return false;
        }
        String packageName = context == null ? "" : context.getPackageName();
        if (xboxAppIsInstalled(context)) {
            String strTrackFriendSuggestionsLink = UTCDeepLink.trackFriendSuggestionsLink(getActivityTitle(), packageName);
            Intent xboxAppLaunchIntent = getXboxAppLaunchIntent(context);
            xboxAppLaunchIntent.setAction(ACTION_FIND_PEOPLE);
            xboxAppLaunchIntent.putExtra(UTCDeepLink.DEEPLINK_KEY_NAME, strTrackFriendSuggestionsLink);
            xboxAppLaunchIntent.putExtra(UTCDeepLink.CALLING_APP_KEY, packageName);
            context.startActivity(xboxAppLaunchIntent);
            return true;
        }
        UTCDeepLink.trackUserSendToStore(getActivityTitle(), packageName, UTCNames.PageAction.DeepLink.FriendSuggestions);
        launchXboxAppStorePageInOculusStore(context);
        return true;
    }

    private static boolean xboxAppIsInstalled(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.microsoft.xboxone.smartglass", 1);
            mainAppInstalled = true;
        } catch (PackageManager.NameNotFoundException unused) {
            mainAppInstalled = false;
        }
        try {
            context.getPackageManager().getPackageInfo("com.microsoft.xboxone.smartglass.beta", 1);
            betaAppInstalled = true;
        } catch (PackageManager.NameNotFoundException unused2) {
            betaAppInstalled = false;
        }
        return mainAppInstalled || betaAppInstalled;
    }

    private static Intent getXboxAppLaunchIntent(Context context) {
        XLEAssert.assertTrue(mainAppInstalled || betaAppInstalled);
        if (betaAppInstalled) {
            return context.getPackageManager().getLaunchIntentForPackage("com.microsoft.xboxone.smartglass.beta");
        }
        return context.getPackageManager().getLaunchIntentForPackage("com.microsoft.xboxone.smartglass");
    }

    @SuppressLint("WrongConstant")
    private static void launchXboxAppStorePage(Context context) {
        Intent xboxAppInStoreIntent = getXboxAppInStoreIntent(context, PLAY_STORE_URI, "com.android.vending");
        if (xboxAppInStoreIntent == null) {
            xboxAppInStoreIntent = getXboxAppInStoreIntent(context, AMAZON_STORE_URI, AMAZON_UNDERGROUND_PACKAGE);
        }
        if (xboxAppInStoreIntent == null) {
            xboxAppInStoreIntent = getXboxAppInStoreIntent(context, AMAZON_STORE_URI, AMAZON_TABLET_STORE_PACKAGE);
        }
        if (xboxAppInStoreIntent == null) {
            xboxAppInStoreIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.microsoft.xboxone.smartglass"));
        }
        xboxAppInStoreIntent.setFlags(270565376);
        context.startActivity(xboxAppInStoreIntent);
    }

    @SuppressLint("WrongConstant")
    private static void launchXboxAppStorePageInOculusStore(Context context) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("oculus.store://link/products?referrer=manual&item_id=1193603937358048"));
        intent.setFlags(270565376);
        context.startActivity(intent);
    }

    @SuppressLint("WrongConstant")
    private static Intent getXboxAppInStoreIntent(Context context, String str, String str2) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str + "com.microsoft.xboxone.smartglass"));
        for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(intent, 0)) {
            if (resolveInfo.activityInfo.applicationInfo.packageName.equals(str2)) {
                ActivityInfo activityInfo = resolveInfo.activityInfo;
                ComponentName componentName = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                intent.setFlags(270532608);
                intent.setComponent(componentName);
                return intent;
            }
        }
        return null;
    }
}
