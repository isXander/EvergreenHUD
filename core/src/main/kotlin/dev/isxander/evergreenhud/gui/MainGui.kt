/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

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