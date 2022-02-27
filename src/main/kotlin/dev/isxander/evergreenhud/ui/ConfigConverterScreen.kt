/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui

import dev.isxander.evergreenhud.config.convert.ConfigConverter
import dev.isxander.evergreenhud.utils.drawString
import dev.isxander.evergreenhud.utils.translate
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText

class ConfigConverterScreen(val converter: ConfigConverter, val parent: Screen?) : Screen(LiteralText("Config Converter")) {
    override fun init() {
        addDrawableChild(ButtonWidget(width / 2 - 102, height / 4 * 3, 100, 20, LiteralText("Convert")) {
            converter.process()
            client!!.setScreen(parent)
        })
        addDrawableChild(ButtonWidget(width / 2 + 2, height / 4 * 3, 100, 20, LiteralText("Skip")) {
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
        drawString(matrices, "${converter.name} has been detected!", width / 2f, height / 4f, -1, centered = true)
        drawString(matrices, "You can convert this into EvergreenHUD if you wish.", width / 2f, height / 4f + textRenderer.fontHeight + 2, -1, centered = true)
        drawString(matrices, "This will not destroy ${converter.name}'s config.", width / 2f, height / 4f + ((textRenderer.fontHeight + 2) * 2), -1, centered = true)
    }
}
