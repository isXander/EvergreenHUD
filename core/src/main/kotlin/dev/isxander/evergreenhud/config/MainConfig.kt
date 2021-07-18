/*
 * Copyright (C) isXander [2019 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.config

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.utils.JsonObjectExt
import java.io.File

class MainConfig(private val manager: ElementManager) {

    fun save() {
        val json = JsonObjectExt()
        json["schema"] = ElementConfig.SCHEMA
        json["data"] = manager.json

        json.writeToFile(CONFIG_FILE)
    }

    fun load() {
        if (!ElementConfig.CONFIG_FILE.exists()) save().also { return@load }
        val json = attemptConversion(JsonObjectExt.getFromFile(ElementConfig.CONFIG_FILE))

        manager.json = json["data", JsonObjectExt()]!!
    }

    private fun attemptConversion(json: JsonObjectExt): JsonObjectExt {
        val currentSchema = json["schema", 0]

        // corrupt config. Reset
        if (currentSchema == 0 || currentSchema > SCHEMA) {
            return JsonObjectExt()
        }

        // there is no point recoding every conversion
        // when a new schema comes to be
        // so just convert the old conversions until done
        var convertedJson = json
        var convertedSchema = currentSchema
        while (convertedSchema != SCHEMA) {
            LOGGER.info("Converting element configuration v$convertedSchema -> ${convertedSchema + 1}")
            when (convertedSchema) {

            }
            convertedSchema++
        }

        return convertedJson
    }

    companion object {
        const val SCHEMA = 4
        val CONFIG_FILE: File
            get() = File(EvergreenHUD.profileManager.profileDirectory, "global.json")
    }

}