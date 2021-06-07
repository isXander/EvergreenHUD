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

import net.minecraft.client.gui.GuiScreen;

public class GuiMessageScreen extends GuiScreen {

    private final String[] lines;

    public GuiMessageScreen(String... message) {
        this.lines = message;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int offset = Math.max(85 - lines.length * 10, 10);

        for (String line : lines) {
            this.drawCenteredString(mc.fontRendererObj, line, width / 2, offset, -1);
            offset += mc.fontRendererObj.FONT_HEIGHT + 2;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
