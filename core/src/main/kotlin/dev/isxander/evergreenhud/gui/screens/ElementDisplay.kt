/*
 *
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
 *
 */

package dev.isxander.evergreenhud.gui.screens

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.api.impl.ElementaScreen
import dev.isxander.evergreenhud.gui.components.ElementComponent
import dev.isxander.evergreenhud.gui.components.SettxiWindow
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.CramSiblingConstraint
import gg.essential.elementa.dsl.*

class ElementDisplay : ElementaScreen() {

    init {
        val container = UIContainer().constrain {
            width = 100.percent()
            height = 100.percent()
        } childOf this

        for (element in EvergreenHUD.elementManager) {
//            ElementComponent(element) childOf this
            SettxiWindow(element).constrain {
                x = CramSiblingConstraint(5f)
                y = CramSiblingConstraint(5f)
                width = 400.pixels()
                height = 100.pixels()
            } childOf container
        }
    }

}
