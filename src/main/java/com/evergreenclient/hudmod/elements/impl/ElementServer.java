/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.impl;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.utils.element.ElementData;

public class ElementServer extends Element {

    @Override
    public void initialise() {

    }

    @Override
    public ElementData getMetadata() {
        return new ElementData("Server IP", "Shows the current server IP.");
    }

    @Override
    protected String getValue() {
        return (mc.getCurrentServerData() == null ? "Singleplayer" : mc.getCurrentServerData().serverIP);
    }

    @Override
    public String getDisplayTitle() {
        return "IP";
    }

}
