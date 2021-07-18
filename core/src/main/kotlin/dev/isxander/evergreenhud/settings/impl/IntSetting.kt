package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.settings.JsonType
import dev.isxander.evergreenhud.settings.Setting
import gg.essential.elementa.UIComponent
import java.lang.reflect.Field

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class IntSetting(val name: String, val category: Array<String>, val description: String, val min: Int, val max: Int, val suffix: String = "", val save: Boolean = true)

class IntSettingWrapped(annotation: IntSetting, annotationObject: Any, annotatedField: Field) : Setting<Int, IntSetting>(annotation, annotationObject, annotatedField, JsonType.INT) {
    override val name: String = annotation.name
    override val category: Array<String> = annotation.category
    override val description: String = annotation.description
    override val shouldSave: Boolean = annotation.save

    override fun getInternal(): Int = annotatedField.getInt(annotatedObject)
    override fun setInternal(new: Int) = annotatedField.setInt(annotatedObject, new)

    override var jsonValue: Any
        get() = value
        set(new) { value = new as Int }

    override val defaultJsonValue: Any = default

    override fun addComponentToUI(parent: UIComponent) {
        TODO("Not yet implemented")
    }
}