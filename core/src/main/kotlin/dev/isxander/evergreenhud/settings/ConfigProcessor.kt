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

package dev.isxander.evergreenhud.settings

import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import dev.isxander.evergreenhud.utils.JsonObjectExt

interface ConfigProcessor {

    fun generateJson(): JsonObjectExt
    fun processJson(json: JsonObjectExt)

    fun addSettingToJson(setting: Setting<*, *>, json: JsonObjectExt) {
        val category = setting.getCategoryJsonKey()

        var obj = json
        for (categoryEntry in category) {
            val newObj = obj.optObject(categoryEntry, JsonObjectExt())!!
            obj.add(categoryEntry, newObj)
            obj = newObj
        }

        when (setting.jsonValue) {
            JsonValues.STRING -> obj.addProperty(setting.getNameJsonKey(), setting.getJsonValue() as String)
            JsonValues.BOOLEAN -> obj.addProperty(setting.getNameJsonKey(), setting.getJsonValue() as Boolean)
            JsonValues.FLOAT -> obj.addProperty(setting.getNameJsonKey(), setting.getJsonValue() as Float)
            JsonValues.INT -> obj.addProperty(setting.getNameJsonKey(), setting.getJsonValue() as Int)
        }

        LOGGER.info("\n${json.toPrettyString()}")
    }

}