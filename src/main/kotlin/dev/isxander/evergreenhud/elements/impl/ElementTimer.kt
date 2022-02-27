/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.mixins.AccessorKeyBindingRegistryImpl
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.extractString
import dev.isxander.evergreenhud.utils.registerKeyBind
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer

@ElementMeta(id = "evergreenhud:timer", name = "Timer", category = "Miscallaneous", description = "Set a timer.", credits = "Wyvest")
class ElementTimer : SimpleTextElement("Timer") {
    var keybind: KeyBinding? = null

    var elapsed = 0L
    var stopped = true
    val timer = timer(period = 1) {
        if (!stopped && isAdded) elapsed++
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
            stopped = !stopped
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
        bindingDsl: () -> Unit
    ): KeyBinding {
        val current = KeyBinding(keyTranslation, InputUtil.Type.KEYSYM, key, keyCategory)
        var result: KeyBinding = current

        try {
            KeyBindingHelper.registerKeyBinding(current)!!
        } catch (e: RuntimeException) {
            var tries = 1
            while (true) {
                result = KeyBinding("$keyTranslation (${tries++}", InputUtil.Type.KEYSYM, key, keyCategory)
                try {
                    result = registerKeyBind(result.defaultKey.code, result.defaultKey.category, result.translationKey, result.category, bindingDsl)
                    break
                } catch (e: RuntimeException) {}
            }
        }

        return result
    }

    private fun unregisterKeybind(keybind: KeyBinding) {
        AccessorKeyBindingRegistryImpl.getModdedKeyBindings().removeIf { it.category == keybind.category && it.translationKey == keybind.translationKey }
    }
}
