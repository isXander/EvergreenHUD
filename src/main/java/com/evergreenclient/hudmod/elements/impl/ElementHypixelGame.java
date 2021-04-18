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

import co.uk.isxander.xanderlib.XanderLib;
import co.uk.isxander.xanderlib.hypixel.locraw.GameType;
import co.uk.isxander.xanderlib.hypixel.locraw.LocationParsed;
import co.uk.isxander.xanderlib.utils.MinecraftUtils;
import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.settings.impl.StringSetting;
import com.evergreenclient.hudmod.elements.ElementData;

public class ElementHypixelGame extends Element {

    public StringSetting notHypixelMessage;

    @Override
    public void initialise() {
        addSettings(notHypixelMessage = new StringSetting("Not Hypixel Message", "What message is displayed when you are not connected to Hypixel", "None"));
    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Hypixel Game", "Displays what game you are currently playing on Hypixel");
    }

    @Override
    protected String getValue() {
        if (!MinecraftUtils.isHypixel())
            return notHypixelMessage.get();
        LocationParsed location = XanderLib.getInstance().getLocrawManager().getCurrentLocation();
        String friendlyName = location.getGameType().friendlyName();
        if (location.getGameType() != GameType.LIMBO && location.isLobby()) {
            friendlyName += " Lobby";
        }
        return friendlyName;
    }

    @Override
    public String getDisplayTitle() {
        return "Game";
    }

}
