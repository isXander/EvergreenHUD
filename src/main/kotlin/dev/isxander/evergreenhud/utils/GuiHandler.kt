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
import net.minecraft.client.gui.screen.Screen

object GuiHandler {
    private var gui: Screen? = null

    fun displayGui(gui: Screen) {
        this.gui = gui
    }

    val clientTickEvent by EvergreenHUD.eventBus.register<ClientTickEvent>({ gui != null }) {
        mc.setScreen(gui)
        gui = null
    }
}
