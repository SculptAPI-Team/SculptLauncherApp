package com.microsoft.xbox.telemetry.helpers;

import com.microsoft.xbox.telemetry.utc.PageView;
import java.util.ArrayList;
import java.util.HashMap;

public class UTCPageView {
    private static ArrayList<String> pages = new ArrayList<>();

    public static int getSize() {
        if (pages == null) {
            pages = new ArrayList<>();
        }
        return pages.size();
    }

    public static String getCurrentPage() {
        int size = getSize();
        return size == 0 ? UTCTelemetry.UNKNOWNPAGE : pages.get(size - 1);
    }

    public static String getPreviousPage() {
        int size = getSize();
        return size < 2 ? UTCTelemetry.UNKNOWNPAGE : pages.get(size - 2);
    }

    public static void addPage(String str) {
        if (pages == null) {
            pages = new ArrayList<>();
        }
        if (pages.contains(str) || str == null) {
            return;
        }
        pages.add(str);
    }

    public static void removePage() {
        int size = getSize();
        if (size > 0) {
            pages.remove(size - 1);
        }
    }

    public static void track(String str, CharSequence charSequence) {
        track(str, charSequence, new HashMap());
    }

    public static void track(String str, CharSequence charSequence, HashMap<String, Object> map) {
        if (charSequence != null) {
            try {
                map.put("activityTitle", charSequence);
            } catch (Exception e) {
                UTCError.trackException(e, "UTCPageView.track");
                UTCLog.log(e.getMessage(), new Object[0]);
                return;
            }
        }
        addPage(str);
        String previousPage = getPreviousPage();
        PageView pageView = new PageView();
        pageView.pageName = str;
        pageView.fromPage = previousPage;
        pageView.additionalInfo = map;
        UTCLog.log("pageView:%s, fromPage:%s, additionalInfo:%s", str, previousPage, map);
        UTCTelemetry.LogEvent(pageView);
    }
}
