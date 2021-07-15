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

import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import java.lang.reflect.Field

@Suppress("UNCHECKED_CAST")
abstract class Setting<T, E>(val annotation: E, val annotatedObject: Any, val annotatedField: Field, val jsonValue: JsonValues) {

    init {
        annotatedField.isAccessible = true
    }

    abstract fun getName(): String
    abstract fun getCategory(): Array<String>
    abstract fun getDescription(): String
    abstract fun shouldSave(): Boolean

    fun get(): T {
        if (SettingAdapter::class.java.isAssignableFrom(annotatedField.type))
            return (annotatedField.get(annotatedObject) as SettingAdapter<T>).get()

        return getInternal()
    }
    protected abstract fun getInternal(): T

    fun set(new: T) {
        if (SettingAdapter::class.java.isAssignableFrom(annotatedField.type))
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
    fun getNameJsonKey(): String {
        return getName().lowercase().trim().replace(" ", "_")
    }
    fun getCategoryJsonKey(): List<String> {
        return getCategory().map {
            it.lowercase().trim().replace(" ", "_")
        }
    }
}

class SettingAdapter<T>(private var value: T) {

    private var getter: (T) -> T = { it }
    private var setter: (T) -> T = { it }

    /**
     * Adjust the value that is given when getter is invoked
     */
    fun modGet(block: (T) -> T): SettingAdapter<T> {
        getter = block
        return this
    }

    /**
     * Adjust the value the setting is set to when invoked
     */
    fun modSet(block: (T) -> T): SettingAdapter<T> {
        setter = block
        return this
    }

    fun get(): T = getter.invoke(value)
    fun set(new: T) {
        value = setter.invoke(new)
    }

}





