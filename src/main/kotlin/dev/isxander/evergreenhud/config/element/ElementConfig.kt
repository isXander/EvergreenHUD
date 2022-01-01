/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.config.element

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.utils.decode
import dev.isxander.evergreenhud.utils.json
import dev.isxander.evergreenhud.utils.logger
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.io.File

class ElementConfig(private val manager: ElementManager) {
    fun save() {
        val elementJson = ElementJson(SCHEMA, manager.currentElements)

        CONFIG_FILE.parentFile.mkdirs()
        CONFIG_FILE.writeText(json.encodeToString(elementJson))
    }

    fun load() {
        if (!CONFIG_FILE.exists()) save().also { return@load }
        val text = CONFIG_FILE.readText()

        val schema = json.decodeFromString<JsonObject>(text).decode<Int>("schema")

        val data: ElementJson
        if (schema != SCHEMA) {
            logger.warn("Schema $schema does not match current schema $SCHEMA. Attempting conversion.")
            data = json.decodeFromJsonElement(attemptConversion(json.decodeFromString(text)) ?: run { save(); return })

            logger.warn("Saving due to conversion.")
            save()
        } else {
            data = json.decodeFromString(text)
        }

        manager.currentElements.clear()
        manager.currentElements.addAll(data.elements)
    }

    @Suppress("UNUSED_EXPRESSION", "UNREACHABLE_CODE")
    private fun attemptConversion(data: JsonObject): JsonObject? {
        val currentSchema = data.decode<Int>("schema") ?: 0

        // corrupt config. Reset
        if (currentSchema == 0 || currentSchema > SCHEMA) {
            return null
        }

        // there is no point recoding every conversion
        // when a new schema comes to be
        // so just convert the old conversions until done
        var convertedData = data
        var convertedSchema = currentSchema
        while (convertedSchema != SCHEMA) {
            logger.info("Converting element configuration v$convertedSchema -> v${convertedSchema + 1}")
            convertedData = when (convertedSchema) {
                else -> {
                    logger.error("Unknown schema conversion tactic v$convertedSchema -> v${convertedSchema + 1}")
                    break
                }
            }
            convertedSchema++
        }

        return convertedData
    }

    companion object {
        const val SCHEMA = 5
        val CONFIG_FILE: File
            get() = File(EvergreenHUD.profileManager.profileDirectory, "elements.json")
    }
}
