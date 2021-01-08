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

import java.text.SimpleDateFormat;
import java.util.Date;

public class ElementTime extends Element {

    private BooleanSetting twelveHour;

    @Override
    public void initialise() {
        addSettings(twelveHour = new BooleanSetting("Twelve Hour", false));
    }

    @Override
    public ElementData getMetadata() {
        return new ElementData("Time Display", "Displays current system time.");
    }

    @Override
    protected String getValue() {
        return new SimpleDateFormat((twelveHour.get() ? "hh:mm a" : "HH:mm")).format(new Date()).toUpperCase();
    }

    @Override
    public String getDisplayTitle() {
        return "Time";
    }

}
