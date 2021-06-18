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

package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.settings.impl.IntegerSetting;
import co.uk.isxander.evergreenhud.utils.ServerPingerUtil;
import co.uk.isxander.evergreenhud.elements.type.SimpleTextElement;
import co.uk.isxander.evergreenhud.settings.impl.EnumSetting;

public class ElementPlayerCount extends SimpleTextElement {

    public EnumSetting<DisplayLevel> level;
    public IntegerSetting updateTime;

    private ServerPingerUtil pinger;

    @Override
    public void initialise() {
        addSettings(level = new EnumSetting<>("Display Level", "Display", "How the element counts the player count.", DisplayLevel.NETWORK));
        addSettings(updateTime = new IntegerSetting("Update Time", "Display", "How frequently will the element update the player count.", 60, 3, 60 ," mins") {
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
        return new ElementData("Player Count", "How many players are currently connected to the server.", "Simple");
    }

    @Override
    protected String getValue() {
        if (mc.theWorld == null)
            return "Unknown";

        String count = "0";

        switch (level.get()) {
            case NETWORK:
                Integer serverCount = pinger.getServerPlayerCount();
                if (serverCount != null) {
                    count = Integer.toString(pinger.getServerPlayerCount());

                    break;
                }
            case WORLD:
                count = Integer.toString(mc.theWorld.playerEntities.size());
                break;
        }

        return count;
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Players";
    }

    private enum DisplayLevel {
        NETWORK,
        WORLD
    }

}
