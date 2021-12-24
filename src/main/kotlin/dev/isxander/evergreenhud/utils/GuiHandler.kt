/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.utils

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.event.EventListener
import net.minecraft.client.gui.screen.Screen

object GuiHandler : EventListener {
    private var gui: Screen? = null

    init {
        EvergreenHUD.eventBus.register(this)
    }

    fun displayGui(gui: Screen) {
        this.gui = gui
    }

    override fun onClientTick() {
        if (gui != null) {
            mc.setScreen(gui)
            gui = null
        }
    }
}
