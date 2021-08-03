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

import com.typesafe.config.*
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.utils.HoconUtils
import dev.isxander.evergreenhud.utils.asConfig
import dev.isxander.evergreenhud.utils.int
import dev.isxander.evergreenhud.utils.obj
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files

class MainConfig(private val manager: ElementManager) {

    fun save() {
        val hocon = ConfigFactory.empty()
            .withValue("schema", SCHEMA.asConfig())
            .withValue("data", manager.conf)
            .resolve().root().render(HoconUtils.niceRender)

        CONFIG_FILE.parentFile.mkdirs()
        Files.write(CONFIG_FILE.toPath(), hocon.lines(), StandardCharsets.UTF_8)
    }

    fun load() {
        if (!CONFIG_FILE.exists()) save().also { return@load }
        val data = attemptConversion(ConfigFactory.parseFile(CONFIG_FILE).root())

        manager.conf = data["data"]!!.obj()
    }

    @Suppress("UNUSED_EXPRESSION")
    private fun attemptConversion(hocon: ConfigObject): ConfigObject {
        val currentSchema = hocon.getOrDefault("schema", 0.asConfig()).int()

        // corrupt config. Reset
        if (currentSchema == 0 || currentSchema > SCHEMA) {
            return ConfigFactory.empty().root()
        }

        // there is no point recoding every conversion
        // when a new schema comes to be
        // so just convert the old conversions until done
        var convertedHocon = hocon
        var convertedSchema = currentSchema
        while (convertedSchema != SCHEMA) {
            LOGGER.info("Converting element configuration v$convertedSchema -> ${convertedSchema + 1}")
            when (convertedSchema) {

            }
            convertedSchema++
        }

        return convertedHocon
    }

    companion object {
        const val SCHEMA = 4
        val CONFIG_FILE: File
            get() = File(EvergreenHUD.profileManager.profileDirectory, "global.conf")
    }

}