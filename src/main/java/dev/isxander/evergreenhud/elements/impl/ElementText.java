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
import dev.isxander.evergreenhud.settings.impl.StringSetting;
import dev.isxander.evergreenhud.elements.ElementData;

public class ElementText extends SimpleTextElement {

    public StringSetting text;

    @Override
    public void initialise() {
        addSettings(text = new StringSetting("Text Element", "Display", "What the value will display.", "Text Element"));
    }

    @Override
    public ElementData metadata() {
        return new ElementData("Custom Text", "Displays a custom message of your liking!", "Advanced");
    }

    @Override
    protected String getValue() {
        return convertColorCodes(text.get());
    }

    private String convertColorCodes(String text) {
        return text.replaceAll("&([a-fA-F0-9])", "\u00A7$1");
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "";
    }

}
