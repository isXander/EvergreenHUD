/*
 *
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 *
 */

package dev.isxander.evergreenhud.utils

import dev.isxander.evergreenhud.EvergreenHUD
import net.minecraft.util.Identifier
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.*

fun resource(path: String) = Identifier(EvergreenHUD.ID, path)

val InputStream.base64: String
    get() = Base64.getEncoder().encodeToString(this.readBytes())

fun fromBase64(string: String): InputStream =
    ByteArrayInputStream(Base64.getDecoder().decode(string))
