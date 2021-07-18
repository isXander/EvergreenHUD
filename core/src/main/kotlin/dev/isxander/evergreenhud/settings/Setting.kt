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

import dev.isxander.evergreenhud.settings.impl.*
import gg.essential.elementa.UIComponent
import java.lang.reflect.Field

@Suppress("UNCHECKED_CAST")
abstract class Setting<T, A>(val annotation: A, val annotatedObject: Any, val annotatedField: Field, val jsonType: JsonType) {

    abstract val name: String
    abstract val category: Array<String>
    abstract val description: String
    abstract val shouldSave: Boolean
    val default = value

    init {
        annotatedField.isAccessible = true
    }

    var value: T
        get() {
            if (SettingAdapter::class.java.isAssignableFrom(annotatedField.type))
                return (annotatedField.get(annotatedObject) as SettingAdapter<T>).get()

            return getInternal()
        }
        set(new) {
            if (SettingAdapter::class.java.isAssignableFrom(annotatedField.type))
                (annotatedField.get(annotatedObject) as SettingAdapter<T>).set(new)
            else
                setInternal(new)
        }

    protected abstract fun getInternal(): T
    protected abstract fun setInternal(new: T)

    abstract var jsonValue: Any
    abstract val defaultJsonValue: Any
    val nameJsonKey: String
        get() = name.lowercase().trim().replace(" ", "_")

    fun reset() {
        value = default
    }

    abstract fun addComponentToUI(parent: UIComponent)

    companion object {
        val registeredSettings = mapOf<Class<Annotation>, Class<out Setting<*, *>>>(
            BooleanSetting::class.java as Class<Annotation> to BooleanSettingWrapped::class.java,
            ColorSetting::class.java as Class<Annotation> to ColorSettingWrapped::class.java,
            OptionSetting::class.java as Class<Annotation> to OptionSettingWrapped::class.java,
            FloatSetting::class.java as Class<Annotation> to FloatSettingWrapped::class.java,
            IntSetting::class.java as Class<Annotation> to IntSettingWrapped::class.java,
            StringListSetting::class.java as Class<Annotation> to StringListSettingWrapped::class.java,
            StringSetting::class.java as Class<Annotation> to StringSettingWrapped::class.java
        )
    }
}

class SettingAdapter<T>(private var value: T) {

    private var getter: (T) -> T = { it }
    private var setter: (T) -> T = { it }

    /**
     * Adjust the value that is given when getter is invoked
     */
    fun adaptGetter(block: (T) -> T): SettingAdapter<T> {
        getter = block
        return this
    }

    /**
     * Adjust the value the setting is set to when invoked
     */
    fun adaptSetter(block: (T) -> T): SettingAdapter<T> {
        setter = block
        return this
    }

    fun get(): T = getter.invoke(value)
    fun set(new: T) {
        value = setter.invoke(new)
    }

}





