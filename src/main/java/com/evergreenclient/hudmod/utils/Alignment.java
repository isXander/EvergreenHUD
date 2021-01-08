/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.utils;

public enum Alignment {
    LEFT("Left"),
    CENTER("Center"),
    RIGHT("Right");

    private final String name;

    Alignment(String displayName) {
        this.name = displayName;
    }

    public String getName() {
        return name;
    }
}
