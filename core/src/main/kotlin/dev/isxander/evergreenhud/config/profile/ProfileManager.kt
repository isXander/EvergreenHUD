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

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigObject
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import dev.isxander.evergreenhud.utils.HoconUtils
import dev.isxander.evergreenhud.utils.asConfig
import dev.isxander.evergreenhud.utils.int
import dev.isxander.evergreenhud.utils.string
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files

class ProfileManager {

    var currentProfile = DEFAULT_PROFILE
    val availableProfiles = hashMapOf(currentProfile.id to currentProfile)
    val profileDirectory: File
        get() = File(EvergreenHUD.dataDir, "profiles/${currentProfile.id}")

    fun load() {
        if (!PROFILES_DATA.exists()) save().also { return@load }

        val data = attemptConversion(ConfigFactory.parseFile(PROFILES_DATA).root())
        availableProfiles.clear()
        for (profileData in data.toConfig().getObjectList("profiles")) {
            val profile = Profile(
                profileData["id"]!!.string(),
                profileData["name"]!!.string(),
                profileData["description"]!!.string(),
                profileData["icon"]!!.string()
            )
            availableProfiles[profile.id] = profile
        }

        val newCurrent = availableProfiles[data.getOrDefault("current", "default".asConfig()).string()]
        if (newCurrent == null) {
            currentProfile = DEFAULT_PROFILE
            availableProfiles[currentProfile.id] = currentProfile
        } else {
            currentProfile = newCurrent
        }
    }

    fun save() {
        var data = ConfigFactory.empty()
            .withValue("schema", SCHEMA.asConfig())
            .withValue("current", currentProfile.id.asConfig())
            .root()

        val profileData = arrayListOf<ConfigObject>()
        for ((_, profile) in availableProfiles) {
            profileData.add(ConfigFactory.empty()
                .withValue("id", profile.id.asConfig())
                .withValue("name", profile.description.asConfig())
                .withValue("description", profile.description.asConfig())
                .withValue("icon", profile.icon.asConfig())
                .root()
            )
        }
        data = data.withValue("profiles", profileData.asConfig())

        PROFILES_DATA.parentFile.mkdirs()
        Files.write(
            PROFILES_DATA.toPath(),
            data.toConfig().resolve().root().render(HoconUtils.niceRender).lines(),
            StandardCharsets.UTF_8
        )
    }

    @Suppress("UNUSED_EXPRESSION")
    private fun attemptConversion(hocon: ConfigObject): ConfigObject {
        val currentSchema = hocon.getOrDefault("schema", 0.asConfig()).int()

        // corrupt config. Reset
        if (currentSchema == 0 || currentSchema > SCHEMA) {
            return ConfigFactory.empty().root()
        }

        // there is no point recoding every conversion
        // when a new schema comes to be
        // so just convert the old conversions until done
        var convertedHocon = hocon
        var convertedSchema = currentSchema
        while (convertedSchema != SCHEMA) {
            LOGGER.info("Converting profile configuration v$convertedSchema -> v${convertedSchema + 1}")
            when (convertedSchema) {

            }
            convertedSchema++
        }

        return convertedHocon
    }

    companion object {
        const val SCHEMA = 1
        val PROFILES_DATA = File(EvergreenHUD.dataDir, "profiles/profiles.conf")
        val DEFAULT_PROFILE = Profile("default", "Default", "The default profile of EvergreenHUD")
    }

}



