/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
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
