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

package dev.isxander.evergreenhud.api.forge10809.impl

import club.chachy.event.on
import dev.isxander.evergreenhud.api.forge10809.mc
import dev.isxander.evergreenhud.api.impl.CustomKeybind
import dev.isxander.evergreenhud.api.impl.UKeybindManager
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.gameevent.TickEvent

class KeybindManagerImpl : UKeybindManager() {
    override fun registerKeybind(keybind: CustomKeybind): CustomKeybind {
        val mcBind = KeyBinding(
            keybind.name,
            keybind.key.lwjgl2,
            keybind.category
        )
        var result = mcBind

        if (mc.gameSettings.keyBindings.any { it.keyCategory == mcBind.keyCategory && it.keyDescription == mcBind.keyDescription }) {
            var tries = 0
            while (mc.gameSettings.keyBindings.any { it.keyCategory == mcBind.keyCategory && it.keyDescription == "${mcBind.keyDescription} (${tries++}" }) {}
            result = KeyBinding(
                "${mcBind.keyDescription} (${tries}",
                keybind.key.lwjgl2,
                keybind.category,
            )
        }
        ClientRegistry.registerKeyBinding(mcBind)

        var pressed = false
        on<TickEvent.ClientTickEvent>()
            .filter { it.phase == TickEvent.Phase.END }
            .subscribe {
                if (mcBind.isKeyDown && !pressed) keybind.onDown?.invoke()
                if (!mcBind.isKeyDown && pressed) keybind.onUp?.invoke()
                pressed = mcBind.isKeyDown

                keybind.keyDown = mcBind.isKeyDown
                keybind.pressed = mcBind.isPressed
            }

        return keybind.apply {
            name = result.keyDescription
        }
    }

    override fun unregisterKeybind(keybind: CustomKeybind) {
        mc.gameSettings.keyBindings =
            mc.gameSettings.keyBindings.toMutableList().apply {
                removeIf { it.keyCategory == keybind.category && it.keyDescription == keybind.name }
            }.toTypedArray()
    }
}
