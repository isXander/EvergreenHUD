package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.settings.JsonType
import dev.isxander.evergreenhud.settings.Setting
import gg.essential.elementa.UIComponent
import java.awt.Color
import java.lang.reflect.Field

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class ColorSetting(val name: String, val category: Array<String>, val description: String, val save: Boolean = true, val transparency: Boolean = true)

class ColorSettingWrapped(annotation: ColorSetting, annotatedObject: Any, annotatedField: Field) : Setting<Color, ColorSetting>(annotation, annotatedObject, annotatedField, JsonType.INT) {
    override val name: String = annotation.name
    override val category: Array<String> = annotation.category
    override val description: String = annotation.description
    override val shouldSave: Boolean = annotation.save
    val transparency: Boolean = annotation.transparency

    override fun getInternal(): Color = annotatedField.get(annotatedObject) as Color
    override fun setInternal(new: Color) = annotatedField.set(annotatedObject, new)

    override var jsonValue: Any
        get() = value.red
        set(new) { value = Color(new as Int) }

    override val defaultJsonValue: Any = default.rgb

    override fun addComponentToUI(parent: UIComponent) {
        TODO("Not yet implemented")
    }
}
