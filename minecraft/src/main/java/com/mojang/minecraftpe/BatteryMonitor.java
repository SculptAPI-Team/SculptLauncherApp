package com.mojang.minecraftpe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/BatteryMonitor.class */
public class BatteryMonitor extends BroadcastReceiver {
    public int mBatteryLevel = -1;
    public int mBatteryScale = -1;
    public int mBatteryStatus = -1;
    private int mBatteryTemperature = -1;
    private Context mContext;

    public BatteryMonitor(Context context) {
        this.mContext = context;
        context.registerReceiver(this, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
    }

    protected void finalize() {
        this.mContext.unregisterReceiver(this);
    }

    public int getBatteryLevel() {
        return this.mBatteryLevel;
    }

    public int getBatteryScale() {
        return this.mBatteryScale;
    }

    public int getBatteryStatus() {
        return this.mBatteryStatus;
    }

    public int getBatteryTemperature() {
        return this.mBatteryTemperature;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        this.mBatteryLevel = intent.getIntExtra("level", -1);
        this.mBatteryScale = intent.getIntExtra("scale", -1);
        this.mBatteryStatus = intent.getIntExtra("status", -1);
        this.mBatteryTemperature = intent.getIntExtra("temperature", -1);
    }
}
