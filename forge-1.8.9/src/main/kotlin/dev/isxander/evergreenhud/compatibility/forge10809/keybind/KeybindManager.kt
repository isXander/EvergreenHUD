package dev.isxander.evergreenhud.compatibility.forge10809.keybind

import club.chachy.event.keventbus.on
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.universal.KEYBIND_MANAGER
import dev.isxander.evergreenhud.compatibility.universal.impl.keybind.AIKeybindManager
import dev.isxander.evergreenhud.compatibility.universal.impl.keybind.CustomKeybind
import dev.isxander.evergreenhud.event.TickEvent
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry

class KeybindManager {

    private val keyBindings = HashMap<CustomKeybind, KeyBinding>()

    init {
        KEYBIND_MANAGER = object : AIKeybindManager() {
            override fun registerKeybind(keybind: CustomKeybind) = register(keybind)
        }

        on<TickEvent>(EvergreenHUD.EVENT_BUS)
            .subscribe {
                keyBindings.forEach { (custom, bind) ->
                    if (bind.isPressed) custom.press.invoke()
                }
            }
    }

    fun register(keybind: CustomKeybind) {
        val mcBind = KeyBinding(
            keybind.name,
            keybind.key.lwjgl2,
            keybind.category
        )
        ClientRegistry.registerKeyBinding(mcBind)

        keyBindings[keybind] = mcBind
    }

}