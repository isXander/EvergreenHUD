/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.settings.impl;

import com.evergreenclient.hudmod.settings.Setting;

public class ButtonSetting extends Setting {

    private final Runnable value;

    protected ButtonSetting(String name, Runnable runnable) {
        super(name);
        this.value = runnable;
    }

    @Override
    public void reset() {

    }

    public Runnable get() {
        return value;
    }

}
