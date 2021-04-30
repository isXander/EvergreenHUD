/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/Evergreen-Client/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.settings.impl.ArraySetting;
import co.uk.isxander.evergreenhud.settings.impl.BooleanSetting;
import co.uk.isxander.xanderlib.utils.MathUtils;
import co.uk.isxander.evergreenhud.elements.ElementData;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.text.DecimalFormat;

public class ElementMemory extends Element {

    private String memDisplay = "";
    private long lastUpdated = 0L;

    public BooleanSetting trailingZeros;
    public ArraySetting displayMode;

    @Override
    public void initialise() {
        addSettings(displayMode = new ArraySetting("Display", "How the value will be displayed.", "Absolute", new String[]{"Absolute", "Percentage"}));
        addSettings(trailingZeros = new BooleanSetting("Trailing Zeros", "Add zeroes to match the accuracy.", false));
    }

    @Override
    public ElementData metadata() {
        return new ElementData("Memory", "Shows how much memory minecraft is utilising.");
    }

    @Override
    protected String getValue() {
        return memDisplay;
    }

    // Update memory every second to minimize lag
    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (lastUpdated < System.currentTimeMillis() - 1000L) {
            if (displayMode.get().equalsIgnoreCase("absolute")) {
                DecimalFormat df = new DecimalFormat((trailingZeros.get() ? "0.0" : "#.#"));
                memDisplay = df.format(bytesToMb(Runtime.getRuntime().totalMemory() -
                        Runtime.getRuntime().freeMemory()) / 1024f) + " GB";
            } else {
                DecimalFormat df = new DecimalFormat((trailingZeros.get() ? "0.0%" : "#.#") + (displayMode.get().equalsIgnoreCase("percentage") ? "%" : " GB"));
                memDisplay = df.format(MathUtils.getPercent(bytesToMb(Runtime.getRuntime().totalMemory() -
                        Runtime.getRuntime().freeMemory()), 0, bytesToMb(Runtime.getRuntime().maxMemory())));
            }
            lastUpdated = System.currentTimeMillis();
        }
    }

    private long bytesToMb(long bytes) {
        return bytes / 1024L / 1024L;
    }

    @Override
    public String getDisplayTitle() {
        return "Mem";
    }

}
