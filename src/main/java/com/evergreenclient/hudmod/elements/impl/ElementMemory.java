/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.impl;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.elements.ElementType;
import com.evergreenclient.hudmod.settings.impl.BooleanSetting;
import com.evergreenclient.hudmod.utils.MathUtils;
import com.evergreenclient.hudmod.utils.ElementData;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.text.DecimalFormat;

public class ElementMemory extends Element {

    private String memDisplay = "";
    private long lastUpdated = 0L;

    public BooleanSetting trailingZeros;

    @Override
    public void initialise() {
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
            DecimalFormat df = new DecimalFormat(trailingZeros.get() ? "0.0%" : "#.#%");
            memDisplay = df.format(MathUtils.getPercent(bytesToMb(Runtime.getRuntime().totalMemory() -
                    Runtime.getRuntime().freeMemory()), 0, bytesToMb(Runtime.getRuntime().maxMemory())));
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
