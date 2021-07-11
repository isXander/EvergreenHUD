/*
 * Copyright (C) isXander [2019 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.elements.type.SimpleTextElement;
import co.uk.isxander.evergreenhud.settings.impl.ArraySetting;
import co.uk.isxander.xanderlib.utils.MathUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class ElementLight extends SimpleTextElement {

    public ArraySetting displayMode;

    @Override
    public void initialise() {
        addSettings(displayMode = new ArraySetting("Mode", "Display", "How the value is displayed.", "Absolute", new String[]{"Absolute", "Percentage"}));
    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Light Level", "Get the current light level of where you are standing.", "Simple");
    }

    @Override
    protected String getValue() {
        if (mc.thePlayer == null || mc.theWorld == null)
            return "Unknown";

        BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        Chunk playerChunk = mc.theWorld.getChunkFromBlockCoords(playerPos);

        int light = playerChunk.getLightSubtracted(playerPos, 0);
        if (displayMode.get().equalsIgnoreCase("percentage"))
            return Math.round(MathUtils.getPercent(light, 0, 15) * 100) + "%";

        return Integer.toString(light);
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Light";
    }

}
