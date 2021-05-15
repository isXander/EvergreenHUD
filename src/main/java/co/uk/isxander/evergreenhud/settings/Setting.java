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

package co.uk.isxander.evergreenhud.settings;

public abstract class Setting {

    private final String name;
    private final String description;
    private final String category;
    private final boolean internal;

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
        this(name, "", category);
    }

    public abstract void reset();

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public boolean isInternal() {
        return internal;
    }

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
