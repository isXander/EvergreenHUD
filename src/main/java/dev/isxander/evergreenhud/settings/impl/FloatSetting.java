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

package dev.isxander.evergreenhud.settings.impl;

import dev.isxander.evergreenhud.settings.Setting;
import lombok.Getter;
import lombok.Setter;

public class FloatSetting extends Setting {

    private final float def;
    private float val;
    @Getter @Setter private float min, max;
    @Getter private final String suffix;

    public FloatSetting(String name, String category, String description, float val, float min, float max, String suffix, boolean internal) {
        super(name, description, category, internal);
        this.val = this.def = val;
        this.min = min;
        this.max = max;
        this.suffix = suffix;
    }

    public FloatSetting(String name, String category, float val, float min, float max, String suffix, boolean internal) {
        super(name, "", category, internal);
        this.val = this.def = val;
        this.min = min;
        this.max = max;
        this.suffix = suffix;
    }

    public FloatSetting(String name, String category, String description, float val, float min, float max, String suffix) {
        super(name, description, category);
        this.val = this.def = val;
        this.min = min;
        this.max = max;
        this.suffix = suffix;
    }

    public FloatSetting(String name, String category, float val, float min, float max, String suffix) {
        super(name, category);
        this.val = this.def = val;
        this.min = min;
        this.max = max;
        this.suffix = suffix;
    }

    public float get() {
        return val;
    }

    public float getDefault() {
        return this.def;
    }

    public void set(float newVal) {
        this.val = newVal;
    }

    @Override
    public void reset() {
        this.val = this.def;
    }
}
