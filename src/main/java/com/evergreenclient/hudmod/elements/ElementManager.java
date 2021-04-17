/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements;

import com.evergreenclient.hudmod.config.ElementConfig;
import com.evergreenclient.hudmod.config.MainConfig;
import com.evergreenclient.hudmod.event.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ElementManager {

    private final Minecraft mc = Minecraft.getMinecraft();

    private final List<Element> currentElements;

    /* Config */
    private final MainConfig mainConfig;
    private final ElementConfig elementConfig;
    private boolean enabled;
    private boolean showInChat;
    private boolean showInDebug;

    private final Logger logger;

    public ElementManager() {
        this.currentElements = new ArrayList<>();
        this.mainConfig = new MainConfig(this);
        this.elementConfig = new ElementConfig(this);
        resetConfig();

        this.logger = LogManager.getLogger("Evergreen Manager");
        this.getElementConfig().load();
        this.getMainConfig().load();
    }

    public void resetConfig() {
        this.enabled = true;
        this.showInChat = true;
        this.showInDebug = false;
    }

    public List<Element> getCurrentElements() {
        return currentElements;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;

        if (isEnabled()) {
            mc.mcProfiler.startSection("Element Render");
            if ((mc.inGameHasFocus && !mc.gameSettings.showDebugInfo) || (mc.gameSettings.showDebugInfo && showInDebug) || (mc.currentScreen instanceof GuiChat && showInChat)) {
                for (Element e : currentElements) {
                    e.render(event);
                }
            }
            mc.mcProfiler.endSection();
        }
    }

    public void addElement(Element element) {
        EventManager.getInstance().addListener(element);
        this.currentElements.add(element);
    }

    public void removeElement(Element element) {
        EventManager.getInstance().removeListener(element);
        this.currentElements.remove(element);
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public ElementConfig getElementConfig() {
        return elementConfig;
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
}
