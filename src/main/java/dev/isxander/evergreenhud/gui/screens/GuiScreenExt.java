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

package dev.isxander.evergreenhud.gui.screens;

import dev.isxander.evergreenhud.gui.components.BetterGuiButton;
import dev.isxander.evergreenhud.gui.components.BetterGuiSlider;
import lombok.Getter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenExt extends GuiScreen {

    @Getter private final GuiScreen parentScreen;

    public GuiScreenExt(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    protected int getRow(int row) {
        return 40 + (row * 22);
    }

    protected int left() {
        return width / 2 - 1 - 120;
    }

    protected int right() {
        return width / 2 + 1;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (GuiButton button : buttonList) {
            if (button instanceof BetterGuiButton) {
                ((BetterGuiButton)button).drawButtonDescription(mc, mouseX, mouseY);
            } else if (button instanceof BetterGuiSlider) {
                ((BetterGuiSlider)button).drawButtonDescription(mc, mouseX, mouseY);
            }
        }
    }

}
