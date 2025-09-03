package com.microsoft.xbox.telemetry.helpers;

import com.microsoft.xbox.telemetry.utc.CommonData;
import com.microsoft.xbox.telemetry.utc.model.UTCNames;
import java.util.HashMap;

public class UTCDeepLink {
    public static final String CALLING_APP_KEY = "deepLinkCaller";
    public static final String DEEPLINK_KEY_NAME = "deepLinkId";
    public static final String INTENDED_ACTION_KEY = "intendedAction";
    public static final String TARGET_TITLE_KEY = "targetTitleId";
    public static final String TARGET_XUID_KEY = "targetXUID";

    private static String generateCorrelationId() {
        return CommonData.getApplicationSession();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static HashMap<String, Object> getAdditionalInfo(String str) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DEEPLINK_KEY_NAME, generateCorrelationId());
        map.put(CALLING_APP_KEY, str);
        return map;
    }

    public static String trackUserProfileLink(final CharSequence charSequence, final String str, final String str2) {
        return UTCEventTracker.callStringTrackWrapper(new UTCEventTracker.UTCStringEventDelegate() { // from class: com.microsoft.xbox.telemetry.helpers.UTCDeepLink.1
            @Override // com.microsoft.xbox.telemetry.helpers.UTCEventTracker.UTCStringEventDelegate
            public String call() {
                HashMap additionalInfo = UTCDeepLink.getAdditionalInfo(str);
                additionalInfo.put(UTCDeepLink.TARGET_XUID_KEY, "x:" + str2);
                UTCPageAction.track(UTCNames.PageAction.DeepLink.UserProfile, charSequence, additionalInfo);
                return additionalInfo.get(UTCDeepLink.DEEPLINK_KEY_NAME).toString();
            }
        });
    }

    public static String trackGameHubLink(final CharSequence charSequence, final String str, final String str2) {
        return UTCEventTracker.callStringTrackWrapper(new UTCEventTracker.UTCStringEventDelegate() { // from class: com.microsoft.xbox.telemetry.helpers.UTCDeepLink.2
            @Override // com.microsoft.xbox.telemetry.helpers.UTCEventTracker.UTCStringEventDelegate
            public String call() {
                HashMap additionalInfo = UTCDeepLink.getAdditionalInfo(str);
                additionalInfo.put(UTCDeepLink.TARGET_XUID_KEY, str2);
                UTCPageAction.track(UTCNames.PageAction.DeepLink.TitleHub, charSequence, additionalInfo);
                return additionalInfo.get(UTCDeepLink.DEEPLINK_KEY_NAME).toString();
            }
        });
    }

    public static String trackGameHubAchievementsLink(final CharSequence charSequence, final String str, final String str2) {
        return UTCEventTracker.callStringTrackWrapper(new UTCEventTracker.UTCStringEventDelegate() { // from class: com.microsoft.xbox.telemetry.helpers.UTCDeepLink.3
            @Override // com.microsoft.xbox.telemetry.helpers.UTCEventTracker.UTCStringEventDelegate
            public String call() {
                HashMap additionalInfo = UTCDeepLink.getAdditionalInfo(str);
                additionalInfo.put(UTCDeepLink.TARGET_TITLE_KEY, str2);
                UTCPageAction.track(UTCNames.PageAction.DeepLink.TitleHub, charSequence, additionalInfo);
                return additionalInfo.get(UTCDeepLink.DEEPLINK_KEY_NAME).toString();
            }
        });
    }

    public static String trackUserSettingsLink(final CharSequence charSequence, final String str) {
        return UTCEventTracker.callStringTrackWrapper(new UTCEventTracker.UTCStringEventDelegate() { // from class: com.microsoft.xbox.telemetry.helpers.UTCDeepLink.4
            @Override // com.microsoft.xbox.telemetry.helpers.UTCEventTracker.UTCStringEventDelegate
            public String call() {
                HashMap additionalInfo = UTCDeepLink.getAdditionalInfo(str);
                UTCPageAction.track(UTCNames.PageAction.DeepLink.UserSettings, charSequence, additionalInfo);
                return additionalInfo.get(UTCDeepLink.DEEPLINK_KEY_NAME).toString();
            }
        });
    }

    public static void trackUserSendToStore(final CharSequence charSequence, final String str, final String str2) {
        UTCEventTracker.callTrackWrapper(new UTCEventTracker.UTCEventDelegate() { // from class: com.microsoft.xbox.telemetry.helpers.UTCDeepLink.5
            @Override // com.microsoft.xbox.telemetry.helpers.UTCEventTracker.UTCEventDelegate
            public void call() {
                HashMap additionalInfo = UTCDeepLink.getAdditionalInfo(str);
                additionalInfo.put(UTCDeepLink.INTENDED_ACTION_KEY, str2);
                UTCPageAction.track(UTCNames.PageAction.DeepLink.SendToStore, charSequence, additionalInfo);
            }
        });
    }

    public static String trackFriendSuggestionsLink(final CharSequence charSequence, final String str) {
        return UTCEventTracker.callStringTrackWrapper(new UTCEventTracker.UTCStringEventDelegate() { // from class: com.microsoft.xbox.telemetry.helpers.UTCDeepLink.6
            @Override // com.microsoft.xbox.telemetry.helpers.UTCEventTracker.UTCStringEventDelegate
            public String call() {
                HashMap additionalInfo = UTCDeepLink.getAdditionalInfo(str);
                UTCPageAction.track(UTCNames.PageAction.DeepLink.FriendSuggestions, charSequence, additionalInfo);
                return additionalInfo.get(UTCDeepLink.DEEPLINK_KEY_NAME).toString();
            }
        });
    }
}
