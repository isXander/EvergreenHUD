/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/gpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.compatibility.universal.impl

import club.chachy.event.keventbus.on
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.event.ClientTickEvent
import gg.essential.elementa.UIComponent

abstract class AIScreenHandler {

    init {
        on<ClientTickEvent>(EvergreenHUD.EVENT_BUS)
            .filter { component != null }
            .subscribe {
                displayComponent(component!!)
                component = null
            }
    }

    private var component: UIComponent? = null
    fun displayComponentNextTick(component: UIComponent) {
        this.component = component
    }

    abstract fun displayComponent(component: UIComponent)

}