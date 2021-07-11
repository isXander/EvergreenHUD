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
import co.uk.isxander.evergreenhud.settings.impl.IntegerSetting;
import co.uk.isxander.evergreenhud.settings.impl.StringSetting;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayDeque;
import java.util.Deque;

public class ElementBlockCount extends SimpleTextElement {

    public IntegerSetting interval;
    public StringSetting suffix;

    private final Deque<Long> blocks = new ArrayDeque<>();

    @Override
    public void initialise() {
        addSettings(interval = new IntegerSetting("Interval", "Display", "How long it should store the place.", 1000, 500, 5000, " ms"));
        addSettings(suffix = new StringSetting("Suffix", "Display", "What text to display after the value.", ""));
    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Block Place Count", "How many blocks you have placed in a certain interval.", "Simple");
    }

    @Override
    protected String getValue() {
        return blocks.size() + suffix.get();
    }

    @Override
    public void onBlockPlaced(BlockEvent.PlaceEvent event) {
        if (event.player.getEntityId() != mc.thePlayer.getEntityId()) return;

        blocks.add(System.currentTimeMillis());
    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        final long currentTime = System.currentTimeMillis();
        if (!blocks.isEmpty()) {
            while ((currentTime - blocks.getFirst()) > interval.get()) {
                blocks.removeFirst();
                if (blocks.isEmpty()) break;
            }
        }
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Blocks";
    }

}
