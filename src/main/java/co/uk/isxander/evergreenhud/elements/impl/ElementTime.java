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
import co.uk.isxander.evergreenhud.elements.type.SimpleTextElement;
import co.uk.isxander.evergreenhud.settings.impl.BooleanSetting;
import co.uk.isxander.evergreenhud.elements.ElementData;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ElementTime extends SimpleTextElement {

    public BooleanSetting twelveHour;
    public BooleanSetting showSeconds;

    @Override
    public void initialise() {
        addSettings(twelveHour = new BooleanSetting("Twelve Hour", "Display", "If the clock will be 12 hour or 24 hour.", false));
        addSettings(showSeconds = new BooleanSetting("Show Seconds", "Display", "If the clock is to show seconds.", false));
    }

    @Override
    public ElementData metadata() {
        return new ElementData("Time Display", "Displays current system time.", "Simple");
    }

    @Override
    protected String getValue() {
        return new SimpleDateFormat(String.format((twelveHour.get() ? "hh:mm%s a" : "HH:mm%s"), (showSeconds.get() ? ":ss" : ""))).format(new Date()).toUpperCase();
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Time";
    }

}
