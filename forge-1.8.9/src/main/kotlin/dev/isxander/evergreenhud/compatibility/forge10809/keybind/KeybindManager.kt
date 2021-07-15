package dev.isxander.evergreenhud.compatibility.forge10809.keybind

import club.chachy.event.forge.on
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import dev.isxander.evergreenhud.compatibility.universal.impl.keybind.AIKeybindManager
import dev.isxander.evergreenhud.compatibility.universal.impl.keybind.CustomKeybind
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.gameevent.TickEvent

class KeybindManager : AIKeybindManager() {

    private val keyBindings = HashMap<CustomKeybind, KeyBinding>()

    init {
        on<TickEvent.ClientTickEvent>()
            .subscribe {
                keyBindings.forEach { (custom, bind) ->
                    if (bind.isPressed) custom.press.invoke()
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