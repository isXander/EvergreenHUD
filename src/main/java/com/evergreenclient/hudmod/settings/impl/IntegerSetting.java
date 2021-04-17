/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.settings.impl;

import com.evergreenclient.hudmod.settings.Setting;

public class IntegerSetting extends Setting {

    private final int def;
    private int val;
    private final int min, max;
    private final String suffix;

    public IntegerSetting(String name, String description, int val, int min, int max, String suffix, boolean internal) {
        super(name, description, internal);
        this.val = this.def = val;
        this.min = min;
        this.max = max;
        this.suffix = suffix;
    }

    public IntegerSetting(String name, int val, int min, int max, String suffix, boolean internal) {
        super(name, "", internal);
        this.val = this.def = val;
        this.min = min;
        this.max = max;
        this.suffix = suffix;
    }

    public IntegerSetting(String name, String description, int val, int min, int max, String suffix) {
        super(name, description);
        this.val = this.def = val;
        this.min = min;
        this.max = max;
        this.suffix = suffix;
    }

    public IntegerSetting(String name, int val, int min, int max, String suffix) {
        super(name);
        this.val = this.def = val;
        this.min = min;
        this.max = max;
        this.suffix = suffix;
    }

    @Override
    public void reset() {
        this.val = def;
    }

    public int get() {
        return val;
    }

    public void set(int newVal) {
        if (onChange(this.val, newVal))
            this.val = newVal;
    }

    protected boolean onChange(int currentVal, int newVal) {
        return true;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public String getSuffix() {
        return suffix;
    }

}
