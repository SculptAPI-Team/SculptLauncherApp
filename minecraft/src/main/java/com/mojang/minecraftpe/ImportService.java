package com.mojang.minecraftpe;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/ImportService.class */
public class ImportService extends Service {
    static final int MSG_CORRELATION_CHECK = 672;
    static final int MSG_CORRELATION_RESPONSE = 837;
    final Messenger mMessenger = new Messenger(new IncomingHandler(this));

    /* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/ImportService$IncomingHandler.class */
    class IncomingHandler extends Handler {
        final ImportService this$0;

        IncomingHandler(ImportService importService) {
            this.this$0 = importService;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != ImportService.MSG_CORRELATION_CHECK) {
                super.handleMessage(message);
                return;
            }
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.this$0.getApplicationContext());
            String string = defaultSharedPreferences.getString("deviceId", "?");
            String string2 = defaultSharedPreferences.getString("LastDeviceSessionId", "");
            if (string == "?") {
                return;
            }
            long j = 0;
            try {
                j = this.this$0.getPackageManager().getPackageInfo(this.this$0.getApplicationContext().getPackageName(), 0).firstInstallTime;
            } catch (PackageManager.NameNotFoundException e) {
            }
            Bundle bundle = new Bundle();
            bundle.putLong("time", j);
            bundle.putString("deviceId", string);
            bundle.putString("sessionId", string2);
            Message messageObtain = Message.obtain((Handler) null, ImportService.MSG_CORRELATION_RESPONSE);
            messageObtain.setData(bundle);
            try {
                message.replyTo.send(messageObtain);
            } catch (RemoteException e) {
            }
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mMessenger.getBinder();
    }
}
