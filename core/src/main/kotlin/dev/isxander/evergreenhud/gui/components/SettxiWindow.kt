/*
 *
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 *
 */

package dev.isxander.evergreenhud.gui.components

import dev.isxander.evergreenhud.elements.Element
import dev.isxander.settxi.impl.BooleanSetting
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.ScaledTextConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.effect
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.effects.ScissorEffect

class SettxiWindow(val element: Element) : UIComponent() {
    val content = ScrollComponent(innerPadding = 1f).apply {
        constrain {
            width = 100.percent()
            height = 100.percent()
        }

        this childOf this@SettxiWindow
    }

    val categories = mutableMapOf<String, CategoryContainer>()

    init {
        for (setting in element.settings) {
            categories.computeIfAbsent(setting.category) { CategoryContainer(setting.category) childOf content }

            categories[setting.category]!!.addComponent(
                setting.nameSerializedKey,
                UIText(setting.name).constrain {
                    y = SiblingConstraint()
                }
            )
        }
    }

    class CategoryContainer(val name: String?) : UIComponent() {
        val nameLabel = if (name != null) UIText(name).constrain { y = SiblingConstraint() } childOf this else null
        val components = mutableMapOf<String, UIComponent>()

        init {
            constrain {
                y = SiblingConstraint()
                width = ChildBasedMaxSizeConstraint()
                height = ChildBasedMaxSizeConstraint()
            }
        }

        fun addComponent(settingKey: String, component: UIComponent) {
            component childOf this
            components[settingKey] = component
        }
    }
}
