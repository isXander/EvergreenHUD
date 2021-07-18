package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.settings.JsonType
import dev.isxander.evergreenhud.settings.Setting
import gg.essential.elementa.UIComponent
import java.lang.reflect.Field

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class StringListSetting(val name: String, val category: Array<String>, val description: String, val options: Array<String>, val save: Boolean = true)

@Suppress("UNCHECKED_CAST")
class StringListSettingWrapped(annotation: StringListSetting, annotationObject: Any, annotatedField: Field) : Setting<Int, StringListSetting>(annotation, annotationObject, annotatedField, JsonType.INT) {
    override val name: String = annotation.name
    override val category: Array<String> = annotation.category
    override val description: String = annotation.description
    override val shouldSave: Boolean = annotation.save
    val options: Array<String> = annotation.options

    var string: String
        get() = annotatedField.get(annotatedObject) as String
        set(new) { annotatedField.set(annotatedObject, new) }
    override fun getInternal(): Int = options.indexOf(string)
    override fun setInternal(new: Int) {
        string = options[new]
    }

    override var jsonValue: Any
        get() = value
        set(new) { value = new as Int }

    override val defaultJsonValue: Any = default

    override fun addComponentToUI(parent: UIComponent) {
        TODO("Not yet implemented")
    }
}