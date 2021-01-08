/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.settings.impl;

import com.evergreenclient.hudmod.settings.Setting;

public class DoubleSetting extends Setting {

    private double val;
    private final double min, max;
    private final String suffix;

    public DoubleSetting(String name, double val, double min, double max, String suffix) {
        super(name);
        this.val = val;
        this.min = min;
        this.max = max;
        this.suffix = suffix;
    }

    public double get() {
        return val;
    }

    public void set(double newVal) {
        this.val = newVal;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public String getSuffix() {
        return suffix;
    }

}
