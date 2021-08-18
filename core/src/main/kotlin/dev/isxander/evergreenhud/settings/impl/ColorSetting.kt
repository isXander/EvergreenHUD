/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.settings.impl

import dev.isxander.evergreenhud.settings.DataType
import dev.isxander.evergreenhud.settings.Setting
import dev.isxander.evergreenhud.settings.providers.IValueProvider
import gg.essential.elementa.UIComponent
import java.awt.Color
import kotlin.reflect.KProperty1

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class ColorSetting(val name: String, val category: String, val subcategory: String = "", val description: String, val save: Boolean = true, val transparency: Boolean = true)

class ColorSettingWrapped(annotation: ColorSetting, provider: IValueProvider<Color>) : Setting<Color, ColorSetting>(annotation, provider, DataType.INT) {
    override val name: String = annotation.name
    override val category: String = annotation.category
    override val subcategory: String = annotation.subcategory
    override val description: String = annotation.description
    override val shouldSave: Boolean = annotation.save
    val transparency: Boolean = annotation.transparency

    override var serializedValue: Any
        get() = value.rgb
        set(new) { value = Color(new as Int) }

    override val defaultSerializedValue: Any = default.rgb

    override fun addComponentToUI(parent: UIComponent) {
        TODO("Not yet implemented")
    }
}
