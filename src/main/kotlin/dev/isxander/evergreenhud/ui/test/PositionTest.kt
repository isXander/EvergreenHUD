/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui.test

import dev.isxander.evergreenhud.utils.drawHorizontalLine
import dev.isxander.evergreenhud.utils.drawVerticalLine
import dev.isxander.evergreenhud.utils.position.ZonedPosition
import gg.essential.universal.UResolution
import net.minecraft.client.gui.GuiScreen
import java.awt.Color

class PositionTest : GuiScreen() {
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)

        val position = ZonedPosition.rawPositioning(mouseX.toFloat(), mouseY.toFloat())
        val activeZone = position.zone

        for (zone in ZonedPosition.Zone.values()) {
            val color = if (zone == activeZone) {
                Color(0, 0, 0, 100)
            } else {
                Color(255, 255, 255, 100)
            }.rgb

            drawRect(
                (zone.x1 * UResolution.scaledWidth).toInt(), (zone.y1 * UResolution.scaledHeight).toInt(),
                (zone.x2 * UResolution.scaledWidth).toInt(), (zone.y2 * UResolution.scaledHeight).toInt(), color)
        }

        val color = Color.blue.rgb
        val width = 1f
        drawHorizontalLine(activeZone.x1 * UResolution.scaledWidth - width / 2f, (activeZone.x2 * UResolution.scaledWidth), position.rawY, width, color)
        drawVerticalLine(position.rawX, activeZone.y1 * UResolution.scaledHeight - width / 2f, activeZone.y2 * UResolution.scaledHeight, width, color)
    }
}
