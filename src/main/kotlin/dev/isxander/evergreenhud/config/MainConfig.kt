/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.config

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.FileConfig
import com.electronwill.nightconfig.core.io.WritingMode
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.utils.jsonFormat
import dev.isxander.evergreenhud.utils.jsonWriter
import dev.isxander.evergreenhud.utils.logger
import java.io.File

class MainConfig(private val manager: ElementManager) {
    fun save() {
        val data = Config.of(jsonFormat)
        data.set<Int>("schema", SCHEMA)
        data.set<Config>("data", manager.conf)

        CONFIG_FILE.parentFile.mkdirs()
        jsonWriter.write(data, CONFIG_FILE, WritingMode.REPLACE)
    }

    fun load() {
        if (!CONFIG_FILE.exists()) save().also { return@load }
        val data = attemptConversion(FileConfig.of(CONFIG_FILE).apply { load() })

        manager.conf = data["data"]
    }

    @Suppress("UNUSED_EXPRESSION")
    private fun attemptConversion(data: Config): Config {
        val currentSchema = data["schema"] ?: 0

        // corrupt config. Reset
        if (currentSchema == 0 || currentSchema > SCHEMA) {
            return Config.of(jsonFormat)
        }

        // there is no point recoding every conversion
        // when a new schema comes to be
        // so just convert the old conversions until done
        var convertedData = data
        var convertedSchema = currentSchema
        while (convertedSchema != SCHEMA) {
            logger.info("Converting element configuration v$convertedSchema -> v${convertedSchema + 1}")
            when (convertedSchema) {

            }
            convertedSchema++
        }

        return convertedData
    }

    companion object {
        const val SCHEMA = 4
        val CONFIG_FILE: File
            get() = File(EvergreenHUD.profileManager.profileDirectory, "global.json")
    }
}
