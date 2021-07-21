/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/gpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.settings

import dev.isxander.evergreenhud.settings.impl.*
import gg.essential.elementa.UIComponent
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createType
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

@Suppress("UNCHECKED_CAST")
abstract class Setting<T, A>(val annotation: A, val annotatedObject: Any, val annotatedProperty: KProperty1<out Any, T>, val jsonType: JsonType) {

    abstract val name: String
    abstract val category: Array<String>
    abstract val description: String
    abstract val shouldSave: Boolean
    val default = value

    protected val mutableProperty = annotatedProperty as? KMutableProperty1<out Any, T>

    val adapter: SettingAdapter<T>?
        get() = annotatedProperty.call(annotatedObject) as? SettingAdapter<T>
    val usesAdapter: Boolean = adapter != null
    val readOnly: Boolean = (!usesAdapter && annotatedProperty.isFinal)
        || (usesAdapter && annotatedProperty.hasAnnotation<SettingAdapter.ReadOnly>())

    init {
        annotatedProperty.isAccessible = true
    }

    var value: T
        get() = adapter?.get() ?: getInternal()

        set(new) {
            if (readOnly) return
            adapter?.set(new) ?: setInternal(new)
        }

    val hidden: Boolean
        get() = adapter?.isHidden() ?: false

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
        val registeredSettings = mapOf<KClass<out Annotation>, KClass<out Setting<*, *>>>(
            BooleanSetting::class to BooleanSettingWrapped::class,
            ColorSetting::class to ColorSettingWrapped::class,
            OptionSetting::class to OptionSettingWrapped::class,
            FloatSetting::class to FloatSettingWrapped::class,
            IntSetting::class to IntSettingWrapped::class,
            StringListSetting::class to StringListSettingWrapped::class,
            StringSetting::class to StringSettingWrapped::class
        )
    }
}

class SettingAdapter<T>(var value: T) {

    private var getter: (T) -> T = { it }
    private var setter: (T) -> T = { it }
    private var hidden: ArrayList<(T) -> Boolean> = arrayListOf()

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

    /**
     * Predicate for GUIs.
     * When true, setting will be hidden from guis
     */
    fun hiddenIf(block: (T) -> Boolean): SettingAdapter<T> {
        hidden.add(block)
        return this
    }

    fun get(): T = getter.invoke(value)
    fun set(new: T) {
        value = setter.invoke(new)
    }
    fun isHidden(): Boolean {
        for (predicate in hidden) {
            if (predicate.invoke(value))
                return true
        }
        return false
    }

    @Target(AnnotationTarget.FIELD)
    @MustBeDocumented
    annotation class ReadOnly

}





