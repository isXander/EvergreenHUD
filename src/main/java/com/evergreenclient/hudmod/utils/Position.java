/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.utils;

import net.minecraft.client.gui.ScaledResolution;

public class Position {

    private float x, y, scale;

    Position(float x, float y, float scale) {
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    public static Position getPositionWithRawPositioning(int x, int y, float scale, ScaledResolution resolution) {
        return new Position(MathUtils.getPercent(x, 0, resolution.getScaledWidth()), MathUtils.getPercent(y, 0, resolution.getScaledHeight()), scale);
    }

    public static Position getPositionWithScaledPositioning(float x, float y, float scale) {
        return new Position(x, y, scale);
    }

    public int getRawX(ScaledResolution resolution) {
        return (int) (resolution.getScaledWidth() * x);
    }

    public int getRawY(ScaledResolution resolution) {
        return (int) (resolution.getScaledHeight() * y);
    }

    public float getXScaled() {
        return x;
    }

    public float getYScaled() {
        return y;
    }

    public void setRawX(int x, ScaledResolution resolution) {
        this.x = MathUtils.getPercent(x, 0, resolution.getScaledWidth());
    }

    public void setRawY(int y, ScaledResolution resolution) {
        this.y = MathUtils.getPercent(y, 0, resolution.getScaledHeight());
    }

    public void setScaledX(float x) {
        this.x = x;
    }

    public void setScaledY(float y) {
        this.y = y;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
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
