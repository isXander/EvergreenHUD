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
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.dsl.*
import java.awt.Color

// We cannot directly call WindowScreen() because it extends from Minecraft's GuiScreen
// which :core does not have access to. Modules will wrap this in a WindowScreen as a
// childOf window. So root components within this class should be a child of this
class MainGui : UIComponent() {

    init {
        val panel = UIRoundedRectangle(5f).constrain {
            x = 20.percent()
            y = 20.percent()
            width = 50.percent()
            height = 50.percent()
            color = Color(18, 18, 18, 240).toConstraint()
        } childOf this

        val titleBar = UIRoundedRectangle(5f).constrain {
            height = 3.percentOfWindow()
            width = 100.percent()
            color = Color(13, 13, 13, 255).toConstraint()
        } childOf panel

        for (element in EvergreenHUD.elementManager) {
            ElementComponent(element) childOf this
        }
    }

}