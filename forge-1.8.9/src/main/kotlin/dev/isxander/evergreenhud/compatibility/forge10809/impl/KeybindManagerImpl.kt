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

package dev.isxander.evergreenhud.compatibility.forge10809.impl

import club.chachy.event.on
import dev.isxander.evergreenhud.compatibility.universal.impl.UKeybindManager
import dev.isxander.evergreenhud.compatibility.universal.impl.CustomKeybind
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.gameevent.TickEvent

class KeybindManagerImpl : UKeybindManager() {

    private val keyBindings = HashMap<CustomKeybind, KeyBinding>()

    init {
        on<TickEvent.ClientTickEvent>()
            .subscribe {
                for ((custom, bind) in keyBindings) {
                    if (bind.isPressed) custom.executor()
                }
            }
    }

    override fun registerKeybind(keybind: CustomKeybind) {
        val mcBind = KeyBinding(
            keybind.name,
            keybind.key.lwjgl2,
            keybind.category
        )
        ClientRegistry.registerKeyBinding(mcBind)

        keyBindings[keybind] = mcBind
    }

}