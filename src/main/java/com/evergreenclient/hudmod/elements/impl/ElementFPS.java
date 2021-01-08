/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.impl;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.utils.element.ElementData;
import net.minecraft.client.Minecraft;

public class ElementFPS extends Element {

    @Override
    public void initialise() {

    }

    @Override
    public ElementData getMetadata() {
        return new ElementData("FPS Display", "Shows your current FPS");
    }

    @Override
    protected String getValue() {
        return Integer.toString(Minecraft.getDebugFPS());
    }

    @Override
    public String getDisplayTitle() {
        return "FPS";
    }

}
