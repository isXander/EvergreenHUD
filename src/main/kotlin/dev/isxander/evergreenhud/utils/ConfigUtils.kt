/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import kotlinx.serialization.json.*

val json = Json {
    prettyPrint = true
}

inline fun <reified T> JsonObject.decode(key: String): T? {
    return this[key]?.let { json.decodeFromJsonElement(it) }
}
