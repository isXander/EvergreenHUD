/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.gui.screens

import dev.isxander.evergreenhud.utils.drawStringExt
import gg.essential.api.EssentialAPI
import gg.essential.universal.ChatColor
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen

class BlacklistedScreen(private val parent: GuiScreen?) : GuiScreen() {

    override fun initGui() {
        super.initGui()
        buttonList.add(GuiButton(1, width / 2 - 100, height / 4 * 3, 200, 20, "Quit Game"))
        buttonList.add(GuiButton(2, width / 2 - 100, height / 4 * 3 + 22, 200, 20, "${ChatColor.RED}I understand the risks, continue."))
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)
        when (mouseButton) {
            1 -> mc.shutdown()
            2 -> EssentialAPI.getGuiUtil().openScreen(parent)
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        drawStringExt("This version of EvergreenHUD has been marked as dangerous!", width / 2, height / 4, 0xff4747, centered = true)
        drawStringExt("It is recommended that you quit immediately", width / 2, height / 4 + fontRendererObj.FONT_HEIGHT + 2, -1, centered = true)
        drawStringExt("or download the latest update if there is one.", width / 2, height / 4 + ((fontRendererObj.FONT_HEIGHT + 2) * 2), -1, centered = true)
    }
}
