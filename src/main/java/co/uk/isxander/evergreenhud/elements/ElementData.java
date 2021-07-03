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

package co.uk.isxander.evergreenhud.elements;

import lombok.Getter;

public class ElementData {

    @Getter private final String name, description, category, notice;
    @Getter private final int maxInstances;

    public ElementData(String name, String description, String category) {
        this(name, description, category, null, Integer.MAX_VALUE);
    }

    public ElementData(String name, String description, String category, String notice) {
        this(name, description, category, notice, Integer.MAX_VALUE);
    }

    public ElementData(String name, String description, String category, int maxInstances) {
        this(name, description, category, null, maxInstances);
    }

    public ElementData(String name, String description, String category, String notice, int maxInstances) {
        if (maxInstances < 1)
            throw new IllegalStateException("Element max instances needs to be at least 1.");

        this.name = name;
        this.description = description;
        this.category = category;
        this.notice = notice;
        this.maxInstances = maxInstances;
    }
}
