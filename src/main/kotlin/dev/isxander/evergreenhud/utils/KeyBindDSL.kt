/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.event.ClientTickEvent
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil

fun registerKeyBind(
    key: Int,
    type: InputUtil.Type,
    name: String,
    category: String,
    action: () -> Unit,
): KeyBinding {
    val keyBind = KeyBinding(name, type, key, category)
    KeyBindingHelper.registerKeyBinding(keyBind)
    EvergreenHUD.eventBus.register<ClientTickEvent> {
        if (keyBind.wasPressed()) action()
    }
    return keyBind
}
