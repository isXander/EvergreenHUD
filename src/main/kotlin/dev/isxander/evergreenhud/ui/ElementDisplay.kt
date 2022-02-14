/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.ui.components.ElementComponent
import dev.isxander.evergreenhud.utils.mc
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.provideDelegate
import net.minecraft.client.gui.screen.Screen

class ElementDisplay(val parentScreen: Screen? = null) : WindowScreen(ElementaVersion.V1) {
    val inspector by Inspector(window) childOf window

    init {
        for (element in EvergreenHUD.elementManager) {
            val component by ElementComponent(element)

            component.settingsButton.onMouseClick {
                val configUI by ConfigUI(component.element)
                configUI childOf window
            }

            component childOf window
        }
    }

    override fun onScreenClose() {
        EvergreenHUD.elementManager.elementConfig.save()

        super.onScreenClose()
    }

    override fun onClose() {
        mc.setScreen(parentScreen)
    }
}
