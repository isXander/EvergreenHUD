package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.settings.JsonType
import dev.isxander.evergreenhud.settings.Setting
import gg.essential.elementa.UIComponent
import java.lang.reflect.Field

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class StringSetting(val name: String, val category: Array<String>, val description: String, val save: Boolean = true)

class StringSettingWrapped(annotation: StringSetting, annotationObject: Any, annotatedField: Field) : Setting<String, StringSetting>(annotation, annotationObject, annotatedField, JsonType.STRING) {
    override val name: String = annotation.name
    override val category: Array<String> = annotation.category
    override val description: String = annotation.description
    override val shouldSave: Boolean = annotation.save

    override fun getInternal(): String = annotatedField.get(annotatedObject) as String
    override fun setInternal(new: String) = annotatedField.set(annotatedObject, new)

    override var jsonValue: Any
        get() = value
        set(new) { value = new as String }

    override val defaultJsonValue: Any = default

    override fun addComponentToUI(parent: UIComponent) {
        TODO("Not yet implemented")
    }
}
