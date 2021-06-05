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

import club.sk1er.mods.core.util.MinecraftUtils;
import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.elements.type.SimpleTextElement;
import co.uk.isxander.evergreenhud.settings.impl.StringSetting;
import co.uk.isxander.xanderlib.XanderLib;
import co.uk.isxander.xanderlib.hypixel.locraw.LocationParsed;
import co.uk.isxander.xanderlib.utils.StringUtils;

public class ElementHypixelMode extends SimpleTextElement {

    public StringSetting notHypixelMessage;
    public StringSetting inLobbyMessage;

    @Override
    public void initialise() {
        addSettings(notHypixelMessage = new StringSetting("Not Hypixel Message", "Display", "What message is displayed when you are not connected to Hypixel", "None"));
        addSettings(inLobbyMessage = new StringSetting("In Lobby Message", "Display", "What message is displayed when you are in a lobby.", "None"));
    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Hypixel Mode", "Displays what mode you are currently playing on Hypixel", "Hypixel");
    }

    @Override
    protected String getValue() {
        if (!MinecraftUtils.isHypixel())
            return notHypixelMessage.get();
        LocationParsed location = XanderLib.getInstance().getLocrawManager().getCurrentLocation();
        String friendlyName = location.getMode();
        if (friendlyName == null || location.isLobby())
            return inLobbyMessage.get();

        return StringUtils.capitalizeEnum(friendlyName.replace("_", " "));
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Mode";
    }

}
