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

package co.uk.isxander.evergreenhud.settings.impl;

import co.uk.isxander.evergreenhud.settings.Setting;

import java.util.Arrays;
import java.util.List;

public class  ArraySetting extends Setting {

    private final List<String> options;
    private final int def;
    private int index;

    public ArraySetting(String name, String description, String current, boolean internal, String... options) {
        super(name, description, internal);
        this.options = Arrays.asList(options);
        this.index = this.def = this.options.indexOf(current);
    }

    public ArraySetting(String name, String description, int current, boolean internal, String... options) {
        super(name, description, internal);
        this.options = Arrays.asList(options);
        this.index = this.def = current;
    }

    public ArraySetting(String name, String description, String current, String... options) {
        super(name, description);
        this.options = Arrays.asList(options);
        this.index = this.def = this.options.indexOf(current);
    }

    public ArraySetting(String name, String description, int current, String... options) {
        super(name, description);
        this.options = Arrays.asList(options);
        this.index = this.def = current;
    }

    public ArraySetting(String name, String current, boolean internal, String... options) {
        super(name, "", internal);
        this.options = Arrays.asList(options);
        this.index = this.def = this.options.indexOf(current);
    }

    public ArraySetting(String name, int current, boolean internal, String... options) {
        super(name, "", internal);
        this.options = Arrays.asList(options);
        this.index = this.def = current;
    }

    public ArraySetting(String name, String current, String... options) {
        super(name);
        this.options = Arrays.asList(options);
        this.index = this.def = this.options.indexOf(current);
    }

    public ArraySetting(String name, int current, String... options) {
        super(name);
        this.options = Arrays.asList(options);
        this.index = this.def = current;
    }

    @Override
    public void reset() {
        this.index = this.def;
    }

    public String get() {
        return options.get(index);
    }

    public int getIndex() {
        return index;
    }

    public void set(String current) {
        set(this.options.indexOf(current));
    }

    public void set(int index) {
        if (onChange(this.index, index))
            this.index = index;
    }

    public String next() {
        int i = index + 1;
        if (i > this.options.size() - 1)
            i = 0;
        set(i);
        return this.options.get(index);
    }

    public String previous() {
        int i = index - 1;
        if (i < 0)
            i = this.options.size() - 1;
        set(i);
        return this.options.get(index);
    }

    protected boolean onChange(int currentIndex, int newIndex) {
        return true;
    }

    public List<String> options() {
        return this.options;
    }

}
