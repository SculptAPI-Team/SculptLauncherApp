package org.sculptlauncher.app.frontend.page

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Start
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import org.sculptlauncher.app.frontend.ModManager
import org.sculptlauncher.app.frontend.activity.GameStage
import org.sculptlauncher.app.frontend.component.GameCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    goPage: (NavKey) -> Unit
) {
    val context = LocalContext.current
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
                                goPage(ModManager)
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
        floatingActionButton = {
            FloatingActionButton({
                val intent = Intent(context, GameStage::class.java)
                context.startActivity(intent)
            }) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(Icons.Default.Start, "start")
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(horizontal = 16.dp)) {
            Card(modifier = Modifier.padding(bottom = 16.dp)) {
                ListItem(
                    headlineContent = {
                        Text("原生模组的不稳定可能会对游戏产生较大影响，请谨慎添加。")
                    },
                    leadingContent = {
                        Icon(Icons.Default.Info, "kind tip")
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
            GameCard("1.21.94.01")
        }
    }
}