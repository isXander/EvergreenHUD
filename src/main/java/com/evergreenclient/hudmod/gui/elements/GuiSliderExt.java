/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.gui.elements;

import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiSliderExt extends GuiSlider {

    private final GuiScreenExt screen;

    public GuiSliderExt(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, GuiScreenExt screen) {
        super(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr);
        this.screen = screen;
    }

    public GuiSliderExt(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, ISlider par, GuiScreenExt screen) {
        super(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, par);
        this.screen = screen;
    }

    public GuiSliderExt(int id, int xPos, int yPos, String displayStr, double minVal, double maxVal, double currentVal, ISlider par, GuiScreenExt screen) {
        super(id, xPos, yPos, displayStr, minVal, maxVal, currentVal, par);
        this.screen = screen;
    }

    @Override
    public void updateSlider() {
        super.updateSlider();
        this.screen.sliderUpdated(this);
    }
}
