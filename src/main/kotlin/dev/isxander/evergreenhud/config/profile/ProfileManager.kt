/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.config.profile

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.conversion.ObjectConverter
import com.electronwill.nightconfig.core.file.FileConfig
import com.electronwill.nightconfig.core.io.WritingMode
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.utils.jsonFormat
import dev.isxander.evergreenhud.utils.jsonWriter
import dev.isxander.evergreenhud.utils.logger
import java.io.File

class ProfileManager {
    var currentProfile = DEFAULT_PROFILE
    val availableProfiles = hashMapOf(currentProfile.id to currentProfile)
    val profileDirectory: File
        get() = File(EvergreenHUD.dataDir, "profiles/${currentProfile.id}")

    fun load() {
        if (!PROFILES_DATA.exists()) save().also { return@load }

        val data = attemptConversion(FileConfig.of(PROFILES_DATA).apply { load() })
        availableProfiles.clear()
        data.get<List<Config>>("profiles").map { ObjectConverter().toObject(it) { Profile {} } }.forEach { availableProfiles[it.id] = it }

        val newCurrent = availableProfiles[data["current"] ?: "default"]
        if (newCurrent == null) {
            currentProfile = DEFAULT_PROFILE
            availableProfiles[currentProfile.id] = currentProfile
        } else {
            currentProfile = newCurrent
        }
    }

    fun save() {
        PROFILES_DATA.parentFile.mkdirs()

        val config = Config.of(jsonFormat)

        config.set<Int>("schema", SCHEMA)
        config.set<String>("current", currentProfile.id)
        config.set<List<Config>>("profiles", availableProfiles.values.toList().map { ObjectConverter().toConfig(it) { Config.of(jsonFormat) } })

        jsonWriter.write(config, PROFILES_DATA, WritingMode.REPLACE)
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
        const val SCHEMA = 1
        val PROFILES_DATA = File(EvergreenHUD.dataDir, "profiles/profiles.json")
        val DEFAULT_PROFILE = Profile {
            id = "default"
            name = "Default"
            description = "The default profile for EvergreenHUD"
        }
    }
}
