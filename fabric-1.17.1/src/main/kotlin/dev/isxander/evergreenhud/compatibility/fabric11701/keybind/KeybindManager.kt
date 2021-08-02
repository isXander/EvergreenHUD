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

package dev.isxander.evergreenhud.compatibility.fabric11701.keybind

import dev.isxander.evergreenhud.compatibility.universal.impl.keybind.AIKeybindManager
import dev.isxander.evergreenhud.compatibility.universal.impl.keybind.CustomKeybind
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil

class KeybindManager : AIKeybindManager() {

    private val keyBindings = HashMap<CustomKeybind, KeyBinding>()

    init {
        ClientTickEvents.END_CLIENT_TICK.register {
            keyBindings.forEach { (custom, bind) ->
                if (bind.wasPressed()) custom.press.invoke()
            }
        }
    }

    override fun registerKeybind(keybind: CustomKeybind) {
        val mcBind = KeyBindingHelper.registerKeyBinding(KeyBinding(
            keybind.name,
            InputUtil.Type.KEYSYM,
            keybind.key.glfw,
            keybind.category
        ))

        keyBindings[keybind] = mcBind
    }

}