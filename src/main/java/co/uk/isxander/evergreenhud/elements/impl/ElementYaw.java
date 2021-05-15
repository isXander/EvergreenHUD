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

package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.settings.impl.BooleanSetting;
import co.uk.isxander.evergreenhud.elements.ElementData;
import net.minecraft.util.MathHelper;

import java.text.DecimalFormat;

public class ElementYaw extends Element {

    public BooleanSetting trailingZeros;

    @Override
    public void initialise() {
        addSettings(trailingZeros = new BooleanSetting("Trailing Zeros", "Display", "Add zeroes to match the accuracy.", false));
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
    public String getDefaultDisplayTitle() {
        return "Yaw";
    }

}
