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
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.ScissorEffect
import java.awt.Color

class SettxiWindow(val element: Element) : UIComponent() {
    val background by UIBlock(Color(0x373737)).apply {
        constrain {
            width = 100.percent()
            height = 100.percent()
        }

        childOf(this@SettxiWindow)
    }

    val scrollContainer by UIContainer().apply {
        constrain {
            x = 5.pixels()
            y = SiblingConstraint() + 7.pixels()
            width = RelativeConstraint(1f) - 10.pixels()
            height = FillConstraint()
        }

        childOf(background)
    }

    val categoryScroller by ScrollComponent().apply {
        constrain {
            width = 100.percent()
            height = 100.percent()
        }

        childOf(scrollContainer)
    }

    val categories = mutableMapOf<String, CategoryContainer>()

    init {

        for (setting in element.settings) {
            categories.computeIfAbsent(setting.category) { CategoryContainer(setting.category) childOf categoryScroller }

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
