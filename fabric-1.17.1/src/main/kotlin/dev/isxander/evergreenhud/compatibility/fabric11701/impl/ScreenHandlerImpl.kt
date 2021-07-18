package dev.isxander.evergreenhud.compatibility.fabric11701.impl

import dev.isxander.evergreenhud.compatibility.fabric11701.mc
import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import dev.isxander.evergreenhud.compatibility.universal.impl.AIScreenHandler
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.dsl.childOf

class ScreenHandlerImpl : AIScreenHandler() {
    override fun displayComponent(component: UIComponent) {
        LOGGER.info("Wrapping Elementa Component")
        mc.setScreen(object : WindowScreen() {
            init { component childOf window }
        })
    }
}