package org.sculptlauncher.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cafe.adriel.voyager.navigator.Navigator
import com.bytedance.shadowhook.ShadowHook
import org.sculptlauncher.app.frontend.screen.Home
import org.sculptlauncher.app.frontend.theme.SculptLauncherTheme

class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        activity = this
        setContent {
            SculptLauncherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigator(Home())
                }
            }
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