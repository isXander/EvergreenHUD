/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.gui.screens

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.utils.UDesktop
import dev.isxander.evergreenhud.utils.drawString
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
