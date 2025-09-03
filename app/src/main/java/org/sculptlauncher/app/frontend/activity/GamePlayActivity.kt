package org.sculptlauncher.app.frontend.activity

import android.os.Bundle
import com.mojang.minecraftpe.MainActivity
import org.sculptlauncher.app.LauncherApp
import org.sculptlauncher.app.backend.launcher.AbstractMCPE

class GamePlayActivity : MainActivity() {
    private fun getPESdk(): AbstractMCPE = LauncherApp.Companion.mAbstractMCPE

    override fun onCreate(bundle: Bundle?) {
        getPESdk().gameManager.doingWhenMCLaunching(this, bundle)
        super.onCreate(bundle)
    }
}