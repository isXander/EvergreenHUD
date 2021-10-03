/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.config

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.FileConfig
import com.electronwill.nightconfig.core.io.WritingMode
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.api.logger
import dev.isxander.evergreenhud.elements.Element
import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.utils.jsonFormat
import dev.isxander.evergreenhud.utils.jsonWriter
import java.io.File

class ElementConfig(private val manager: ElementManager) {
    private var shouldSave = false

    fun save() {
        val data = Config.of(jsonFormat)
        data.set<Int>("schema", SCHEMA)

        val arr = mutableListOf<Config>()
        for (element in manager) {
            arr.add(Config.of(jsonFormat).apply {
                set<String>("id", element.metadata.id)
                set<Config>("data", element.conf)
            })
        }
        data.set<List<Config>>("elements", arr)

        CONFIG_FILE.parentFile.mkdirs()
        jsonWriter.write(data, CONFIG_FILE, WritingMode.REPLACE)
        shouldSave = false
    }

    fun load() {
        if (!CONFIG_FILE.exists()) save().also { return@load }
        val data = attemptConversion(FileConfig.of(CONFIG_FILE).apply { load() })

        val arr: List<Config> = data["elements"]
        for (elementData in arr) {
            val id = elementData["id"] ?: "null"
            val element = manager.getNewElementInstance<Element>(id)

            if (element == null) {
                logger.err("Found unknown element id ($id) in json! This probably means someone tampered with the json!")
                continue
            }

            element.conf = elementData["data"]
            manager.addElement(element)
        }

        if (shouldSave) save()
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
            get() = File(EvergreenHUD.profileManager.profileDirectory, "elements.json")
    }
}
