/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.impl;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.settings.impl.StringSetting;
import com.evergreenclient.hudmod.utils.ElementData;

public class ElementText extends Element {

    public StringSetting text;

    @Override
    public void initialise() {
        addSettings(text = new StringSetting("Text Element", "What the value will display.", "Text Element"));
    }

    @Override
    public ElementData metadata() {
        return new ElementData("Custom Text", "Displays a custom message of your liking!");
    }

    @Override
    protected String getValue() {
        return text.get();
    }

    @Override
    public boolean canShowTitle() {
        return false;
    }

    @Override
    public String getDisplayTitle() {
        return "";
    }

}
