/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.api.fabric11701.impl

import dev.isxander.evergreenhud.api.impl.CustomKeybind
import dev.isxander.evergreenhud.api.impl.UKeybindManager
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil

class KeybindManagerImpl : UKeybindManager() {
    override fun registerKeybind(keybind: CustomKeybind) {
        val mcBind = KeyBindingHelper.registerKeyBinding(KeyBinding(
            keybind.name,
            InputUtil.Type.KEYSYM,
            keybind.key.glfw,
            keybind.category
        ))

        var pressed = false
        ClientTickEvents.END_CLIENT_TICK.register {
            if (mcBind.wasPressed() && !pressed) keybind.onDown()
            if (!mcBind.wasPressed() && pressed) keybind.onUp()
            pressed = mcBind.wasPressed()
        }
    }
}