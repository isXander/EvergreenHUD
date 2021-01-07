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

import java.text.DecimalFormat;

public class ElementCoordinates extends Element {

    @Override
    public void initialise() {

    }

    @Override
    public ElementData getMetadata() {
        return new ElementData("Coordinates", "Shows your current coordinates.");
    }

    @Override
    protected String getValue() {
        if (mc.thePlayer == null) return "unknown";
        DecimalFormat df = new DecimalFormat("#");
        return df.format(mc.thePlayer.posX) + ", " + df.format(mc.thePlayer.posY) + ", " + df.format(mc.thePlayer.posZ);
    }

    @Override
    public String getDisplayPrefix() {
        return "Coordinates";
    }

}
