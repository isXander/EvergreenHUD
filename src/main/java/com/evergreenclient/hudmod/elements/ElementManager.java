/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements;

import com.evergreenclient.hudmod.config.MainConfig;
import com.evergreenclient.hudmod.elements.impl.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class ElementManager {

    private final Minecraft mc = Minecraft.getMinecraft();

    private final List<Element> elements;

    /* Config */
    private final MainConfig config;
    private boolean enabled;
    private boolean showInChat;
    private boolean showInDebug;
    private boolean colorsInGui;

    private final Logger logger;

    public ElementManager() {
        this.config = new MainConfig(this);
        resetConfig();

        this.elements = Arrays.asList(
                new ElementFPS(),
                new ElementCoordinates(),
                new ElementBiome(),
                new ElementServer(),
                new ElementCps(),
                new ElementReach(),
                new ElementMemory(),
                new ElementTime(),
                new ElementDirection(),
                new ElementSpeed(),
                new ElementPing(),
                new ElementCombo(),
                new ElementYaw(),
                new ElementPitch(),
                new ElementBlockAbove(),
                new ElementText(),
                new ElementArmour(),
                new ElementImage(),
                new ElementPlayerPreview()
                //new ElementScoreboard()
        );

        this.logger = LogManager.getLogger("Evergreen Manager");
        this.getConfig().load();
    }

    public void resetConfig() {
        this.enabled = true;
        this.showInChat = true;
        this.showInDebug = false;
        this.colorsInGui = true;
    }

    public List<Element> getElements() {
        return elements;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;

        if (isEnabled()) {
            if ((mc.inGameHasFocus && !mc.gameSettings.showDebugInfo) || (mc.gameSettings.showDebugInfo && showInDebug) || (mc.currentScreen instanceof GuiChat && showInChat)) {
                for (Element e : elements) {
                    if (e.isEnabled()) {
                        e.render(event);
                    }
                }
            }
        }
    }

    public void saveAll() {
        for (Element e : elements) e.getConfig().save();
    }

    public void resetAll() {
        for (Element e : elements) e.resetSettings();
    }

    public MainConfig getConfig() {
        return config;
    }

    public Logger getLogger() {
        return logger;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean doShowInChat() {
        return showInChat;
    }

    public void setShowInChat(boolean showInChat) {
        this.showInChat = showInChat;
    }

    public boolean doShowInDebug() {
        return showInDebug;
    }

    public void setShowInDebug(boolean showInDebug) {
        this.showInDebug = showInDebug;
    }

    public boolean doColorsInGui() {
        return colorsInGui;
    }

    public void setColorsInGui(boolean colorsInGui) {
        this.colorsInGui = colorsInGui;
    }
}
