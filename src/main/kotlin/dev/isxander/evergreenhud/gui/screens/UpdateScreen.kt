/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.gui.screens

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.utils.drawString
import gg.essential.universal.UDesktop
import io.ejekta.kambrik.text.textLiteral
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.util.math.MatrixStack
import java.net.URI

class UpdateScreen(private val latest: String, private val parent: Screen?) : Screen(textLiteral("EvergreenHUD Update")) {
    override fun init() {
        addDrawableChild(ButtonWidget(width / 2 - 102, height / 4 * 3, 100, 20, textLiteral("Download")) {
            UDesktop.browse(URI.create("https://www.isxander.dev/mods/evergreenhud"))
            client!!.setScreen(parent)
        })
        addDrawableChild(ButtonWidget(width / 2 + 2, height / 4 * 3, 100, 20, textLiteral("Skip")) {
            client!!.setScreen(parent)
        })
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        super.render(matrices, mouseX, mouseY, delta)
        drawString(matrices, "A new version is available for EvergreenHUD!", width / 2f, height / 4f, -1, centered = true)
        drawString(matrices, "The latest version is $latest.", width / 2f, height / 4f + textRenderer.fontHeight + 2, -1, centered = true)
        drawString(matrices, "The current version is ${EvergreenHUD.VERSION_STR}.", width / 2f, height / 4f + ((textRenderer.fontHeight + 2) * 2), -1, centered = true)
    }
}
