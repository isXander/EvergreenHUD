/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.gui.screens.ui

import dev.isxander.evergreenhud.gui.screens.ui.components.CategoryLabel
import dev.isxander.settxi.serialization.ConfigProcessor
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.provideDelegate
import net.minecraft.client.gui.screen.Screen

class ConfigUI(val config: ConfigProcessor, parent: Screen? = null) : MainUI(parent) {
    val categoryList = mutableListOf<CategoryLabel>()

    val categoryContainer by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = ChildBasedSizeConstraint()
        height = 25.percent()
    } childOf lowerHeader

    init {
        // get all categories and remove duplicates
        val categories = config.settings.map { it.category }.toSet()
        for (i in categories.indices) {
            val category = categories.elementAt(i)

            val label = CategoryLabel(category, i == 0).constrain {
                x = SiblingConstraint(20f)
                y = CenterConstraint()
                width = TextAspectConstraint()
                height = 100.percent()
            }
            label.onMouseClick {
                categoryList.forEach { it.selected = false }
                label.selected = true
            }
            label childOf categoryContainer
            categoryList.add(label)
        }
    }
}
