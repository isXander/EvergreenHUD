/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.utils.drawStringExt
import gg.essential.api.EssentialAPI
import gg.essential.universal.UDesktop
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import java.net.URI

class UpdateScreen(private val latest: String, private val parent: GuiScreen?) : GuiScreen() {
    override fun initGui() {
        super.initGui()
        buttonList.add(GuiButton(1, width / 2 - 102, height / 4 * 3, 100, 20, "Download"))
        buttonList.add(GuiButton(2, width / 2 + 2, height / 4 * 3, 100, 20, "Skip"))
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)
        when (mouseButton) {
            1 -> {
                UDesktop.browse(URI.create("https://www.isxander.dev/mods/evergreenhud"))
                EssentialAPI.getGuiUtil().openScreen(parent)
            }
            2 -> {
                EssentialAPI.getGuiUtil().openScreen(parent)
            }
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        drawStringExt("A new version is available for EvergreenHUD!", width / 2f, height / 4f, -1, centered = true)
        drawStringExt("The latest version is $latest.", width / 2f, height / 4f + fontRendererObj.FONT_HEIGHT + 2, -1, centered = true)
        drawStringExt("The current version is ${EvergreenHUD.VERSION_STR}.", width / 2f, height / 4f + ((fontRendererObj.FONT_HEIGHT + 2) * 2), -1, centered = true)
    }
}
