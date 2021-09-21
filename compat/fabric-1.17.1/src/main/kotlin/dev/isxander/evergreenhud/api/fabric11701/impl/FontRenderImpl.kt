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

package dev.isxander.evergreenhud.api.fabric11701.impl

import dev.isxander.evergreenhud.api.fabric11701.Main
import dev.isxander.evergreenhud.api.fabric11701.mc
import dev.isxander.evergreenhud.api.impl.UFontRenderer

class FontRenderImpl : UFontRenderer() {
    override fun draw(text: String, x: Float, y: Float, color: Int, shadow: Boolean): UFontRenderer {
        if (shadow) mc.textRenderer.drawWithShadow(Main.matrices, text, x, y, color)
        else mc.textRenderer.draw(Main.matrices, text, x, y, color)

        return this
    }

    override val fontHeight: Int
        get() = mc.textRenderer.fontHeight

    override fun width(text: String): Int = mc.textRenderer.getWidth(text)
}