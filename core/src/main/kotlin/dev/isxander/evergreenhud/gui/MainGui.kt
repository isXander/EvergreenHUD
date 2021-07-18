package dev.isxander.evergreenhud.gui

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.gui.components.ElementComponent
import gg.essential.elementa.UIComponent
import gg.essential.elementa.dsl.childOf

// We cannot directly call WindowScreen() because it extends from Minecraft's GuiScreen
// which :core does not have access to. Modules will wrap this in a WindowScreen as a
// childOf window. So root components within this class should be a child of this
class MainGui : UIComponent() {

    init {
        for (element in EvergreenHUD.elementManager) {
            ElementComponent(element) childOf this
        }
    }

}