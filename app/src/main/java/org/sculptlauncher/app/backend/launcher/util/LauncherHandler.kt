package org.sculptlauncher.app.backend.launcher.util

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import org.sculptlauncher.app.frontend.activity.GamePlayActivity
import org.sculptlauncher.app.MainActivity

class LauncherHandler : Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when (msg.what) {
            1 -> {
                val intent = Intent(
                    MainActivity.activity.applicationContext,
                    GamePlayActivity::class.java
                )
                intent.putExtras(msg.data)
                MainActivity.activity.startActivity(intent)
                MainActivity.activity.finish()
            }
        }
    }
}