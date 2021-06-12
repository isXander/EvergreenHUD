/*
 * Copyright (C) isXander [2019 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package co.uk.isxander.evergreenhud.settings.impl;

import co.uk.isxander.evergreenhud.settings.Setting;

public class BooleanSetting extends Setting {

    private final boolean def;
    private boolean value;

    public BooleanSetting(String name, String category, String description, boolean def, boolean internal) {
        super(name, description, category, internal);
        this.value = this.def = def;
    }

    public BooleanSetting(String name, String category, boolean def, boolean internal) {
        super(name, "", category, internal);
        this.value = this.def = def;
    }

    public BooleanSetting(String name, String category, String description, boolean def) {
        super(name, description ,category);
        this.value = this.def = def;
    }

    public BooleanSetting(String name, String category, boolean def) {
        super(name, category);
        this.value = this.def = def;
    }

    public boolean get() {
        return value;
    }

    public boolean getDefault() {
        return def;
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
        return !isDisabled();
    }

    @Override
    public void reset() {
        this.value = def;
    }
}
