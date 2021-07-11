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

package co.uk.isxander.evergreenhud.utils;

import club.sk1er.mods.core.util.Multithreading;
import co.uk.isxander.evergreenhud.EvergreenHUD;
import co.uk.isxander.xanderlib.utils.Constants;
import co.uk.isxander.xanderlib.utils.GuiUtils;
import lombok.Getter;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.OldServerPinger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ServerPingerUtil implements Constants {

    public static long SERVER_UPDATE_TIME = 300000;

    private final OldServerPinger serverPinger;
    private final Map<String, Long> serverUpdateTime;
    private final Map<String, Boolean> serverUpdateStatus;

    @Getter private Integer serverPlayerCount;
    @Getter private Integer serverPlayerCap;
    @Getter private Long serverPing;

    public ServerPingerUtil() {
        this.serverPinger = new OldServerPinger();
        this.serverUpdateTime = new HashMap<>();
        this.serverUpdateStatus = new HashMap<>();
        this.serverPlayerCount = null;
        this.serverPlayerCap = null;
        this.serverPing = null;

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        updateManually(mc.getCurrentServerData());
    }

    public void updateManually(ServerData server) {
        if (server != null) {
            Long updateTime = serverUpdateTime.get(server.serverIP);
            if ((updateTime == null || updateTime + SERVER_UPDATE_TIME <= System.currentTimeMillis()) && !serverUpdateStatus.getOrDefault(server.serverIP, false)) {
                EvergreenHUD.LOGGER.info("Pinging " + server.serverIP + " for information...");
                serverUpdateStatus.put(server.serverIP, true);

                Multithreading.runAsync(() -> {
                    try {
                        serverPinger.ping(server);
                        EvergreenHUD.LOGGER.info("Successfully pinged " + server.serverIP);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }

                    serverUpdateStatus.put(server.serverIP, false);
                    serverUpdateTime.put(server.serverIP, System.currentTimeMillis());
                });
            }

            if (serverUpdateStatus.getOrDefault(server.serverIP, false)) serverPlayerCount = null;
            else if (!("".equals(server.populationInfo))) {
                String[] splitPopulationInfo = GuiUtils.stripFormattingCodes(server.populationInfo).split("/");

                serverPlayerCount = Integer.parseInt(splitPopulationInfo[0]);
                serverPlayerCap = Integer.parseInt(splitPopulationInfo[1]);
            }
            serverPing = server.pingToServer;
        } else {
            serverPlayerCount = serverPlayerCap = null;
        }
    }
}
