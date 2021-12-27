/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.utils

import kotlinx.serialization.json.*

val json = Json {
    prettyPrint = true
}

inline fun <reified T> JsonObject.decode(key: String): T? {
    return this[key]?.let { json.decodeFromJsonElement(it) }
}
