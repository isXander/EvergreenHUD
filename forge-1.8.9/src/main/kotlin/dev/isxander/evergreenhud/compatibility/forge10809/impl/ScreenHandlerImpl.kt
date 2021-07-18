package dev.isxander.evergreenhud.compatibility.forge10809.impl

import dev.isxander.evergreenhud.compatibility.forge10809.mc
import dev.isxander.evergreenhud.compatibility.universal.impl.AIScreenHandler
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.dsl.childOf

class ScreenHandlerImpl : AIScreenHandler() {
    override fun displayComponent(component: UIComponent) {
        mc.displayGuiScreen(object : WindowScreen() {
            init { component childOf window }
        })
    }
}