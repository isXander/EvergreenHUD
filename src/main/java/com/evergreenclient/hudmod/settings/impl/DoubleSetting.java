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

package com.evergreenclient.hudmod.settings.impl;

import com.evergreenclient.hudmod.settings.Setting;

public class DoubleSetting extends Setting {

    private final double def;
    private double val;
    private final double min, max;
    private final String suffix;

    public DoubleSetting(String name, String description, double val, double min, double max, String suffix, boolean internal) {
        super(name, description, internal);
        this.val = this.def = val;
        this.min = min;
        this.max = max;
        this.suffix = suffix;
    }

    public DoubleSetting(String name, double val, double min, double max, String suffix, boolean internal) {
        super(name, "", internal);
        this.val = this.def = val;
        this.min = min;
        this.max = max;
        this.suffix = suffix;
    }

    public DoubleSetting(String name, String description, double val, double min, double max, String suffix) {
        super(name, description);
        this.val = this.def = val;
        this.min = min;
        this.max = max;
        this.suffix = suffix;
    }

    public DoubleSetting(String name, double val, double min, double max, String suffix) {
        super(name);
        this.val = this.def = val;
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

    @Override
    public void reset() {
        this.val = this.def;
    }
}
