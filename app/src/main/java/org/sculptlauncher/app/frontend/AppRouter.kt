package org.sculptlauncher.app.frontend

import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import kotlinx.serialization.Serializable
import org.sculptlauncher.app.frontend.page.HomePage

@Serializable
data object Home : NavKey

val appEntryProvider = entryProvider<Any> {
    entry<Home> { HomePage() }
}