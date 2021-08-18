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

package dev.isxander.evergreenhud.config.profile

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.toml
import com.uchuhimo.konf.source.toml.toToml
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import java.io.File

class ProfileManager {

    var currentProfile = DEFAULT_PROFILE
    val availableProfiles = hashMapOf(currentProfile.id to currentProfile)
    val profileDirectory: File
        get() = File(EvergreenHUD.dataDir, "profiles/${currentProfile.id}")

    fun load() {
        if (!PROFILES_DATA.exists()) save().also { return@load }

        val data = attemptConversion(Config().from.toml.file(PROFILES_DATA))
        availableProfiles.clear()
        data[ProfileSpec.profiles].forEach { availableProfiles[it.id] = it }

        val newCurrent = availableProfiles[data.getOrNull("current") ?: "default"]
        if (newCurrent == null) {
            currentProfile = DEFAULT_PROFILE
            availableProfiles[currentProfile.id] = currentProfile
        } else {
            currentProfile = newCurrent
        }
    }

    fun save() {
        PROFILES_DATA.parentFile.mkdirs()
        Config {
            addSpec(ProfileSpec)
            this[ProfileSpec.schema] = SCHEMA
            this[ProfileSpec.current] = currentProfile.id
            this[ProfileSpec.profiles] = availableProfiles.values.toList()
        }.toToml.toFile(PROFILES_DATA)
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

        convertedData.addSpec(ProfileSpec)
        return convertedData
    }

    companion object {
        const val SCHEMA = 1
        val PROFILES_DATA = File(EvergreenHUD.dataDir, "profiles/profiles.toml")
        val DEFAULT_PROFILE = Profile("default", "Default", "The default profile of EvergreenHUD")
    }

}



