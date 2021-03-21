/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.event;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

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

}
