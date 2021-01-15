/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.impl;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.settings.impl.BooleanSetting;
import com.evergreenclient.hudmod.settings.impl.IntegerSetting;
import com.evergreenclient.hudmod.utils.element.ElementData;

import java.text.DecimalFormat;

public class ElementCoordinates extends Element {

    public BooleanSetting showX;
    public BooleanSetting showY;
    public BooleanSetting showZ;
    public BooleanSetting showCoord;
    public IntegerSetting accuracy;
    public BooleanSetting trailingZeros;

    @Override
    public void initialise() {
        addSettings(showCoord = new BooleanSetting("Show Name", true));
        addSettings(showX = new BooleanSetting("Show X", true));
        addSettings(showY = new BooleanSetting("Show Y", true));
        addSettings(showZ = new BooleanSetting("Show Z", true));
        addSettings(accuracy = new IntegerSetting("Accuracy", 0, 0, 4, " places"));
        addSettings(trailingZeros = new BooleanSetting("Trailing Zeros", false));
    }

    @Override
    public ElementData getMetadata() {
        return new ElementData("Coordinates", "Shows your current coordinates.");
    }

    @Override
    protected String getValue() {
        if (mc.thePlayer == null) return "unknown";
        String formatter = (trailingZeros.get() ? "0" : "#");
        StringBuilder sb = new StringBuilder(accuracy.get() < 1 ? formatter : formatter + ".");
        for (int i = 0; i < accuracy.get(); i++) sb.append(formatter);
        DecimalFormat df = new DecimalFormat(sb.toString());
        String builder = "";
        if (showX.get()) builder += (showCoord.get() ? "X: " : "") + df.format(mc.thePlayer.posX) + (showY.get() || showZ.get() ? ", " : "");
        if (showY.get()) builder += (showCoord.get() ? "Y: " : "") + df.format(mc.thePlayer.posY) + (showZ.get() ? ", " : "");
        if (showZ.get()) builder += (showCoord.get() ? "Z: " : "") + df.format(mc.thePlayer.posZ);
        return builder;
    }

    @Override
    public String getDisplayTitle() {
        return "Coords";
    }

}
