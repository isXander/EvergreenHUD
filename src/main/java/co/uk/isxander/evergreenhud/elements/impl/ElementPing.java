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
import co.uk.isxander.evergreenhud.elements.RenderOrigin;
import co.uk.isxander.evergreenhud.elements.type.SimpleTextElement;
import co.uk.isxander.evergreenhud.settings.impl.BooleanSetting;
import co.uk.isxander.evergreenhud.settings.impl.IntegerSetting;
import co.uk.isxander.evergreenhud.utils.ServerPingerUtil;
import co.uk.isxander.xanderlib.XanderLib;
import net.minecraft.client.network.NetworkPlayerInfo;

public class ElementPing extends SimpleTextElement {

    public BooleanSetting hideInSingleplayer;
    public BooleanSetting hypixelCompatability;
    public IntegerSetting pingerUpdateTime;

    private int ping = 0;
    private ServerPingerUtil pinger;

    @Override
    public void initialise() {
        addSettings(hideInSingleplayer = new BooleanSetting("Hide In Singleplayer", "Display", "Hide the element in a singleplayer world.", true));
        addSettings(hypixelCompatability = new BooleanSetting("Compatability", "Hypixel", "In hypixel games, your ping displays as 1 at all times. This uses a server list pinger instead of asking normally.", true));
        addSettings(pingerUpdateTime = new IntegerSetting("Update Time", "Hypixel", "How frequently will the element update the player count. (Compatability only)", 60, 1, 60 ," mins") {
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
    public ElementData metadata() {
        return new ElementData("Ping Display", "Shows the delay in ms for your actions to be sent to the server.", "Combat");
    }

    @Override
    protected String getValue() {
        if (mc.thePlayer == null)
            return "Unknown";

        if (hypixelCompatability.get() && MinecraftUtils.isHypixel() && !XanderLib.getInstance().getLocrawManager().getCurrentLocation().isLobby()) {
            int newPing = (int) pinger.getServerPing().longValue();
            if (newPing > 0) ping = newPing;
        } else {
            NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getGameProfile().getId());
            if (info != null)
                ping = info.getResponseTime();
        }

        return Integer.toString(ping);
    }

    @Override
    public void render(float partialTicks, int origin) {
        if (origin != RenderOrigin.GUI && mc.isSingleplayer() && hideInSingleplayer.get()) {
            return;
        }

        super.render(partialTicks, origin);
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Ping";
    }

}
