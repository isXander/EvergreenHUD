/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.utils;

public enum Facing {
    NORTH("North", "N"),
    NORTH_EAST("North East", "NE"),
    EAST("East", "E"),
    SOUTH_EAST("South East", "SE"),
    SOUTH("South", "S"),
    SOUTH_WEST("South West", "SW"),
    WEST("West", "W"),
    NORTH_WEST("North West", "NW");

    private final String big;
    private final String small;
    Facing(String big, String small) {
        this.big = big;
        this.small = small;
    }

    public String getNormal() {
        return this.big;
    }

    public String getAbbreviated() {
        return this.small;
    }
}
