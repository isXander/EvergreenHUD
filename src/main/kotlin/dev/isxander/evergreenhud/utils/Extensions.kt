/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import dev.isxander.evergreenhud.EvergreenHUD
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.util.Identifier
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.*

fun resource(path: String) = Identifier(EvergreenHUD.ID, path)

val InputStream.base64: String
    get() = Base64.getEncoder().encodeToString(this.readBytes())

fun fromBase64(string: String): InputStream =
    ByteArrayInputStream(Base64.getDecoder().decode(string))

fun Identifier.readText(): String =
    mc.resourceManager.getResource(this).inputStream.readAllBytes().decodeToString()

fun KeyBinding.getBoundKey(): InputUtil.Key =
    KeyBindingHelper.getBoundKeyOf(this)
