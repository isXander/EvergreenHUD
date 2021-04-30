package co.uk.isxander.evergreenhud.settings.impl;

import co.uk.isxander.evergreenhud.settings.Setting;

public class EnumSetting<T extends Enum<?>> extends Setting {

    private final Class<T> options;
    private final int def;
    private T cached;
    private int index;

    public EnumSetting(String name, String description, T current, boolean internal) {
        super(name, description, internal);
        this.options = (Class<T>) current.getClass();
        this.cached = current;
        this.def = this.index = current.ordinal();
    }

    public EnumSetting(String name, String description, T current) {
        super(name, description);
        this.options = (Class<T>) current.getClass();
        this.cached = current;
        this.def = this.index = current.ordinal();
    }

    public EnumSetting(String name, T current, boolean internal) {
        super(name, "", internal);
        this.options = (Class<T>) current.getClass();
        this.cached = current;
        this.index = this.def = current.ordinal();
    }

    public EnumSetting(String name, T current) {
        super(name);
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
        return true;
    }

    public Class<T> getType() {
        return options;
    }

}
