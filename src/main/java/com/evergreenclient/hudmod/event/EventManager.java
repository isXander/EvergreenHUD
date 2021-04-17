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

package com.evergreenclient.hudmod.event;

import com.evergreenclient.hudmod.elements.impl.ElementCombo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class EventManager {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static EventManager instance;

    public static EventManager getInstance() {
        if (instance == null)
            instance = new EventManager();
        return instance;
    }

    private final List<Listenable> listenables = new ArrayList<>();

    public EventManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void addListener(Listenable listener) {
        this.listenables.add(listener);
    }
    
    public void removeListener(Listenable listener) {
        this.listenables.remove(listener);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        listenables.parallelStream().filter(Listenable::canReceiveEvents).forEach((listenable -> listenable.onClientTick(event)));

        // Registers a packet listener for packet event
        if (mc.theWorld != null) {
            ChannelPipeline pipeline = mc.thePlayer.sendQueue.getNetworkManager().channel().pipeline();
            if (pipeline.get("evergreen_packet_handler") == null && pipeline.get("packet_handler") != null) {
                try {
                    pipeline.addBefore("packet_handler", "evergreen_packet_handler", new EvergreenPacketHandler(this));
                } catch (NoSuchElementException | IllegalArgumentException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        listenables.parallelStream().filter(Listenable::canReceiveEvents).forEach((listenable -> listenable.onRenderTick(event)));
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        listenables.parallelStream().filter(Listenable::canReceiveEvents).forEach((listenable -> listenable.onRenderGameOverlay(event)));
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        listenables.parallelStream().filter(Listenable::canReceiveEvents).forEach((listenable -> listenable.onAttackEntity(event)));
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        listenables.parallelStream().filter(Listenable::canReceiveEvents).forEach((listenable -> listenable.onLivingUpdate(event)));
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        listenables.parallelStream().filter(Listenable::canReceiveEvents).forEach((listenable -> listenable.onLivingHurt(event)));
    }

    public void onPacketReceive(Packet<?> packet) {
        listenables.parallelStream().filter(Listenable::canReceiveEvents).forEach((listenable -> listenable.onPacketReceive(packet)));
    }

    public static class EvergreenPacketHandler extends ChannelInboundHandlerAdapter {

        private final EventManager eventManager;

        public EvergreenPacketHandler(EventManager eventManager) {
            this.eventManager = eventManager;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof Packet) {
                eventManager.onPacketReceive((Packet<?>) msg);

            }
            super.channelRead(ctx, msg);
        }

    }

}
