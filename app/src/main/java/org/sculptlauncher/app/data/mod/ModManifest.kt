package org.sculptlauncher.app.data.mod

data class ModManifest(
    val nmod: ModBasic,
    val developers: List<ModDev>,
    val requirements: ModReq,
    val dependencies: List<ModDep>
)

data class ModBasic(
    val name: String,
    val id: String,
    val version: Int,
    val repo: String?,
    val entrypoint: String,
    val icon: String
)

data class ModDev(
    val name: String,
    val repo: String?,
    val email: String
)

data class ModReq(
    val minLauncherVersion: Int,
    val maxLauncherVersion: Int
)

data class ModDep(
    val id: String,
    val version: String
)
