package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.settings.JsonType
import dev.isxander.evergreenhud.settings.Setting
import gg.essential.elementa.UIComponent
import java.lang.reflect.Field

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class OptionSetting(val name: String, val category: Array<String>, val description: String, val save: Boolean = true)

class OptionSettingWrapped(annotation: OptionSetting, annotationObject: Any, annotatedField: Field) : Setting<Int, OptionSetting>(annotation, annotationObject, annotatedField, JsonType.INT) {
    override val name: String = annotation.name
    override val category: Array<String> = annotation.category
    override val description: String = annotation.description
    override val shouldSave: Boolean = annotation.save

    fun getOption(): OptionContainer.Option = annotatedField.get(annotatedObject) as OptionContainer.Option

    override fun getInternal(): Int = getOption().ordinal
    override fun setInternal(new: Int) = annotatedField.set(annotatedObject, getOption().values[new])

    override var jsonValue: Any
        get() = value
        set(new) { value = new as Int }

    override val defaultJsonValue: Any = default

    override fun addComponentToUI(parent: UIComponent) {
        TODO("Not yet implemented")
    }

}

abstract class OptionContainer {

    val options = arrayListOf<Option>()
    fun option(name: String, description: String? = null): Option = Option(this, name, description).also { options.add(it) }

    data class Option(private val container: OptionContainer, val name: String, val description: String?) {
        val ordinal = container.options.indexOf(this)
        val values get() = container.options
    }

}