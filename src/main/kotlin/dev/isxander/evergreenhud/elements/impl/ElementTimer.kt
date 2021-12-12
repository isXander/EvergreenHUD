/*
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
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.annotations.ElementMeta
import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.mixins.AccessorKeyBindingRegistryImpl
import io.ejekta.kambrik.Kambrik
import io.ejekta.kambrik.input.KambrikKeybind
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer

@ElementMeta(id = "TIMER", name = "Timer", category = "Miscallaneous", description = "Set a timer.", credits = "Wyvest")
class ElementTimer : SimpleTextElement() {
    var keybind: KambrikKeybind? = null

    var elapsed = 0L
    var stopped = true
    val timer = timer(period = 1) {
        if (!stopped) elapsed++
    }

    init {
        title = "Timer"
    }

    override fun calculateValue(): String {
        val date = Date(elapsed)

        var format = "s"
        if (elapsed >= TimeUnit.SECONDS.toMillis(10)) format = "s$format"
        if (elapsed >= TimeUnit.MINUTES.toMillis(1)) format = "m:$format"
        if (elapsed >= TimeUnit.MINUTES.toMillis(10)) format = "m$format"
        if (elapsed >= TimeUnit.HOURS.toMillis(1)) format = "h:$format"
        if (elapsed >= TimeUnit.HOURS.toMillis(10)) format = "h$format"

        return SimpleDateFormat(format).format(date)
    }

    override fun onAdded() {
        super.onAdded()

        keybind = registerKeyboardBindingIteratively(
            key = GLFW.GLFW_KEY_L,
            keyTranslation = "Toggle ${metadata.name}",
            keyCategory = "EvergreenHUD Timers",
        ) {
            onDown {
                stopped = !stopped
            }
        }
    }

    override fun onRemoved() {
        super.onRemoved()
        stopped = true

        if (keybind != null) unregisterKeybind(keybind!!)
    }

    private fun registerKeyboardBindingIteratively(
        key: Int = GLFW.GLFW_KEY_UNKNOWN,
        keyTranslation: String,
        keyCategory: String,
        realTime: Boolean = false,
        bindingDsl: KambrikKeybind.() -> Unit
    ): KambrikKeybind {
        val current = KambrikKeybind(keyTranslation, InputUtil.Type.KEYSYM, key, keyCategory, realTime).apply(bindingDsl)
        var result: KambrikKeybind = current

        try {
            KeyBindingHelper.registerKeyBinding(current)!!
        } catch (e: RuntimeException) {
            var tries = 1
            while (true) {
                result = KambrikKeybind("$keyTranslation (${tries++}", InputUtil.Type.KEYSYM, key, keyCategory, realTime).apply(bindingDsl)
                try {
                    result = KeyBindingHelper.registerKeyBinding(result)!! as KambrikKeybind
                    break
                } catch (e: RuntimeException) {}
            }
        }

        return result
    }

    private fun unregisterKeybind(keybind: KambrikKeybind) {
        AccessorKeyBindingRegistryImpl.getModdedKeyBindings().removeIf { it.category == keybind.category && it.translationKey == keybind.translationKey }
    }
}
