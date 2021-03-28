/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.settings.impl;

import com.evergreenclient.hudmod.settings.Setting;

public class BooleanSetting extends Setting {

    private final boolean def;
    private boolean value;

    public BooleanSetting(String name, String description, boolean def) {
        super(name, description);
        this.value = this.def = def;
    }

    public BooleanSetting(String name, boolean def) {
        super(name);
        this.value = this.def = def;
    }

    public boolean get() {
        return value;
    }

    public void set(boolean newVal) {
        if (onChange(value, newVal))
            value = newVal;
    }

    public boolean toggle() {
        boolean newVal = !value;
        if (onChange(value, newVal)) {
            value = newVal;
        }
        return value;
    }

    protected boolean onChange(boolean oldValue, boolean newValue) {
        return true;
    }

    @Override
    public void reset() {
        this.value = def;
    }
}
