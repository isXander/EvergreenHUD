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

public class ButtonSetting extends Setting {

    private final Runnable value;

    public ButtonSetting(String name, String category, String description, Runnable runnable, boolean internal) {
        super(name, description, category, internal);
        this.value = runnable;
    }

    public ButtonSetting(String name, String category, Runnable runnable, boolean internal) {
        super(name, "", category, internal);
        this.value = runnable;
    }

    public ButtonSetting(String name, String category, String description, Runnable runnable) {
        super(name, description, category);
        this.value = runnable;
    }

    public ButtonSetting(String name, String category, Runnable runnable) {
        super(name, category);
        this.value = runnable;
    }

    @Override
    public void reset() {

    }

    public Runnable get() {
        return value;
    }

}
