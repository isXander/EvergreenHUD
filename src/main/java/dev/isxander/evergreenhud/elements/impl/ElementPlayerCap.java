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
import dev.isxander.evergreenhud.settings.impl.IntegerSetting;
import dev.isxander.evergreenhud.settings.impl.StringSetting;
import dev.isxander.evergreenhud.utils.ServerPingerUtil;
import dev.isxander.evergreenhud.elements.type.SimpleTextElement;

public class ElementPlayerCap extends SimpleTextElement {

    public StringSetting singlePlayerMsg;
    public IntegerSetting updateTime;

    private ServerPingerUtil pinger;

    @Override
    public void initialise() {
        addSettings(singlePlayerMsg = new StringSetting("Singleplayer Message", "Display", "What message is displayed when you are connected to a singleplayer world.", "1"));
        addSettings(updateTime = new IntegerSetting("Update Time", "Display", "How frequently will the element update the capacity.", 60, 1, 60 ," mins") {
            @Override
            protected boolean onChange(int currentVal, int newVal) {
                boolean success = super.onChange(currentVal, newVal);

                if (success)
                    ServerPingerUtil.SERVER_UPDATE_TIME = Math.min(newVal * 60000L, ServerPingerUtil.SERVER_UPDATE_TIME);

                return success;
            }
        });
    }

    @Override
    public void onAdded() {
        super.onAdded();

        pinger = getUtilitySharer().register(ServerPingerUtil.class, this);
    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Player Capacity", "The player capacity of the current server.", "Simple");
    }

    @Override
    protected String getValue() {
        Integer cap = pinger.getServerPlayerCap();
        return cap == null ? singlePlayerMsg.get() : Integer.toString(cap);
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Cap";
    }

}
