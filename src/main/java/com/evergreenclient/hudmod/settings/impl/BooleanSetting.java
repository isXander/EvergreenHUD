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

    private boolean value;

    public BooleanSetting(String name, boolean def) {
        super(name);
        this.value = def;
    }

    public boolean get() {
        return value;
    }

    public void set(boolean newVal) {
        value = newVal;
    }

    public boolean toggle() {
        value = !value;
        return value;
    }

}
