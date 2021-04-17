/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.settings;

public abstract class Setting {

    private final String name;
    private final String description;
    private boolean internal;

    protected Setting(String name, String description, boolean internal) {
        this.name = name;
        this.description = description;
        this.internal = internal;
    }

    protected Setting(String name, String description) {
        this(name, description, false);
    }

    protected Setting(String name) {
        this(name, "", false);
    }

    public abstract void reset();

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isInternal() {
        return internal;
    }

    public final String getJsonKey() {
        return name.replace(" ", "").toLowerCase();
    }

}
