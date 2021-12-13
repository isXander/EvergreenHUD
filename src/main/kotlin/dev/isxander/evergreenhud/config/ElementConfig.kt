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
import dev.isxander.evergreenhud.elements.Element
import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.utils.jsonFormat
import dev.isxander.evergreenhud.utils.jsonWriter
import dev.isxander.evergreenhud.utils.logger
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
                logger.error("Found unknown element id ($id) in json! This probably means someone tampered with the json!")
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
