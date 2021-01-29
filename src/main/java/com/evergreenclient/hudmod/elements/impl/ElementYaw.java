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
import com.evergreenclient.hudmod.utils.element.ElementData;
import net.minecraft.util.MathHelper;

import java.text.DecimalFormat;

public class ElementYaw extends Element {

    public BooleanSetting trailingZeros;

    @Override
    public void initialise() {
        addSettings(trailingZeros = new BooleanSetting("Trailing Zeros", false));
    }

    @Override
    public ElementData metadata() {
        return new ElementData("Yaw Display", "Shows the player's rotation yaw. Useful for bridging.");
    }

    @Override
    protected String getValue() {
        return new DecimalFormat(trailingZeros.get() ? "0.0" : "#.#").format(MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw));
    }

    @Override
    public String getDisplayTitle() {
        return "Yaw";
    }

}
