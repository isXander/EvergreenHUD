package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.settings.impl.DoubleSetting;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ElementChunkUpdates extends Element {

    private long time = 0L;
    private String chunkUpdates = "0";

    public DoubleSetting updateTime;

    @Override
    public void initialise() {
        addSettings(updateTime = new DoubleSetting("Update Time", "How often the counter updates.", 1, 0.5, 10, " secs"));
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
    public String getDisplayTitle() {
        return "Updates";
    }

}
