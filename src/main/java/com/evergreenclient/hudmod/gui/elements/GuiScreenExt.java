/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.gui.elements;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiScreenExt extends GuiScreen {

    public void sliderUpdated(GuiSlider button) {
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

}
