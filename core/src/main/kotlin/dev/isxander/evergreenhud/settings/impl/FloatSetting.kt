package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.settings.JsonType
import dev.isxander.evergreenhud.settings.Setting
import gg.essential.elementa.UIComponent
import java.lang.reflect.Field

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class FloatSetting(val name: String, val category: Array<String>, val description: String, val min: Float, val max: Float, val suffix: String = "", val save: Boolean = true)

class FloatSettingWrapped(annotation: FloatSetting, annotationObject: Any, annotatedField: Field) : Setting<Float, FloatSetting>(annotation, annotationObject, annotatedField, JsonType.FLOAT) {
    override val name: String = annotation.name
    override val category: Array<String> = annotation.category
    override val description: String = annotation.description
    override val shouldSave: Boolean = annotation.save

    override fun getInternal(): Float = annotatedField.getFloat(annotatedObject)
    override fun setInternal(new: Float) = annotatedField.setFloat(annotatedObject, new)

    override var jsonValue: Any
        get() = value
        set(new) { value = new as Float }

    override val defaultJsonValue: Any = default

    override fun addComponentToUI(parent: UIComponent) {
        TODO("Not yet implemented")
    }

}