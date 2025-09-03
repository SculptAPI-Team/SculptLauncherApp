package com.microsoft.xbox.telemetry.helpers;

import com.microsoft.xbox.idp.ui.ErrorActivity;
import com.microsoft.xbox.telemetry.utc.CommonData;
import com.microsoft.xbox.telemetry.utc.model.UTCNames;

public class UTCTelemetry {
    public static final String UNKNOWNPAGE = "Unknown";

    public enum CallBackSources {
        Account,
        Ticket
    }

    public static void LogEvent(CommonData commonData) {
    }

    /* renamed from: com.microsoft.xbox.telemetry.helpers.UTCTelemetry$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$microsoft$xbox$idp$ui$ErrorActivity$ErrorScreen;

        static {
            int[] iArr = new int[ErrorActivity.ErrorScreen.values().length];
            $SwitchMap$com$microsoft$xbox$idp$ui$ErrorActivity$ErrorScreen = iArr;
            try {
                iArr[ErrorActivity.ErrorScreen.BAN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$microsoft$xbox$idp$ui$ErrorActivity$ErrorScreen[ErrorActivity.ErrorScreen.CATCHALL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$microsoft$xbox$idp$ui$ErrorActivity$ErrorScreen[ErrorActivity.ErrorScreen.CREATION.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$microsoft$xbox$idp$ui$ErrorActivity$ErrorScreen[ErrorActivity.ErrorScreen.OFFLINE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public static String getErrorScreen(ErrorActivity.ErrorScreen errorScreen) {
        int i = AnonymousClass1.$SwitchMap$com$microsoft$xbox$idp$ui$ErrorActivity$ErrorScreen[errorScreen.ordinal()];
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? String.format("%sErrorScreen", UNKNOWNPAGE) : UTCNames.PageView.Errors.Offline : UTCNames.PageView.Errors.Create : UTCNames.PageView.Errors.Generic : UTCNames.PageView.Errors.Banned;
    }
}
