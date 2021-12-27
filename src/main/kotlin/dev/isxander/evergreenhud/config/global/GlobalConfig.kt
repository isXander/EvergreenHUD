/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.config.global

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.utils.decode
import dev.isxander.evergreenhud.utils.json
import dev.isxander.evergreenhud.utils.logger
import dev.isxander.settxi.serialization.asJson
import dev.isxander.settxi.serialization.populateFromJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.io.File

class GlobalConfig(private val manager: ElementManager) {
    fun save() {
        val globalJson = GlobalJson(SCHEMA, manager.settings.asJson())

        CONFIG_FILE.parentFile.mkdirs()
        CONFIG_FILE.writeText(json.encodeToString(globalJson))
    }

    fun load() {
        if (!CONFIG_FILE.exists()) save().also { return@load }
        val text = CONFIG_FILE.readText()

        val schema = json.decodeFromString<JsonObject>(text).decode<Int>("schema")

        val data: GlobalJson
        if (schema != SCHEMA) {
            logger.warn("Schema $schema does not match current schema ${SCHEMA}. Attempting conversion.")
            data = json.decodeFromJsonElement(attemptConversion(json.decodeFromString(text)) ?: run { save(); return })

            logger.warn("Saving due to conversion.")
            save()
        } else {
            data = json.decodeFromString(text)
        }

        manager.settings.populateFromJson(data.settings)
    }

    private fun attemptConversion(data: JsonObject): JsonObject? {
        val currentSchema = data.decode<Int>("schema") ?: 0

        // corrupt config. Reset
        if (currentSchema == 0 || currentSchema > SCHEMA) {
            return null
        }

        var convertedData = data
        var convertedSchema = currentSchema
        while (convertedSchema != SCHEMA) {
            logger.info("Converting element configuration v$convertedSchema -> v${convertedSchema + 1}")
            convertedData = when (convertedSchema) {
                4 -> {
                    JsonObject(
                        mapOf(
                            "schema" to JsonPrimitive(5),
                            "settings" to convertedData["data"]!!,
                        )
                    )
                }
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
            get() = File(EvergreenHUD.profileManager.profileDirectory, "global.json")
    }
}
