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
import dev.isxander.evergreenhud.gui.components.ElementComponent
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.font.DefaultFonts
import java.awt.Color
import java.io.File

class MainGui : UIComponent() {

    private var clickPos: Pair<Float, Float>? = null

    init {
        val panel = UIRoundedRectangle(5f).constrain {
            x = 20.percent()
            y = 20.percent()
            width = 50.percent()
            height = 50.percent()
            color = Color(18, 18, 18, 200).toConstraint()
        } childOf this

        val titleBar = UIRoundedRectangle(5f).constrain {
            height = 3.percentOfWindow()
            width = 100.percent()
            color = Color(13, 13, 13, 255).toConstraint()
        }.onMouseClick {
            clickPos = if (it.relativeX < 0 || it.relativeY < 0 || it.relativeX > getWidth() || it.relativeY > getHeight()) {
                null
            } else {
                it.relativeX to it.relativeY
            }
        }.onMouseRelease {
            clickPos = null
        }.onMouseDrag { mouseX, mouseY, button ->
            if (clickPos == null)
                return@onMouseDrag

            if (button == 0) {
                panel.constrain {
                    x = (panel.getLeft() + mouseX - clickPos!!.first).pixels()
                    y = (panel.getTop() + mouseY - clickPos!!.second).pixels()
                }
            }
        } childOf panel

        val titleIcon = UIImage.ofFile(File(EvergreenHUD.resourceDir, "evergreenhud-transparent.png")).constrain {
            x = (-8).pixels()
            y = (-8).pixels()
            width = AspectConstraint()
            height = 300.percent()
        } childOf titleBar

        val titleText = UIText("/evergreenhud : bash - Minecraft").constrain {
            x = CenterConstraint()
            y = CenterConstraint() - (100 / 16).percent()
            width = TextAspectConstraint()
            height = 65.percent()
            color = Color(255, 255, 255, 255).toConstraint()
            fontProvider = DefaultFonts.ELEMENTA_MINECRAFT_FONT_RENDERER
        } childOf titleBar

        for (element in EvergreenHUD.elementManager) {
            ElementComponent(element) childOf this
        }
    }

}