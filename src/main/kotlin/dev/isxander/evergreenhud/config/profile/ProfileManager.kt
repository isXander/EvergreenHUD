/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.config.profile

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.utils.decode
import dev.isxander.evergreenhud.utils.json
import dev.isxander.evergreenhud.utils.logger
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import java.io.File

class ProfileManager {
    var currentProfile = DEFAULT_PROFILE

    var availableProfiles = mutableMapOf(currentProfile.id to currentProfile)
        private set

    val profileDirectory: File
        get() = File(EvergreenHUD.dataDir, "profiles/${currentProfile.id}")

    fun save() {
        PROFILES_DATA.parentFile.mkdirs()

        val profilesJson = ProfilesJson(SCHEMA, currentProfile.id, availableProfiles.values.toList())

        PROFILES_DATA.writeText(json.encodeToString(profilesJson))
    }

    fun load() {
        if (!PROFILES_DATA.exists()) save().also { return@load }
        val text = PROFILES_DATA.readText()
        val schema = json.decodeFromString<JsonObject>(text).decode<Int>("schema")

        val data: ProfilesJson
        if (schema != SCHEMA) {
            logger.warn("Schema $schema does not match current schema ${SCHEMA}. Attempting conversion.")
            data = json.decodeFromJsonElement(attemptConversion(json.decodeFromString(text)) ?: run { save(); return })

            logger.warn("Saving due to conversion.")
            save()
        } else {
            data = json.decodeFromString(text)
        }

        availableProfiles = data.profiles.associateBy { it.id }.toMutableMap()

        currentProfile = availableProfiles[data.current] ?: run {
            availableProfiles[DEFAULT_PROFILE.id] = DEFAULT_PROFILE
            DEFAULT_PROFILE
        }
    }

    @Suppress("UNUSED_EXPRESSION", "UNREACHABLE_CODE")
    private fun attemptConversion(data: JsonObject): JsonObject? {
        val currentSchema = data["schema"]?.jsonPrimitive?.int ?: 0

        // corrupt config. Reset
        if (currentSchema == 0 || currentSchema > SCHEMA) {
            return null
        }

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
        const val SCHEMA = 1
        val PROFILES_DATA = File(EvergreenHUD.dataDir, "profiles/profiles.json")
        val DEFAULT_PROFILE = Profile(
            id = "default",
            name = "Default",
            description = "The default profile for EvergreenHUD",
        )
    }
}
