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

package com.evergreenclient.hudmod.elements.impl;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.elements.ElementType;
import com.evergreenclient.hudmod.settings.impl.BooleanSetting;
import com.evergreenclient.hudmod.elements.ElementData;
import net.minecraft.util.MathHelper;

import java.text.DecimalFormat;

public class ElementPitch extends Element {

    public BooleanSetting trailingZeros;

    @Override
    public void initialise() {
        addSettings(trailingZeros = new BooleanSetting("Trailing Zeros", "Add zeroes to match the accuracy.", true));
    }

    @Override
    public ElementData metadata() {
        return new ElementData("Pitch Display", "Shows the player's rotation pitch. Useful for bridging.");
    }

    @Override
    public ElementType getType() {
        return ElementType.PITCH;
    }

    @Override
    protected String getValue() {
        return new DecimalFormat(trailingZeros.get() ? "0.0" : "#.#").format(MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch));
    }

    @Override
    public String getDisplayTitle() {
        return "Pitch";
    }

}
