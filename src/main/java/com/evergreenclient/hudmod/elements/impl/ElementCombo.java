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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

// FIXME: 14/01/2021 fix combo counter idk how to do it
public class ElementCombo extends Element {

    private int counter = 0;
    private Entity hitEntity = null;
    private long lastHit = 0L;

    @Override
    public void initialise() {

    }

    @Override
    public ElementData metadata() {
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

    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (System.currentTimeMillis() - lastHit > 2000L) {
            hitEntity = null;
            counter = 0;
            lastHit = 0;
        }
    }

    public void onAttackEntity(AttackEntityEvent event) {
        if (event.target.hurtResistantTime > 0) return;

        if (event.entity instanceof EntityPlayerSP) {
            if (hitEntity == null) {
                if (event.target instanceof EntityLivingBase) {
                    hitEntity = event.target;
                    counter++;
                    lastHit = System.currentTimeMillis();
                }
            } else {
                if (hitEntity == event.target) {
                    counter++;
                    lastHit = System.currentTimeMillis();
                } else if (event.target instanceof EntityLivingBase) {
                    hitEntity = event.target;
                    counter = 1;
                    lastHit = System.currentTimeMillis();
                }
            }
        } else if (event.target instanceof EntityPlayerSP) {
            System.out.println("Damage");
            counter = 0;
            hitEntity = null;
        }

    }

    public void onLivingHurt(LivingHurtEvent event) {
        if (event.entity instanceof EntityPlayerSP) {

        }
    }

}
