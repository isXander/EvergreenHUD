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

package co.uk.isxander.evergreenhud.elements;

import co.uk.isxander.evergreenhud.elements.impl.*;
import co.uk.isxander.evergreenhud.gui.screens.GuiScreenElements;
import co.uk.isxander.xanderlib.utils.BreakException;
import co.uk.isxander.xanderlib.utils.Constants;
import co.uk.isxander.evergreenhud.config.ElementConfig;
import co.uk.isxander.evergreenhud.config.MainConfig;
import co.uk.isxander.evergreenhud.event.EventManager;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ElementManager implements Constants {

    private final Map<String, Class<? extends Element>> availableElements;
    private final List<Element> currentElements;

    /* Config */
    private final MainConfig mainConfig;
    private final ElementConfig elementConfig;
    private boolean enabled;

    private final Logger logger;

    public ElementManager() {
        this.logger = LogManager.getLogger("Evergreen Manager");

        this.availableElements = new HashMap<>();
        this.currentElements = new ArrayList<>();
        this.mainConfig = new MainConfig(this);
        this.elementConfig = new ElementConfig(this);
        resetConfig();

        registerNormals();
    }

    private void registerNormals() {
        registerElement("ARMOUR", ElementArmour.class);
        registerElement("BIOME", ElementBiome.class);
        registerElement("BLOCK_ABOVE", ElementBlockAbove.class);
        registerElement("CHUNK_COUNT", ElementChunkRenderCount.class);
        registerElement("CHUNK_UPDATES", ElementChunkUpdates.class);
        registerElement("COMBO", ElementCombo.class);
        registerElement("COORDS", ElementCoordinates.class);
        registerElement("CPS", ElementCps.class);
        registerElement("DAY", ElementDay.class);
        registerElement("DIRECTION", ElementDirection.class);
        registerElement("ENTITY_COUNT", ElementEntityCount.class);
        registerElement("FPS", ElementFps.class);
        registerElement("HYPIXEL_GAME", ElementHypixelGame.class);
        registerElement("HYPIXEL_MAP", ElementHypixelMap.class);
        registerElement("HYPIXEL_MODE", ElementHypixelMode.class);
        registerElement("IMAGE", ElementImage.class);
        registerElement("LIGHT", ElementLight.class);
        registerElement("MEMORY", ElementMemory.class);
        registerElement("PING", ElementPing.class);
        registerElement("PITCH", ElementPitch.class);
        registerElement("PLAYER_PREVIEW", ElementPlayerPreview.class);
        registerElement("REACH", ElementReach.class);
        registerElement("SERVER", ElementServer.class);
        registerElement("SPEED", ElementSpeed.class);
        registerElement("TEXT", ElementText.class);
        registerElement("TIME", ElementTime.class);
        registerElement("YAW", ElementYaw.class);
    }

    /**
     * Registers an element to Evergreen
     *
     * @param name the internal name of the element. Example: MY_NEW_ELEMENT
     * @param type class of your element
     */
    public void registerElement(String name, Class<? extends Element> type) {
        availableElements.putIfAbsent(name, type);
    }

    public Class<? extends Element> getElementClass(String name) {
        return availableElements.get(name);
    }

    public Element getNewElementInstance(String name) {
        Class<? extends Element> elementClass = getElementClass(name);
        if (elementClass == null) return null;
        try {
            return elementClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getElementIdentifier(Element element) {
        AtomicReference<String> name = new AtomicReference<>();
        try {
            availableElements.forEach((k, v) -> {
                if (v.equals(element.getClass())) {
                    name.set(k);
                    throw new BreakException();
                }
            });
        } catch (BreakException ignored) {
        }

        return name.get();
    }

    public Map<String, Class<? extends Element>> getAvailableElements() {
        return Collections.unmodifiableMap(availableElements);
    }

    public void resetConfig() {
        this.enabled = true;
    }

    public List<Element> getCurrentElements() {
        return currentElements;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;

        if (isEnabled()) {
            mc.mcProfiler.startSection("Element Render");

            boolean inChat = mc.currentScreen instanceof GuiChat;
            boolean inDebug = mc.gameSettings.showDebugInfo;
            boolean inGui = mc.currentScreen != null && !(mc.currentScreen instanceof GuiScreenElements) && !(mc.currentScreen instanceof GuiChat);

            for (Element e : currentElements) {
                if ((mc.inGameHasFocus && !inDebug) || (e.getShowInChat().get() && inChat) || (e.getShowInDebug().get() && inDebug && !(!e.getShowInChat().get() && inChat)) || (e.getShowUnderGui().get() && inGui)) {
                    e.render(event.partialTicks, RenderOrigin.HUD);
                }
            }
            mc.mcProfiler.endSection();
        }
    }

    public void addElement(Element element) {
        EventManager.getInstance().addListener(element);
        this.currentElements.add(element);
        element.onAdded();
    }

    public void removeElement(Element element) {
        EventManager.getInstance().removeListener(element);
        this.currentElements.remove(element);
        element.onRemoved();
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

}
