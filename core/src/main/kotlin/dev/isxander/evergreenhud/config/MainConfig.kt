/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.config

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.toml
import com.uchuhimo.konf.source.toml.toToml
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.api.logger
import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.utils.setOrAdd
import dev.isxander.evergreenhud.utils.toFileMkdirs
import java.io.File

class MainConfig(private val manager: ElementManager) {

    fun save() {
        val data = Config {
            setOrAdd("schema", SCHEMA)
            setOrAdd("data", manager.conf)
        }.toToml.toFileMkdirs(CONFIG_FILE)
    }

    fun load() {
        if (!CONFIG_FILE.exists()) save().also { return@load }
        val data = attemptConversion(Config().from.toml.file(CONFIG_FILE))

        manager.conf = data["data"]
    }

    @Suppress("UNUSED_EXPRESSION")
    private fun attemptConversion(data: Config): Config {
        val currentSchema = data.getOrNull("schema") ?: 0

        // corrupt config. Reset
        if (currentSchema == 0 || currentSchema > SCHEMA) {
            return Config()
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
            get() = File(EvergreenHUD.profileManager.profileDirectory, "global.toml")
    }

}