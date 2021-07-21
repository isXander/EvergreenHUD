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

package dev.isxander.evergreenhud.config.profile

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.utils.JsonObjectExt
import java.io.File

class ProfileManager {

    var currentProfile = DEFAULT_PROFILE
    val availableProfiles = hashMapOf(currentProfile.id to currentProfile)
    val profileDirectory: File
        get() = File(EvergreenHUD.DATA_DIR, "profiles/${currentProfile.id}")

    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun load() {
        if (!PROFILES_JSON.exists()) save().also { return@load }

        val json = attemptConversion(JsonObjectExt.getFromFile(PROFILES_JSON))
        availableProfiles.clear()
        for (profileObj in json["profiles", JsonArray()]!!) {
            if (profileObj is JsonObject) {
                val profile = gson.fromJson(profileObj, Profile::class.java)
                availableProfiles[profile.id] = profile
            }
        }

        val newCurrent = availableProfiles[json["current", "default"]!!]
        if (newCurrent == null) {
            currentProfile = DEFAULT_PROFILE
            availableProfiles[currentProfile.id] = currentProfile
        } else {
            currentProfile = newCurrent
        }
    }

    fun save() {
        val json = JsonObjectExt()

        json["schema"] = SCHEMA
        json["current"] = currentProfile.id

        val profileJson = JsonArray()
        for ((_, profile) in availableProfiles) {
            profileJson.add(JsonObjectExt(gson.toJson(profile)).data)
        }
        json["profiles"] = profileJson

        json.writeToFile(PROFILES_JSON)
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
            when (convertedSchema) {

            }
            convertedSchema++
        }

        return convertedJson
    }

    companion object {
        const val SCHEMA = 1
        val PROFILES_JSON = File(EvergreenHUD.DATA_DIR, "profiles/profiles.json")
        val DEFAULT_PROFILE = Profile("default", "Default", "The default profile of EvergreenHUD")
    }

}



