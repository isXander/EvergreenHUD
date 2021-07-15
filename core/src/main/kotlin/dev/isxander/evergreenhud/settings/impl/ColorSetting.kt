package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.settings.JsonValues
import dev.isxander.evergreenhud.settings.Setting
import java.awt.Color
import java.lang.reflect.Field

@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class ColorSetting(val name: String, val category: Array<String>, val description: String, val save: Boolean = true)

class ColorSettingWrapped(annotation: ColorSetting, annotatedObject: Any, annotatedField: Field) : Setting<Color, ColorSetting>(annotation, annotatedObject, annotatedField, JsonValues.INT) {
    private val default: Color = get()

    override fun getName(): String = annotation.name
    override fun getCategory(): Array<String> = annotation.category
    override fun getDescription(): String = annotation.description
    override fun shouldSave(): Boolean = annotation.save

    override fun getInternal(): Color {
        return annotatedField.get(annotatedObject) as Color
    }

    override fun setInternal(new: Color) {
        annotatedField.set(annotatedObject, new)
    }
    override fun setJsonValue(new: Any) = set(Color(new as Int))

    override fun getJsonValue(): Int = get().rgb

    override fun getDefault(): Color = default
    override fun getDefaultJsonValue(): Int = default.rgb
}
