package org.sculptlauncher.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.bytedance.shadowhook.ShadowHook
import org.sculptlauncher.app.frontend.Home
import org.sculptlauncher.app.frontend.page.HomePage
import org.sculptlauncher.app.frontend.theme.SculptLauncherTheme

class MainActivity : ComponentActivity() {

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
                    val backStack = rememberNavBackStack(Home)

                    NavDisplay(
                        backStack = backStack,
                        entryDecorators =
                            listOf(
                                rememberSceneSetupNavEntryDecorator(),
                                rememberSavedStateNavEntryDecorator(),
                                rememberViewModelStoreNavEntryDecorator(),
                            ),
                        onBack = { backStack.removeLastOrNull() },
                        entryProvider = entryProvider({
                            NavEntry(it) { Text("Unknown destination") }
                        }) {
                            entry<Home> { HomePage() }
                        }
                    )
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