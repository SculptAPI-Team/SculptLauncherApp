package org.sculptlauncher.app.frontend.screen

import android.os.Bundle
import android.os.Message
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.AppsOutage
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sculptlauncher.app.LauncherApp
import org.sculptlauncher.app.backend.launcher.GamePreloader
import org.sculptlauncher.app.backend.launcher.util.LauncherHandler
import org.sculptlauncher.app.frontend.component.GameCard

class Home : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val gameNotInstalled = LauncherApp.mAbstractMCPE.minecraftInfo
            .minecraftPackageContext.packageName == context.packageName
        val gameVersion = LauncherApp.mAbstractMCPE.minecraftInfo.minecraftVersionName
        val handler = LauncherHandler()
        var openMenu by remember { mutableStateOf(false) }

        Scaffold (
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text("雕刻启动器")
                    },
                    actions = {
                        IconButton({ openMenu = true }) {
                            Icon(Icons.Default.MoreVert, "more option icon")
                        }
                        DropdownMenu(
                            openMenu,
                            { openMenu = !openMenu }
                        ) {
                            DropdownMenuItem(
                                { Text("管理模组") },
                                {
                                    openMenu = false
                                },
                                leadingIcon = { Icon(Icons.Default.AppRegistration, "") }
                            )
                            DropdownMenuItem(
                                { Text("启动选项") },
                                {},
                                leadingIcon = { Icon(Icons.Default.Settings, "") }
                            )
                            DropdownMenuItem(
                                { Text("关于") },
                                {},
                                leadingIcon = { Icon(Icons.Default.AccountBalance, "") }
                            )
                        }
                    }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (gameNotInstalled) {
                    ListItem(
                        headlineContent = { Text("游戏未安装") },
                        leadingContent = {
                            Icon(Icons.Default.Warning, "")
                        },
                        supportingContent = {
                            Text("你需要先安装游戏才能使用本软件。")
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    )
                }

                if (gameVersion != "1.21.94.1") {
                    Card {
                        ListItem(
                            headlineContent = { Text("当前安装的版本") },
                            supportingContent = {
                                Text("你当前已经安装了：${gameVersion}，但它是不符合要求的，请安装1.21.0.03后继续。")
                            },
                            leadingContent = {
                                Icon(Icons.Default.AppsOutage, "")
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    }
                } else {
                    GameCard(gameVersion)
                    Button(
                        {
                            scope.launch(Dispatchers.IO) {
                                GamePreloader(
                                    LauncherApp.mAbstractMCPE,
                                    preloadListener = object :
                                        GamePreloader.GamePreloadListener() {
                                        override fun onFinish(bundle: Bundle) {
                                            val message = Message()
                                            message.what = 1
                                            message.data = bundle
                                            handler.sendMessage(message)
                                        }
                                    }
                                ).preload(context)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.PlayCircleFilled, "")
                            Spacer(Modifier.width(8.dp))
                            Text("启动游戏")
                        }
                    }
                }
            }
        }
    }
}