/*
 * Copyright (C) isXander [2019 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package co.uk.isxander.evergreenhud.gui.screens.impl;

import co.uk.isxander.evergreenhud.gui.screens.GuiScreenExt;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiCredits extends GuiScreenExt {

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButtonExt(0, width / 2 - 90 - 1, height - 20, 182, 20, "Back"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            mc.displayGuiScreen(new GuiMain());
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        mc.displayGuiScreen(new GuiMain());
    }
}
