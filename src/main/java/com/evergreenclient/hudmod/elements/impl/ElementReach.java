/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.impl;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.settings.impl.BooleanSetting;
import com.evergreenclient.hudmod.utils.element.ElementData;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.text.DecimalFormat;

public class ElementReach extends Element {

    public BooleanSetting trailingZeros;

    private Double reach = 0D;
    private long lastHit = 0L;

    @Override
    public void initialise() {
        addSettings(trailingZeros = new BooleanSetting("Trailing Zeros", false));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public ElementData getMetadata() {
        return new ElementData("Reach Display", "Shows how far away you are from your enemy.");
    }

    @Override
    protected String getValue() {
        DecimalFormat df = new DecimalFormat(trailingZeros.get() ? "#.#" : "0.0");
        return df.format(reach);
    }

    @Override
    public String getDisplayTitle() {
        return "Reach";
    }

    @SubscribeEvent
    public void attackEntity(AttackEntityEvent event) {
        if (event.entity instanceof EntityPlayerSP) {
            reach = getReachDistanceFromEntity(event.target);
            lastHit = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (System.currentTimeMillis() - lastHit > 3000) reach = 0D;
    }

    private double getReachDistanceFromEntity(Entity entity) {
        mc.mcProfiler.startSection("Calculate Reach Dist");

        // How far will ray travel before ending
        double maxSize = 6D;
        // Bounding box of entity
        AxisAlignedBB otherBB = entity.getEntityBoundingBox();
        // This is where people found out that F3+B is not accurate for hitboxes,
        // it makes hitboxes bigger by certain amount
        float collisionBorderSize = entity.getCollisionBorderSize();
        AxisAlignedBB otherHitbox = otherBB.expand(collisionBorderSize, collisionBorderSize, collisionBorderSize);
        // Not quite sure what the difference is between these two vectors
        // In actual code where this is taken from, partialTicks is always 1.0
        // So this won't decrease accuracy
        Vec3 eyePos = mc.thePlayer.getPositionEyes(1.0F);
        Vec3 lookPos = mc.thePlayer.getLook(1.0F);
        // Get vector for raycast
        Vec3 adjustedPos = eyePos.addVector(lookPos.xCoord * maxSize, lookPos.yCoord * maxSize, lookPos.zCoord * maxSize);
        MovingObjectPosition movingObjectPosition = otherHitbox.calculateIntercept(eyePos, adjustedPos);
        Vec3 otherEntityVec;
        // This will trigger if hit distance is more than maxSize
        if (movingObjectPosition == null)
            return -1;
        otherEntityVec = movingObjectPosition.hitVec;
        // finally calculate distance between both vectors
        double dist = eyePos.distanceTo(otherEntityVec);

        mc.mcProfiler.endSection();
        return dist;
    }

}
