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

package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.settings.HoconType
import dev.isxander.evergreenhud.settings.Setting
import gg.essential.elementa.UIComponent
import kotlin.reflect.KProperty1

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class StringSetting(val name: String, val category: Array<String>, val description: String, val save: Boolean = true)

class StringSettingWrapped(annotation: StringSetting, annotationObject: Any, annotatedProperty: KProperty1<out Any, String>) : Setting<String, StringSetting>(annotation, annotationObject, annotatedProperty, HoconType.STRING) {
    override val name: String = annotation.name
    override val category: Array<String> = annotation.category
    override val description: String = annotation.description
    override val shouldSave: Boolean = annotation.save

    override fun getInternal(): String = annotatedProperty.call(annotatedObject)
    override fun setInternal(new: String) = mutableProperty!!.setter.call(annotatedObject, new)

    override var serializedValue: Any
        get() = value
        set(new) { value = new as String }

    override val defaultSerializedValue: Any = default

    override fun addComponentToUI(parent: UIComponent) {
        TODO("Not yet implemented")
    }
}
