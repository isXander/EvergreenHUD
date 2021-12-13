/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.gui.screens

import dev.isxander.evergreenhud.utils.*
import io.ejekta.kambrik.text.textLiteral
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import java.awt.Color

class PositionTest : Screen(textLiteral("Position Test")), Drawable {
    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(matrices, mouseX, mouseY, delta)

        val position = Position2D.rawPositioning(mouseX.toFloat(), mouseY.toFloat())

        for (origin in Position2D.Origin.values()) {
            val color = if (origin == position.origin) {
                Color.white
            } else {
                Color.black
            }.rgb

            val originX = (origin.x * mc.window.scaledWidth).coerceIn(20f .. mc.window.scaledWidth - 20f)
            val originY = (origin.y * mc.window.scaledHeight).coerceIn(20f .. mc.window.scaledHeight - 20f)

            matrices.fill(originX - 20f, originY - 20f, originX + 20f, originY + 20f, color)
        }

        matrices.fill(position.rawX - 8, position.rawY - 8, position.rawX + 8, position.rawY + 8, Color.green.rgb)
        matrices.fill(mouseX - 4f, mouseY - 4f, mouseX + 4f, mouseY + 4f, Color.red.rgb)
        matrices.fill(mc.mouse.scaledX.toFloat() - 2f, mc.mouse.scaledY.toFloat() - 2f, mc.mouse.scaledX.toFloat() + 2f, mc.mouse.scaledY.toFloat() + 2f, Color.blue.rgb)
    }
}
