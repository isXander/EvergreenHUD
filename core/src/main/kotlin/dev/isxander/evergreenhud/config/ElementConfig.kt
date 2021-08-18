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
import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import dev.isxander.evergreenhud.elements.ElementManager
import java.io.File

class ElementConfig(private val manager: ElementManager) {

    private var shouldSave = false

    fun save() {
        val data = Config {
            this["schema"] = SCHEMA
        }

        val arr = mutableListOf<Config>()
        for (element in manager) {
            arr.add(Config {
                this["id"] = manager.getElementId(element)
                this["data"] = element.conf
            })
        }
        data["elements"] = arr

        CONFIG_FILE.parentFile.mkdirs()
        data.toToml.toFile(CONFIG_FILE)
        shouldSave = false
    }

    fun load() {
        if (!CONFIG_FILE.exists()) save().also { return@load }
        val data = attemptConversion(Config().from.toml.file(CONFIG_FILE))

        val arr: List<Config> = data["elements"]
        for (elementData in arr) {
            val id = elementData.getOrNull("id") ?: "null"
            val element = manager.getNewElementInstance(id)

            if (element == null) {
                LOGGER.err("Found unknown element id ($id) in json! This probably means someone tampered with the json!")
                continue
            }

            element.preload().conf = elementData["data"]
            manager.addElement(element)
        }

        if (shouldSave) save()
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
            LOGGER.info("Converting element configuration v$convertedSchema -> v${convertedSchema + 1}")
            when (convertedSchema) {

            }
            convertedSchema++
        }

        return convertedData
    }

    companion object {
        const val SCHEMA = 4
        val CONFIG_FILE: File
            get() = File(EvergreenHUD.profileManager.profileDirectory, "elements.toml")
    }

}