/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
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
