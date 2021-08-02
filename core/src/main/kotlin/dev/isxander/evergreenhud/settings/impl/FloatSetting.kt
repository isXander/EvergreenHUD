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

import dev.isxander.evergreenhud.settings.JsonType
import dev.isxander.evergreenhud.settings.Setting
import gg.essential.elementa.UIComponent
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class FloatSetting(val name: String, val category: Array<String>, val description: String, val min: Float, val max: Float, val suffix: String = "", val save: Boolean = true)

class FloatSettingWrapped(annotation: FloatSetting, annotationObject: Any, annotatedProperty: KProperty1<out Any, Float>) : Setting<Float, FloatSetting>(annotation, annotationObject, annotatedProperty, JsonType.FLOAT) {
    override val name: String = annotation.name
    override val category: Array<String> = annotation.category
    override val description: String = annotation.description
    override val shouldSave: Boolean = annotation.save

    override fun getInternal(): Float = annotatedProperty.call(annotatedObject)
    override fun setInternal(new: Float) = mutableProperty!!.setter.call(annotatedObject, new)

    override var jsonValue: Any
        get() = value
        set(new) { value = new as Float }

    override val defaultJsonValue: Any = default

    override fun addComponentToUI(parent: UIComponent) {
        TODO("Not yet implemented")
    }

}