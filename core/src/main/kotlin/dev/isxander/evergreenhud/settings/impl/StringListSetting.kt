package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.settings.JsonValues
import dev.isxander.evergreenhud.settings.Setting
import java.lang.reflect.Field

@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class StringListSetting(val name: String, val category: Array<String>, val description: String, val defaultIndex: Int, val save: Boolean = true)

@Suppress("UNCHECKED_CAST")
class StringListSettingWrapped(annotation: StringListSetting, annotationObject: Any, annotatedField: Field) : Setting<Int, StringListSetting>(annotation, annotationObject, annotatedField, JsonValues.INT) {
    private val options: List<String> = annotatedField.get(annotationObject) as List<String>
    private var index: Int = annotation.defaultIndex
    private val defaultIndex: Int = index

    override fun getName(): String = annotation.name
    override fun getCategory(): Array<String> = annotation.category
    override fun getDescription(): String = annotation.description
    override fun shouldSave(): Boolean = annotation.save

    override fun getInternal(): Int {
        return index
    }
    fun getString(): String {
        return options[index]
    }

    override fun setInternal(new: Int) {
        index = new
    }
    override fun setJsonValue(new: Any) = set(new as Int)
    fun setString(new: String) {
        index = options.indexOf(new)
    }

    override fun getDefault(): Int = defaultIndex
    override fun getDefaultJsonValue(): Int = defaultIndex

    override fun getJsonValue(): Int {
        return get()
    }
}