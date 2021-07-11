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

package co.uk.isxander.evergreenhud.settings;

import lombok.Getter;

public abstract class Setting {

    @Getter private final String name;
    @Getter private final String description;
    @Getter private final String category;
    @Getter private final boolean internal;

    protected Setting(String name, String description, String category, boolean internal) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.internal = internal;
    }

    protected Setting(String name, String description, String category) {
        this(name, description, category, false);
    }

    protected Setting(String name, String category) {
        this(name, null, category);
    }

    public abstract void reset();

    public final String getJsonKey() {
        return name.replace(" ", "").toLowerCase();
    }

    public boolean isDisabled() {
        return false;
    }

    public boolean shouldAddToConfig() {
        return true;
    }

}
