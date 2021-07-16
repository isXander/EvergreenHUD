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

package dev.isxander.evergreenhud.elements.impl;

import dev.isxander.evergreenhud.elements.type.SimpleTextElement;
import dev.isxander.evergreenhud.settings.impl.BooleanSetting;
import dev.isxander.evergreenhud.elements.ElementData;
import net.minecraft.util.MathHelper;

import java.text.DecimalFormat;

public class ElementPitch extends SimpleTextElement {

    public BooleanSetting trailingZeros;

    @Override
    public void initialise() {
        addSettings(trailingZeros = new BooleanSetting("Trailing Zeros", "Display", "Add zeroes to match the accuracy.", true));
    }

    @Override
    public ElementData metadata() {
        return new ElementData("Pitch Display", "Shows the player's rotation pitch. Useful for bridging.", "Simple");
    }

    @Override
    protected String getValue() {
        if (mc.thePlayer == null)
            return "Unknown";

        return new DecimalFormat(trailingZeros.get() ? "0.0" : "#.#").format(MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch));
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Pitch";
    }

}
