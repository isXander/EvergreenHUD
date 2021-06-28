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

import club.sk1er.mods.core.util.MinecraftUtils;
import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.elements.type.SimpleTextElement;
import co.uk.isxander.evergreenhud.settings.impl.BooleanSetting;
import co.uk.isxander.evergreenhud.settings.impl.IntegerSetting;
import co.uk.isxander.evergreenhud.utils.ServerPingerUtil;
import co.uk.isxander.xanderlib.XanderLib;
import net.minecraft.client.network.NetworkPlayerInfo;

public class ElementPing extends SimpleTextElement {

    private int ping = 0;
    public BooleanSetting autoServerListPingerHypixel;
    public BooleanSetting hideInSingleplayer;
    public IntegerSetting updateTime;
    private ServerPingerUtil pinger;

    @Override
    public void initialise() {
        addSettings(autoServerListPingerHypixel = new BooleanSetting("Detect Hypixel", "Display", "Use a different method of finding ping to provide compatability in Hypixel.", true));
        addSettings(updateTime = new IntegerSetting("Update Time (Server List)", "Display", "How frequently will the element update the capacity (HYPIXEL PINGER ONLY).", 60, 3, 60 ," mins") {
            @Override
            protected boolean onChange(int currentVal, int newVal) {
                boolean success = super.onChange(currentVal, newVal);

                if (success)
                    ServerPingerUtil.SERVER_UPDATE_TIME = Math.min(newVal * 60000L, ServerPingerUtil.SERVER_UPDATE_TIME);

                return success;
            }
        });
        addSettings(hideInSingleplayer = new BooleanSetting("Hide in Singleplayer", "Display", "Hide this element when in Singleplayer.", false));
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
        if (autoServerListPingerHypixel.get() && !XanderLib.getInstance().getLocrawManager().getCurrentLocation().isLobby() && MinecraftUtils.isHypixel()) {
            return Integer.toString((pinger.getServerPing() == -1 ? 0 : pinger.getServerPing()));
        }
        NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getGameProfile().getId());
        if (info != null && info.getResponseTime() != 1) {
            ping = info.getResponseTime();
        }
        return Integer.toString(ping);
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Ping";
    }

    @Override
    public void render(float partialTicks, int origin) {
        if (origin == 0) {
            if (hideInSingleplayer.get() && mc.isSingleplayer()) {
                return;
            }
        }

        super.render(partialTicks, origin);
    }
}
