package com.microsoft.xbox.telemetry.helpers;

public class UTCEventTracker {
    public interface UTCEventDelegate {
        void call();
    }

    public interface UTCStringEventDelegate {
        String call();
    }

    public static void callTrackWrapper(UTCEventDelegate uTCEventDelegate) {
        try {
            uTCEventDelegate.call();
        } catch (Exception e) {
            UTCLog.log(e.getMessage(), new Object[0]);
        }
    }

    public static String callStringTrackWrapper(UTCStringEventDelegate uTCStringEventDelegate) {
        try {
            return uTCStringEventDelegate.call();
        } catch (Exception e) {
            UTCLog.log(e.getMessage(), new Object[0]);
            return null;
        }
    }
}
