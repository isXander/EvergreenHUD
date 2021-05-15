package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.settings.impl.FloatSetting;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ElementChunkUpdates extends Element {

    private long time = 0L;
    private String chunkUpdates = "0";

    public FloatSetting updateTime;

    @Override
    public void initialise() {
        addSettings(updateTime = new FloatSetting("Update Time", "Functionality", "How often the counter updates.", 1, 0.5f, 10, " secs"));
    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Chunk Updates", "Displays the amount of chunk updates currently taking place.");
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
