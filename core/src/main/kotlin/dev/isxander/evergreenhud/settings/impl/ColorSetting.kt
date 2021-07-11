package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.settings.JsonValues
import dev.isxander.evergreenhud.settings.Setting
import java.lang.reflect.Field

@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class ColorSetting(val name: String, val category: String, val description: String, val save: Boolean = true)

class ColorSettingWrapped(annotation: ColorSetting, annotatedObject: Any, annotatedField: Field) : Setting<Int, ColorSetting>(annotation, annotatedObject, annotatedField, JsonValues.BOOLEAN) {
    private val default: Int = getInternal()

    override fun getName(): String = annotation.name
    override fun getCategory(): String = annotation.category
    override fun getDescription(): String = annotation.description
    override fun shouldSave(): Boolean = annotation.save

    override fun getInternal(): Int {
        return annotatedField.getInt(annotatedObject)
    }

    override fun setInternal(new: Int) {
        annotatedField.setInt(annotatedObject, new)
    }
    override fun setJsonValue(new: Any) = set(new as Int)

    override fun getJsonValue(): Int {
        return getInternal()
    }

    override fun getDefault(): Int = default
    override fun getDefaultJsonValue(): Int = default
}
