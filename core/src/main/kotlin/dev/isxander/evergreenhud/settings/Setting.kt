/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
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
import kotlin.reflect.jvm.isAccessible

@Suppress("UNCHECKED_CAST")
abstract class Setting<T, A>(val annotation: A, val annotatedObject: Any, val annotatedProperty: KProperty1<out Any, T>, val dataType: DataType) {

    abstract val name: String
    abstract val category: Array<String>
    abstract val description: String
    abstract val shouldSave: Boolean
    val default = value

    protected val mutableProperty = annotatedProperty as? KMutableProperty1<out Any, T>

    val adapter: SettingAdapter<T>?
        get() = annotatedProperty.call(annotatedObject) as? SettingAdapter<T>
    val usesAdapter: Boolean = adapter != null
    val readOnly: Boolean = !usesAdapter && annotatedProperty.isFinal

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
        get() = adapter?.hidden ?: false

    protected abstract fun getInternal(): T
    protected abstract fun setInternal(new: T)

    abstract var serializedValue: Any
    abstract val defaultSerializedValue: Any
    val nameSerializedKey: String
        get() = name.lowercase().trim().replace(" ", "")

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





