/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.impl;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.utils.element.ElementData;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ElementCombo extends Element {

    private int counter = 0;
    private Entity hitEntity = null;
    private long lastHit = 0L;

    @Override
    public void initialise() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public ElementData getMetadata() {
        return new ElementData("Combo Counter", "Shows the amount of hits you get before you are attacked.");
    }

    @Override
    protected String getValue() {
        return Integer.toString(counter);
    }

    @Override
    public String getDisplayTitle() {
        return "Combo";
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (System.currentTimeMillis() - lastHit > 2000L) {
            counter = 0;
            lastHit = 0;
        }
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (event.entity instanceof EntityPlayerSP && !(event.target instanceof EntityPlayerSP)) {
            if (hitEntity == null || event.target == hitEntity) {
                counter++;
            } else {
                counter = 1;
            }
            lastHit = System.currentTimeMillis();
            hitEntity = event.target;
        }
        else if (!(event.entity instanceof EntityPlayerSP) && event.target instanceof EntityPlayerSP) {
            counter = 0;
            hitEntity = null;
        }
    }

}
