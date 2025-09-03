package org.sculptlauncher.app.frontend.page.mod

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.sculptlauncher.app.frontend.component.GameCard

@Composable
fun ModManagerView(back: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GameCard("123")
    }
}