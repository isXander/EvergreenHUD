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
import com.evergreenclient.hudmod.utils.ElementData;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ElementTime extends Element {

    private BooleanSetting twelveHour;

    @Override
    public void initialise() {
        addSettings(twelveHour = new BooleanSetting("Twelve Hour", "If the clock will be 12 hour or 24 hour.", false));
    }

    @Override
    public ElementData metadata() {
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
