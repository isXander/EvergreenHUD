/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui

import dev.isxander.evergreenhud.utils.drawString
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.evergreenhud.utils.translate
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.util.Formatting

class BlacklistedScreen(private val parent: Screen?) : Screen(LiteralText("EvergreenHUD Dangerous Version")) {
    override fun init() {
        addDrawableChild(ButtonWidget(width / 2 - 100, height / 4 * 3, 200, 20, LiteralText("Quit Game")) {
            mc.scheduleStop()
        })
        addDrawableChild(ButtonWidget(width / 2 - 100, height / 4 * 3 + 22, 200, 20, LiteralText("I understand the risks, continue.").formatted(Formatting.RED)) {
            client!!.setScreen(parent)
        })
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        super.render(matrices, mouseX, mouseY, delta)
        matrices.push()
        matrices.translate(width / 2f, height / 8f)
        matrices.scale(2f, 2f, 1f)
        drawString(matrices, "EvergreenHUD", 0f, 0f, EvergreenPalette.Evergreen.Evergreen3.rgba, centered = true)
        matrices.pop()
        drawString(matrices, "This version of EvergreenHUD has been marked as dangerous!", width / 2f, height / 4f, 0xff4747, centered = true)
        drawString(matrices, "It is recommended that you quit immediately", width / 2f, height / 4f + textRenderer.fontHeight + 2, -1, centered = true)
        drawString(matrices, "or download the latest update if there is one.", width / 2f, height / 4f + ((textRenderer.fontHeight + 2) * 2), -1, centered = true)
    }
}
