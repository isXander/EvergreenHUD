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

package dev.isxander.evergreenhud.api.fabric11701.impl

import dev.isxander.evergreenhud.api.impl.UScreenHandler
import dev.isxander.evergreenhud.api.fabric11701.mc
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.dsl.*

class ScreenHandlerImpl : UScreenHandler() {
    override fun displayComponent(component: UIComponent) {
        mc.setScreen(object : WindowScreen() {
            init {
                component.constrain {
                    width = 100.percent()
                    height = 100.percent()
                } childOf window
            }
        })
    }
}