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

public class EnumSetting<T extends Enum<?>> extends Setting {

    private final Class<T> options;
    private final int def;
    private T cached;
    private int index;

    public EnumSetting(String name, String category, String description, T current, boolean internal) {
        super(name, description, category, internal);
        this.options = (Class<T>) current.getClass();
        this.cached = current;
        this.def = this.index = current.ordinal();
    }

    public EnumSetting(String name, String category, String description, T current) {
        super(name, description, category);
        this.options = (Class<T>) current.getClass();
        this.cached = current;
        this.def = this.index = current.ordinal();
    }

    public EnumSetting(String name, String category, T current, boolean internal) {
        super(name, "", category, internal);
        this.options = (Class<T>) current.getClass();
        this.cached = current;
        this.index = this.def = current.ordinal();
    }

    public EnumSetting(String name, String category, T current) {
        super(name, category);
        this.options = (Class<T>) current.getClass();
        this.cached = current;
        this.index = this.def = current.ordinal();
    }

    @Override
    public void reset() {
        this.index = this.def;
    }

    public T get() {
        return cached;
    }

    public int getIndex() {
        return index;
    }

    public void set(T current) {
        set(Enum.valueOf(current.getClass(), current.name()).ordinal());
    }

    public void set(int index) {
        if (onChange(this.index, index)) {
            this.cached = options.getEnumConstants()[index];
            this.index = index;
        }
    }

    public T next() {
        int i = index + 1;
        if (i > this.options.getEnumConstants().length - 1)
            i = 0;
        set(i);
        return cached;
    }

    public T previous() {
        int i = index - 1;
        if (i < 0)
            i = this.options.getEnumConstants().length - 1;
        set(i);
        return cached;
    }

    protected boolean onChange(int currentIndex, int newIndex) {
        return !isDisabled();
    }

    public Class<T> getType() {
        return options;
    }

}
