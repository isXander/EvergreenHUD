/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements;

import com.evergreenclient.hudmod.elements.impl.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.List;

public class ElementManager {

    private final List<Element> elements;

    public ElementManager() {
        this.elements = Arrays.asList(
                new ElementFPS(),
                new ElementCoordinates(),
                new ElementServer(),
                new ElementCps(),
                new ElementReach(),
                new ElementMemory()
        );
    }

    public List<Element> getElements() {
        return elements;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        if (Minecraft.getMinecraft().inGameHasFocus || Minecraft.getMinecraft().currentScreen instanceof GuiChat)
            for (Element e : elements)
                if (e.isEnabled())
                    e.render();
    }

    public void saveAll() {
        for (Element e : elements) {
            e.getConfig().save();
        }
    }

    public void resetAll() {
        for (Element e : elements) {
            e.resetSettings();
        }
    }

}
