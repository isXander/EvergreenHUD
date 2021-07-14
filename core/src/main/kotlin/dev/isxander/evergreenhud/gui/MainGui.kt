package dev.isxander.evergreenhud.gui

import gg.essential.elementa.UIComponent

// We cannot directly call WindowScreen() because it extends from Minecraft's GuiScreen
// which :core does not have access to. Modules will wrap this in a WindowScreen as a
// childOf window. So root components within this class should be a child of this
class MainGui : UIComponent() {
}