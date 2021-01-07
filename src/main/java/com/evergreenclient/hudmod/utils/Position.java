/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.utils;

public class Position {

    public int x, y;
    public float scale;

    public Position(int x, int y, float scale) {
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    public void add(Position b) {
        x += b.x;
        y += b.y;
        scale += b.scale;
    }

    public void subtract(Position b) {
        x -= b.x;
        y -= b.y;
        scale -= b.scale;
    }

    public static Position center() {
        return new Position(0, 0, 1);
    }

}
