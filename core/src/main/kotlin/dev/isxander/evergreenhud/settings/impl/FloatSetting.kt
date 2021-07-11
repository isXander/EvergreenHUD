package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.settings.JsonValues
import dev.isxander.evergreenhud.settings.Setting
import java.lang.IllegalArgumentException
import java.lang.reflect.Field

@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class FloatSetting(val name: String, val category: String, val description: String, val min: Float, val max: Float, val suffix: String = "", val save: Boolean = true)

class FloatSettingWrapped(annotation: FloatSetting, annotationObject: Any, annotatedField: Field) : Setting<Float, FloatSetting>(annotation, annotationObject, annotatedField, JsonValues.FLOAT) {
    private val default: Float = get()

    override fun getName(): String = annotation.name
    override fun getCategory(): String = annotation.category
    override fun getDescription(): String = annotation.description
    override fun shouldSave(): Boolean = annotation.save

    override fun getInternal(): Float {
        return annotatedField.getFloat(annotatedObject)
    }

    override fun setInternal(new: Float) {
        annotatedField.setFloat(annotatedObject, new)
    }
    override fun setJsonValue(new: Any) = set(new as Float)

    override fun getDefault(): Float = default
    override fun getDefaultJsonValue(): Float = default

    override fun getJsonValue(): Float {
        return getInternal()
    }
}