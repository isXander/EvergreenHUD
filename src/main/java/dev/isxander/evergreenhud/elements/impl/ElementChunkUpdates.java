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

package dev.isxander.evergreenhud.elements.impl;

import dev.isxander.evergreenhud.elements.ElementData;
import dev.isxander.evergreenhud.elements.type.SimpleTextElement;
import dev.isxander.evergreenhud.settings.impl.FloatSetting;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ElementChunkUpdates extends SimpleTextElement {

    private long time = 0L;
    private String chunkUpdates = "0";

    public FloatSetting updateTime;

    @Override
    public void initialise() {
        addSettings(updateTime = new FloatSetting("Update Time", "Functionality", "How often the counter updates.", 1, 0.5f, 10, " secs"));
    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Chunk Updates", "Displays the amount of chunk updates currently taking place.", "Simple");
    }

    @Override
    protected String getValue() {
        return chunkUpdates;
    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (System.currentTimeMillis() - time > updateTime.get() * 1000L) {
            time = System.currentTimeMillis();
            chunkUpdates = Integer.toString(RenderChunk.renderChunksUpdated);
        }
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Updates";
    }

}
