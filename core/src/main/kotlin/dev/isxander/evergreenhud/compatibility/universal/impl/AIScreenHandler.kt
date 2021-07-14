package dev.isxander.evergreenhud.compatibility.universal.impl

import club.chachy.event.keventbus.on
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.event.TickEvent
import gg.essential.elementa.UIComponent

abstract class AIScreenHandler {

    init {
        on<TickEvent>(EvergreenHUD.EVENT_BUS)
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