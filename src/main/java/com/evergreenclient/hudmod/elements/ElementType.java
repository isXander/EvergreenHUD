/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements;

import com.evergreenclient.hudmod.elements.impl.*;

public enum ElementType {
    ARMOUR(ElementArmour.class),
    BIOME(ElementBiome.class),
    BLOCK_ABOVE(ElementBlockAbove.class),
    COMBO(ElementCombo.class),
    COORDS(ElementCoordinates.class),
    CPS(ElementCps.class),
    DIRECTION(ElementDirection.class),
    FPS(ElementFPS.class),
    IMAGE(ElementImage.class),
    MEMORY(ElementMemory.class),
    PING(ElementPing.class),
    PITCH(ElementPitch.class),
    PLAYER_PREVIEW(ElementPlayerPreview.class),
    REACH(ElementReach.class),
    //SCOREBOARD(new ElementScoreboard()),
    SERVER(ElementServer.class),
    SPEED(ElementSpeed.class),
    TEXT(ElementText.class),
    TIME(ElementTime.class),
    YAW(ElementYaw.class);

    private final Class<? extends Element> element;
    ElementType(Class<? extends Element> element) {
        this.element = element;
    }

    public Class<? extends Element> getElementClass() {
        return element;
    }

    public Element getElement() {
        try {
            return element.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ElementType getType(Element element) {
        for (ElementType type : values()) {
            if (type.getElementClass().equals(element.getClass())) {
                return type;
            }
        }
        return null;
    }
}
