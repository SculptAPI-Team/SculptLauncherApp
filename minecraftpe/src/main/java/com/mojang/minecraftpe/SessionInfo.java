package com.mojang.minecraftpe;

import android.content.Context;
import android.content.pm.PackageManager;
import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/SessionInfo.class */
public class SessionInfo implements Serializable {
    private static final String NOT_YET_CONFIGURED = "Not yet configured";
    private static final String SEP = ";";
    public int appVersion;
    public String branchId;
    public String buildId;
    public String commitId;
    public transient Date crashTimestamp;
    public String flavor;
    public String gameVersionName;
    public Date recordDate;
    public String sessionId;

    public SessionInfo() {
        this.appVersion = 0;
        this.recordDate = null;
        this.crashTimestamp = null;
        this.sessionId = NOT_YET_CONFIGURED;
        this.buildId = NOT_YET_CONFIGURED;
        this.commitId = NOT_YET_CONFIGURED;
        this.branchId = NOT_YET_CONFIGURED;
        this.flavor = NOT_YET_CONFIGURED;
        this.gameVersionName = NOT_YET_CONFIGURED;
        this.recordDate = new Date();
    }

    public SessionInfo(String str, String str2, String str3, String str4, String str5, String str6, int i, Date date) {
        this.crashTimestamp = null;
        this.sessionId = str;
        this.buildId = str2;
        this.commitId = str3;
        this.branchId = str4;
        this.flavor = str5;
        this.gameVersionName = str6;
        this.appVersion = i;
        this.recordDate = date;
    }

    public static SessionInfo fromString(String str) {
        SessionInfo sessionInfo = new SessionInfo();
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("Empty SessionInfo string");
        }
        String[] strArrSplit = str.split(SEP);
        if (strArrSplit.length != 8) {
            throw new IllegalArgumentException("Invalid SessionInfo string '" + str + "', must be 8 parts split by ';'");
        }
        sessionInfo.sessionId = strArrSplit[0];
        sessionInfo.buildId = strArrSplit[1];
        sessionInfo.commitId = strArrSplit[2];
        sessionInfo.branchId = strArrSplit[3];
        sessionInfo.flavor = strArrSplit[4];
        sessionInfo.gameVersionName = strArrSplit[5];
        try {
            sessionInfo.appVersion = Integer.parseInt(strArrSplit[6]);
            Date date = getDateFormat().parse(strArrSplit[7], new ParsePosition(0));
            sessionInfo.recordDate = date;
            if (date != null) {
                return sessionInfo;
            }
            throw new IllegalArgumentException("Failed to parse date/time in SessionInfo string '" + str + "'");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to convert app version '" + strArrSplit[6] + "' into an integer");
        }
    }

    public static SimpleDateFormat getDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat;
    }

    public void setContents(Context context, String str, String str2, String str3, String str4, String str5) {
        this.sessionId = str;
        this.buildId = str2;
        this.commitId = str3;
        this.branchId = str4;
        this.flavor = str5;
        updateJavaConstants(context);
    }

    public String toString() {
        return this.sessionId + SEP + this.buildId + SEP + this.commitId + SEP + this.branchId + SEP + this.flavor + SEP + this.gameVersionName + SEP + this.appVersion + SEP + getDateFormat().format(this.recordDate);
    }

    public void updateJavaConstants(Context context) {
        this.appVersion = AppConstants.APP_VERSION;
        try {
            this.gameVersionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            this.gameVersionName = "Not found";
        }
    }
}
