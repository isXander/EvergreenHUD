package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.repo.ReleaseChannel
import dev.isxander.evergreenhud.settings.JsonValues
import dev.isxander.evergreenhud.settings.Setting
import java.lang.reflect.Field

@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class EnumSetting(val name: String, val category: String, val description: String, val save: Boolean = true)

class EnumSettingWrapped(annotation: EnumSetting, annotationObject: Any, annotatedField: Field) : Setting<Int, EnumSetting>(annotation, annotationObject, annotatedField, JsonValues.INT) {

    private val default = getInternal()

    override fun getName(): String = annotation.name
    override fun getCategory(): String = annotation.category
    override fun getDescription(): String = annotation.description
    override fun shouldSave(): Boolean = annotation.save

    override fun getInternal(): Int {
        return getEnum().ordinal
    }

    fun getEnum(): Enum<*> {
        return annotatedField.get(annotatedObject) as Enum<*>
    }

    override fun setInternal(new: Int) {
        TODO("idk how yet")
        //annotatedField.set(annotatedObject, getEnum().values()[new])
    }

    override fun setJsonValue(new: Any) {
        setInternal(new as Int)
    }

    override fun getJsonValue(): Any {
        return getInternal()
    }

    override fun getDefault(): Int {
        return default
    }

    override fun getDefaultJsonValue(): Any {
        return getDefault()
    }

}