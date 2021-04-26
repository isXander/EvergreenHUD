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
import co.uk.isxander.evergreenhud.elements.ElementData;
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
