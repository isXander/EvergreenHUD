/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.gui.screens

import dev.isxander.evergreenhud.utils.drawString
import dev.isxander.evergreenhud.utils.mc
import io.ejekta.kambrik.text.textLiteral
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Formatting

class BlacklistedScreen(private val parent: Screen?) : Screen(textLiteral("EvergreenHUD Dangerous Version")) {
    override fun init() {
        addDrawableChild(ButtonWidget(width / 2 - 100, height / 4 * 3, 200, 20, textLiteral("Quit Game")) {
            mc.scheduleStop()
        })
        addDrawableChild(ButtonWidget(width / 2 - 100, height / 4 * 3 + 22, 200, 20, textLiteral("I understand the risks, continue.") { color(Formatting.RED.colorValue!!) }) {
            client!!.setScreen(parent)
        })
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        super.render(matrices, mouseX, mouseY, delta)
        drawString(matrices, "This version of EvergreenHUD has been marked as dangerous!", width / 2f, height / 4f, 0xff4747, centered = true)
        drawString(matrices, "It is recommended that you quit immediately", width / 2f, height / 4f + textRenderer.fontHeight + 2, -1, centered = true)
        drawString(matrices, "or download the latest update if there is one.", width / 2f, height / 4f + ((textRenderer.fontHeight + 2) * 2), -1, centered = true)
    }
}
