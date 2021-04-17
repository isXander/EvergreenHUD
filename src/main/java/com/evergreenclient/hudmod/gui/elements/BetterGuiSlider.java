/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/Evergreen-Client/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package com.evergreenclient.hudmod.gui.elements;

import com.evergreenclient.hudmod.gui.screens.GuiScreenExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.Collections;

public class BetterGuiSlider extends GuiSlider {

    public String description;

    private final GuiScreenExt screen;

    public BetterGuiSlider(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, GuiScreenExt screen, String description) {
        super(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr);
        this.screen = screen;
        this.description = description;
    }

    public BetterGuiSlider(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, ISlider par, GuiScreenExt screen, String description) {
        super(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, par);
        this.screen = screen;
        this.description = description;
    }

    public BetterGuiSlider(int id, int xPos, int yPos, String displayStr, double minVal, double maxVal, double currentVal, ISlider par, GuiScreenExt screen, String description) {
        super(id, xPos, yPos, displayStr, minVal, maxVal, currentVal, par);
        this.screen = screen;
        this.description = description;
    }

    @Override
    public void updateSlider() {
        super.updateSlider();
        this.screen.sliderUpdated(this);
    }

    public void drawButtonDescription(Minecraft mc, int mouseX, int mouseY) {
        if (enabled && !description.trim().equals("") && mouseX >= xPosition && mouseX <= xPosition + width && mouseY >= yPosition && mouseY <= yPosition + height) {
            GuiUtils.drawHoveringText(Collections.singletonList(description), mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, mc.fontRendererObj);
            GlStateManager.disableLighting();
        }
    }
}
