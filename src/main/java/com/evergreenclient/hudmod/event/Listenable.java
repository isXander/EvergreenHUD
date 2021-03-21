/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.event;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public interface Listenable {

    boolean canReceiveEvents();

    default void onClientTick(TickEvent.ClientTickEvent event) {

    }

    default void onRenderTick(TickEvent.RenderTickEvent event) {

    }

    default void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {

    }

    default void onAttackEntity(AttackEntityEvent event) {

    }

    default void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {

    }

    default void onLivingHurt(LivingHurtEvent event) {

    }

}
