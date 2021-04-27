package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.elements.ElementData;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class ElementLight extends Element {

    @Override
    public void initialise() {

    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Light Level", "Get the current light level of where you are standing.");
    }

    @Override
    protected String getValue() {
        if (mc.thePlayer == null)
            return "Unknown";

        BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        Chunk playerChunk = mc.theWorld.getChunkFromBlockCoords(playerPos);

        return Integer.toString(playerChunk.getLightSubtracted(playerPos, 0));
    }

    @Override
    public String getDisplayTitle() {
        return "Light";
    }

}
