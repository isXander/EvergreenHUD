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

package dev.isxander.evergreenhud.gui

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.api.impl.ElementaScreen
import dev.isxander.evergreenhud.gui.components.ElementComponent
import gg.essential.elementa.dsl.*

class MainGui : ElementaScreen() {

    init {
        for (element in EvergreenHUD.elementManager) {
            ElementComponent(element) childOf this
        }
    }

}
