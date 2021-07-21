/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/gpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.config

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.utils.JsonObjectExt
import java.io.File

class ElementConfig(private val manager: ElementManager) {

    private var shouldSave = false

    fun save() {
        val json = JsonObjectExt()
        json["schema"] = SCHEMA

        val arr = JsonArray()
        for (element in manager) {
            val obj = JsonObjectExt()
            obj["id"] = manager.getElementId(element)
            obj["data"] = element.json
            arr.add(obj.data)
        }
        json["elements"] = arr

        json.writeToFile(CONFIG_FILE)
        shouldSave = false
    }

    fun load() {
        if (!CONFIG_FILE.exists()) save().also { return@load }
        val json = attemptConversion(JsonObjectExt.getFromFile(CONFIG_FILE))

        val arr = json["elements", JsonArray()]!!
        for (elementJsonElement in arr) {
            if (elementJsonElement is JsonObject) {
                val elementJson = JsonObjectExt(elementJsonElement)
                val id = elementJson["id", "null"]
                val element = manager.getNewElementInstance(id)

                if (element == null) {
                    LOGGER.err("Found unknown element id ($id) in json! This probably means someone tampered with the json!")
                    continue
                }

                element.preload().json = elementJson["data", JsonObjectExt()]!!
                manager.currentElements.add(element)
            }
        }

        if (shouldSave) save()
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
            shouldSave = true
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
            get() = File(EvergreenHUD.profileManager.profileDirectory, "elements.json")
    }

}