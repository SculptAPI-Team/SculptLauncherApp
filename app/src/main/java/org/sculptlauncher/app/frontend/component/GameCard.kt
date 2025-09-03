package org.sculptlauncher.app.frontend.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.sculptlauncher.app.R

@Composable
fun GameCard(version: String) {
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = Modifier.clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.wallpaper),
            "",
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .height(230.dp),
            contentScale = ContentScale.FillBounds
        )
        Column(
            Modifier.fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(4.dp)
                .padding(start = 8.dp)
        ) {
            Text(
                "Minecraft: Bedrock Edition",
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.Bold)
            )
            Text(
                "游戏版本：${version} | 准备启动",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.outlineVariant)
            )
        }
    }
}