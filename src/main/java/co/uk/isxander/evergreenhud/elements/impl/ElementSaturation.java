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

package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.elements.type.SimpleTextElement;

public class ElementSaturation extends SimpleTextElement {

    @Override
    protected ElementData metadata() {
        return new ElementData("Saturation", "The current saturation of your player.", "Simple");
    }

    @Override
    protected String getValue() {
        int saturation = 20;
        if (mc.thePlayer != null)
            saturation = (int) mc.thePlayer.getFoodStats().getSaturationLevel();

        return Integer.toString(saturation);
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Saturation";
    }

}

