package com.microsoft.xbox.toolkit;

import android.os.Handler;

public class ThreadManager {
    public static Handler Handler;
    public static Thread UIThread;

    public static void UIThreadPost(Runnable runnable) {
        UIThreadPostDelayed(runnable, 0L);
    }

    public static void UIThreadPostDelayed(Runnable runnable, long j) {
        Handler.postDelayed(runnable, j);
    }

    public static void UIThreadSend(final Runnable runnable) {
        if (UIThread == Thread.currentThread()) {
            runnable.run();
            return;
        }
        final Ready ready = new Ready();
        Handler.post(new Runnable() { // from class: com.microsoft.xbox.toolkit.ThreadManager.1
            @Override // java.lang.Runnable
            public void run() {
                runnable.run();
                ready.setReady();
            }
        });
        ready.waitForReady();
    }
}
