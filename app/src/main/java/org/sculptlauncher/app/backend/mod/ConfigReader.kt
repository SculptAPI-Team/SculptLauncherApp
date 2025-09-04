package org.sculptlauncher.app.backend.mod

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.sculptlauncher.app.data.mod.ModManifest
import java.io.File

object ConfigReader {
    private val yamlMapper by lazy {
        ObjectMapper(YAMLFactory())
            .registerModule(KotlinModule.Builder().build())
    }

    fun readConfig(configPath: String): ModManifest =
        try {
            yamlMapper.readValue<ModManifest>(File(configPath))
        } catch (e: Exception) {
            throw IllegalStateException("无法读取配置文件，${e.message}。")
        }
}