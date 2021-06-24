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
import co.uk.isxander.evergreenhud.elements.type.SimpleTextElement;
import co.uk.isxander.evergreenhud.settings.impl.BooleanSetting;
import co.uk.isxander.evergreenhud.settings.impl.IntegerSetting;
import co.uk.isxander.evergreenhud.utils.ServerPingerUtil;
import co.uk.isxander.xanderlib.XanderLib;
import net.minecraft.client.network.NetworkPlayerInfo;

public class ElementPing extends SimpleTextElement {

    private int ping = 0;
    public BooleanSetting useServerListPinger;
    public BooleanSetting autoServerListPingerHypixel;
    public IntegerSetting updateTime;
    private ServerPingerUtil pinger;

    @Override
    public void initialise() {
        addSettings(useServerListPinger = new BooleanSetting("Server List Pinger", "Display", "Use the server list pinger (less accurate).", false));
        addSettings(autoServerListPingerHypixel = new BooleanSetting("Server List Pinger Hypixel", "Display", "Use the server list pinger automatically when in a Hypixel game (will provide you a more accurate number).", true));
        addSettings(updateTime = new IntegerSetting("Update Time (Server List)", "Display", "How frequently will the element update the capacity (SERVER LIST PINGER ONLY!).", 60, 3, 60 ," mins") {
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
    public ElementData metadata() {
        return new ElementData("Ping Display", "Shows the delay in ms for your actions to be sent to the server.", "Combat");
    }

    @Override
    public void onAdded() {
        super.onAdded();
        pinger = getUtilitySharer().register(ServerPingerUtil.class, this);
    }

    @Override
    protected String getValue() {
        if (mc.thePlayer == null)
            return "Unknown";
        if (autoServerListPingerHypixel.get() && !XanderLib.getInstance().getLocrawManager().getCurrentLocation().isLobby()) {
            return Integer.toString((pinger.getServerPing() == -1 ? 0 : pinger.getServerPing()));
        }
        if (useServerListPinger.get()) {
            return Integer.toString((pinger.getServerPing() == -1 ? 0 : pinger.getServerPing()));
        }
        NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getGameProfile().getId());
        if (info != null && info.getResponseTime() != 1)
            ping = info.getResponseTime();

        return Integer.toString(ping);
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Ping";
    }

}
