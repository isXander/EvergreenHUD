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

package dev.isxander.evergreenhud.settings

import java.lang.reflect.Field

abstract class Setting<T, E>(val annotation: E, val annotatedObject: Any, val annotatedField: Field, val jsonValue: JsonValues) {

    init {
        annotatedField.isAccessible = true
    }

    abstract fun getName(): String
    abstract fun getCategory(): String
    abstract fun getDescription(): String
    abstract fun shouldSave(): Boolean

    fun get(): T {
        if (annotatedField.type.equals(SettingAdapter::class.java))
            return (annotatedField.get(annotatedObject) as SettingAdapter<T>).get()

        return getInternal()
    }
    protected abstract fun getInternal(): T

    fun set(new: T) {
        if (annotatedField.type.equals(SettingAdapter::class.java))
            (annotatedField.get(annotatedObject) as SettingAdapter<T>).set(new)
        else
            setInternal(new)
    }
    protected abstract fun setInternal(new: T)

    abstract fun setJsonValue(new: Any)
    abstract fun getJsonValue(): Any
    abstract fun getDefault(): T
    abstract fun getDefaultJsonValue(): Any

    fun reset() {
        set(getDefault())
    }
    fun getJsonKey(): String {
        return getName().toLowerCase().replace(" ", "")
    }
}

open class SettingAdapter<T>(var value: T) {

    /**
     * Adjust the value that is given when getter is invoked
     */
    open fun get(): T {
        return value
    }

    /**
     * Adjust the value the setting is set to when invoked
     */
    open fun set(newVal: T) {
        value = newVal
    }

}





