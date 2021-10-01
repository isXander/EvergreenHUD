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

package dev.isxander.evergreenhud.api.impl

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.event.ClientTickEvent
import dev.isxander.evergreenhud.utils.subscribe
import gg.essential.elementa.UIComponent

abstract class UScreenHandler {
    init {
        EvergreenHUD.eventBus.subscribe<ClientTickEvent> {
            if (screen == null) return@subscribe

            displayScreen(screen!!)
            screen = null
        }
    }

    private var screen: ElementaScreen? = null
    fun displayScreenNextTick(screen: ElementaScreen) {
        this.screen = screen
    }

    abstract fun displayScreen(screen: ElementaScreen)
}
