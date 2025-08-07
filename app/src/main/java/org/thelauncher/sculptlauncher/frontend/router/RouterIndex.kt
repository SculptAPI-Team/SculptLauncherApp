package org.thelauncher.sculptlauncher.frontend.router

import kotlinx.serialization.Serializable

@Serializable
sealed class RouterIndex {
    @Serializable object HomePage: RouterIndex()
}