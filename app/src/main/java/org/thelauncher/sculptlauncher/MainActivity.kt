package org.thelauncher.sculptlauncher

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bytedance.shadowhook.ShadowHook
import org.thelauncher.sculptlauncher.frontend.router.RouterIndex
import org.thelauncher.sculptlauncher.frontend.screen.Home
import org.thelauncher.sculptlauncher.frontend.theme.SculptLauncherTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        activity = this
        setContent {
            SculptLauncherTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = RouterIndex.HomePage
                    ) {
                        composable<RouterIndex.HomePage> { Home() }
                    }
                }
            }
        }
    }

    companion object {
        lateinit var activity: MainActivity

        init {
            ShadowHook.init(
                ShadowHook.ConfigBuilder()
                    .setMode(ShadowHook.Mode.UNIQUE)
                    .build()
            )
        }
    }
}