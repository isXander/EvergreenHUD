package dev.isxander.evergreenhud.compatibility.fabric11701.keybind

import dev.isxander.evergreenhud.compatibility.universal.KEYBIND_MANAGER
import dev.isxander.evergreenhud.compatibility.universal.impl.keybind.AIKeybindManager
import dev.isxander.evergreenhud.compatibility.universal.impl.keybind.CustomKeybind
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil

class KeybindManager {

    private val keyBindings = HashMap<CustomKeybind, KeyBinding>()

    init {
        KEYBIND_MANAGER = object : AIKeybindManager() {
            override fun registerKeybind(keybind: CustomKeybind) = register(keybind)
        }

        ClientTickEvents.END_CLIENT_TICK.register {
            keyBindings.forEach { (custom, bind) ->
                if (bind.wasPressed()) custom.press.invoke()
            }
        }
    }

    fun register(keybind: CustomKeybind) {
        val mcBind = KeyBindingHelper.registerKeyBinding(KeyBinding(
            keybind.name,
            InputUtil.Type.KEYSYM,
            keybind.key.glfw,
            keybind.category
        ))

        keyBindings[keybind] = mcBind
    }

}