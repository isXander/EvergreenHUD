package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.settings.JsonValues
import dev.isxander.evergreenhud.settings.Setting
import java.lang.IllegalArgumentException
import java.lang.reflect.Field

@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class IntSetting(val name: String, val category: Array<String>, val description: String, val min: Int, val max: Int, val suffix: String = "", val save: Boolean = true)

class IntSettingWrapped(annotation: IntSetting, annotationObject: Any, annotatedField: Field) : Setting<Int, IntSetting>(annotation, annotationObject, annotatedField, JsonValues.INT) {
    private val default: Int = get()

    override fun getName(): String = annotation.name
    override fun getCategory(): Array<String> = annotation.category
    override fun getDescription(): String = annotation.description
    override fun shouldSave(): Boolean = annotation.save

    override fun getInternal(): Int {
        return annotatedField.getInt(annotatedObject)
    }

    override fun setInternal(new: Int) {
        annotatedField.setInt(annotatedObject, new)
    }
    override fun setJsonValue(new: Any) = set(new as Int)

    override fun getDefault(): Int = default
    override fun getDefaultJsonValue(): Int = default

    override fun getJsonValue(): Int {
        return get()
    }
}