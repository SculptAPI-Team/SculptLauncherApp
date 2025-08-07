package org.thelauncher.sculptlauncher

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.thelauncher.sculptlauncher.backend.launcher.GamePreloader

class StartActivity : AppCompatActivity() {

    val handler = PreloadHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)
        activity = this
        findViewById<Button>(R.id.button).setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                GamePreloader(
                    LauncherApp.mAbstractMCPE,
                    preloadListener = object : GamePreloader.GamePreloadListener() {
                        override fun onFinish(bundle: Bundle) {
                            val message = Message()
                            message.what = 1
                            message.data = bundle
                            handler.sendMessage(message)
                        }
                    }
                ).preload(applicationContext)
            }
        }
    }

    companion object {
        lateinit var activity: StartActivity
    }
}

class PreloadHandler: Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when(msg.what) {
            1 -> {
                val intent = Intent(
                    StartActivity.activity.applicationContext,
                    GamePlayActivity::class.java
                )
                intent.putExtras(msg.data)
                StartActivity.activity.startActivity(intent)
                StartActivity.activity.finish()
            }
        }
    }
}