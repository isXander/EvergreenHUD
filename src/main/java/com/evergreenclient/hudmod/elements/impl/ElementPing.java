/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.impl;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.utils.ElementData;
import net.minecraft.client.network.NetworkPlayerInfo;

public class ElementPing extends Element {

    private int ping = 0;

    @Override
    public void initialise() {

    }

    @Override
    public ElementData metadata() {
        return new ElementData("Ping Display", "Shows the delay in ms for your actions to be sent to the server.");
    }

    @Override
    protected String getValue() {
        NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getGameProfile().getId());
        if (info != null && info.getResponseTime() != 1)
            ping = info.getResponseTime();

        return Integer.toString(ping);
    }


    @Override
    public String getDisplayTitle() {
        return "Ping";
    }

}
