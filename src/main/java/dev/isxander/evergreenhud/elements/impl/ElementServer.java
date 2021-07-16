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

import dev.isxander.evergreenhud.elements.ElementData;
import dev.isxander.evergreenhud.elements.type.SimpleTextElement;

public class ElementServer extends SimpleTextElement {

    @Override
    public ElementData metadata() {
        return new ElementData("Server IP", "Shows the current server IP.", "Simple");
    }

    @Override
    protected String getValue() {
        return (mc.getCurrentServerData() == null ? "127.0.0.1" : mc.getCurrentServerData().serverIP);
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "IP";
    }

}
