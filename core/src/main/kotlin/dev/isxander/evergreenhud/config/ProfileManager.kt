package dev.isxander.evergreenhud.config

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.utils.JsonObjectExt
import java.io.File

class ProfileManager {



    fun load() {
        val json = attemptConversion(JsonObjectExt.getFromFile(PROFILES_JSON))


    }

    private fun attemptConversion(json: JsonObjectExt): JsonObjectExt {
        val currentSchema = json["schema", 0]

        // Schema is never less than 1, we have a corrupt config. Reset
        if (currentSchema == 0) {
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
        val PROFILES_JSON = File(EvergreenHUD.DATA_DIR, "profiles.json")
    }

}

data class Profile(val name: String, val description: String, val icon: File = File(""))