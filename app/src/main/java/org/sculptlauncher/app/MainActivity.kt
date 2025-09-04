package org.sculptlauncher.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.bytedance.shadowhook.ShadowHook
import org.sculptlauncher.app.frontend.screen.Home
import org.sculptlauncher.app.frontend.screen.Import
import org.sculptlauncher.app.frontend.theme.SculptLauncherTheme

class MainActivity : AppCompatActivity() {
    private var pendingUri: Uri? = null

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        activity = this
        handleIntent(intent)
        setContent {
            SculptLauncherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigator(Home()) {
                        LaunchedEffect(pendingUri) {
                            pendingUri?.let { uri ->
                                println(uri)
                                it.push(Import(uri))
                                pendingUri = null
                            }
                        }
                        CurrentScreen()
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_VIEW) {
            pendingUri = intent.data
        }
    }

    companion object {
        init {
            ShadowHook.init(ShadowHook.ConfigBuilder().setMode(ShadowHook.Mode.UNIQUE).build())
            System.loadLibrary("app")
        }

        lateinit var activity: MainActivity
    }
}