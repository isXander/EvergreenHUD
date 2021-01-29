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
import com.evergreenclient.hudmod.settings.impl.IntegerSetting;
import com.evergreenclient.hudmod.utils.element.ElementData;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

public class ElementSpeed extends Element {

    private double speed = 0;

    public IntegerSetting accuracy;
    public BooleanSetting trailingZeros;
    public BooleanSetting suffix;

    @Override
    public void initialise() {
        addSettings(accuracy = new IntegerSetting("Accuracy", 2, 0, 4, " places"));
        addSettings(trailingZeros = new BooleanSetting("Trailing Zeros", false));
        addSettings(suffix = new BooleanSetting("Suffix", false));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public ElementData metadata() {
        return new ElementData("Speed Display", "Displays the blocks per second speed of the player.");
    }

    @Override
    protected String getValue() {
        String format = (trailingZeros.get() ? "0" : "#");
        StringBuilder sb = new StringBuilder(accuracy.get() < 1 ? format : format + ".");
        for (int i = 0; i < accuracy.get(); i++) sb.append(format);
        return new DecimalFormat(sb.toString()).format(speed) + (suffix.get() ? " m/s" : "");
    }

    @Override
    public String getDisplayTitle() {
        return "Speed";
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;

        double distTraveledLastTickX = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double distTraveledLastTickY = mc.thePlayer.posY - mc.thePlayer.prevPosY;
        double distTraveledLastTickZ = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;

        this.speed = MathHelper.sqrt_double(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickY * distTraveledLastTickY + distTraveledLastTickZ * distTraveledLastTickZ) * 20;
    }

}
