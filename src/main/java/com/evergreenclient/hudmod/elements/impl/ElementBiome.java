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
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class ElementBiome extends Element {

    @Override
    public void initialise() {

    }

    @Override
    public ElementData metadata() {
        return new ElementData("Biome Display", "Displays the current biome you are standing in.");
    }

    @Override
    protected String getValue() {
        String text = null;

        if (mc.theWorld != null && mc.thePlayer != null) {
            BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
            Chunk playerChunk = mc.theWorld.getChunkFromBlockCoords(playerPos);
            text = playerChunk.getBiome(playerPos, mc.theWorld.getWorldChunkManager()).biomeName;
        }

        if (text == null) text = "Unknown";

        return text;
    }

    @Override
    public String getDisplayTitle() {
        return "Biome";
    }

}
