package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.settings.JsonValues
import dev.isxander.evergreenhud.settings.Setting
import java.lang.reflect.Field

@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class StringSetting(val name: String, val category: Array<String>, val description: String, val save: Boolean = true)

class StringSettingWrapped(annotation: StringSetting, annotationObject: Any, annotatedField: Field) : Setting<String, StringSetting>(annotation, annotationObject, annotatedField, JsonValues.STRING) {
    private val defaultString: String = get()

    override fun getName(): String = annotation.name
    override fun getCategory(): Array<String> = annotation.category
    override fun getDescription(): String = annotation.description
    override fun shouldSave(): Boolean = annotation.save

    override fun getInternal(): String {
        return annotatedField.get(annotatedObject) as String
    }

    override fun setInternal(new: String) {
        annotatedField.set(annotatedObject, new)
    }
    override fun setJsonValue(new: Any) = set(new as String)

    override fun getJsonValue(): String {
        return getInternal()
    }

    override fun getDefault(): String = defaultString
    override fun getDefaultJsonValue(): String = get()

}