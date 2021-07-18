package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.settings.JsonType
import dev.isxander.evergreenhud.settings.Setting
import gg.essential.elementa.UIComponent
import java.lang.reflect.Field

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class BooleanSetting(val name: String, val category: Array<String>, val description: String, val save: Boolean = true)

class BooleanSettingWrapped(annotation: BooleanSetting, annotatedObject: Any, annotatedField: Field) : Setting<Boolean, BooleanSetting>(annotation, annotatedObject, annotatedField, JsonType.BOOLEAN) {
    override val name: String = annotation.name
    override val category: Array<String> = annotation.category
    override val description: String = annotation.description
    override val shouldSave: Boolean = annotation.save

    override fun getInternal(): Boolean = annotatedField.getBoolean(annotatedObject)
    override fun setInternal(new: Boolean) = annotatedField.setBoolean(annotatedObject, new)

    override var jsonValue: Any
        get() = value
        set(new) {
            value = new as Boolean
        }

    override val defaultJsonValue: Any = default

    override fun addComponentToUI(parent: UIComponent) {
        TODO("Not yet implemented")
    }
}

