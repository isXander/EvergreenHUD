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

package co.uk.isxander.evergreenhud.elements;

import co.uk.isxander.evergreenhud.elements.impl.*;
import co.uk.isxander.evergreenhud.utils.BreakException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ElementType {

    public static final ElementType instance = new ElementType();

    private final Map<String, Class<? extends Element>> elements;

    public ElementType() {
        this.elements = new HashMap<>();

        registerNormal();
    }

    private void registerNormal() {
        registerElement("ARMOUR", ElementArmour.class);
        registerElement("BIOME", ElementBiome.class);
        registerElement("BLOCK_ABOVE", ElementBlockAbove.class);
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
        elements.putIfAbsent(name.toUpperCase(), type);
    }

    public Class<? extends Element> getElementClass(String name) {
        return elements.get(name.toUpperCase());
    }

    public Element getElement(String name) {
        try {
            return getElementClass(name).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getType(Element element) {
        AtomicReference<String> name = new AtomicReference<>();
        try {
            elements.forEach((k, v) -> {
                if (v.equals(element.getClass())) {
                    name.set(k);
                    throw new BreakException();
                }
            });
        } catch (BreakException e) {
        }

        return name.get();
    }

    public Map<String, Class<? extends Element>> getTypes() {
        return Collections.unmodifiableMap(elements);
    }
}
