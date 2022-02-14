/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui.test

import dev.isxander.evergreenhud.utils.*
import dev.isxander.evergreenhud.utils.position.ZonedPosition
import io.ejekta.kambrik.text.textLiteral
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import java.awt.Color

class PositionTest : Screen(textLiteral("Position Zone Test")) {
    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        super.render(matrices, mouseX, mouseY, delta)

        val position = ZonedPosition.rawPositioning(mouseX.toFloat(), mouseY.toFloat())
        val activeZone = position.zone

        for (zone in ZonedPosition.Zone.values()) {
            val color = if (zone == activeZone) {
                Color(0, 0, 0, 100)
            } else {
                Color(255, 255, 255, 100)
            }.rgb

            matrices.fill(zone.x1 * mc.window.scaledWidth, zone.y1 * mc.window.scaledHeight, zone.x2 * mc.window.scaledWidth, zone.y2 * mc.window.scaledHeight, color)
        }

        val color = Color.blue.rgb
        val width = 1f
        matrices.drawHorizontalLine(activeZone.x1 * mc.window.scaledWidth - width / 2f, (activeZone.x2 * mc.window.scaledWidth), position.rawY, width, color)
        matrices.drawVerticalLine(position.rawX, activeZone.y1 * mc.window.scaledHeight - width / 2f, activeZone.y2 * mc.window.scaledHeight, width, color)
    }
}
