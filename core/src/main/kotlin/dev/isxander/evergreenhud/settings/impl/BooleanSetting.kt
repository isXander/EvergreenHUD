package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.settings.JsonValues
import dev.isxander.evergreenhud.settings.Setting
import java.lang.IllegalArgumentException
import java.lang.reflect.Field

@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class BooleanSetting(val name: String, val category: String, val description: String, val save: Boolean = true)

class BooleanSettingWrapped(annotation: BooleanSetting, annotatedObject: Any, annotatedField: Field) : Setting<Boolean, BooleanSetting>(annotation, annotatedObject, annotatedField, JsonValues.BOOLEAN) {
    private val default: Boolean = getInternal()

    override fun getName(): String = annotation.name
    override fun getCategory(): String = annotation.category
    override fun getDescription(): String = annotation.description
    override fun shouldSave(): Boolean = annotation.save

    override fun getInternal(): Boolean {
        return annotatedField.getBoolean(annotatedObject)
    }

    override fun setInternal(new: Boolean) {
        annotatedField.setBoolean(annotatedObject, new)
    }
    override fun setJsonValue(new: Any) = set(new as Boolean)

    override fun getJsonValue(): Boolean {
        return getInternal()
    }

    override fun getDefault(): Boolean = default
    override fun getDefaultJsonValue(): Boolean = default
}

