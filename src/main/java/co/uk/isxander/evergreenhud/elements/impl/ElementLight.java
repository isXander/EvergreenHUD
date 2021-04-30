package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.settings.impl.ArraySetting;
import co.uk.isxander.xanderlib.utils.MathUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class ElementLight extends Element {

    public ArraySetting displayMode;

    @Override
    public void initialise() {
        addSettings(displayMode = new ArraySetting("Display", "How the value is displayed.", "Absolute", new String[]{"Absolute", "Percentage"}));
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

        int light = playerChunk.getLightSubtracted(playerPos, 0);
        if (displayMode.get().equalsIgnoreCase("percentage"))
            return Math.round(MathUtils.getPercent(light, 0, 15) * 100) + "%";

        return Integer.toString(light);
    }

    @Override
    public String getDisplayTitle() {
        return "Light";
    }

}
