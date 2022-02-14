/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui

import dev.isxander.evergreenhud.ui.components.CategoryLabel
import dev.isxander.evergreenhud.ui.components.settings.SettingComponent
import dev.isxander.evergreenhud.utils.constraint
import dev.isxander.settxi.serialization.ConfigProcessor
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*

class ConfigUI(val config: ConfigProcessor) : MainUI() {
    val categoryList = mutableListOf<CategoryLabel>()

    val categoryContainer by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = ChildBasedSizeConstraint()
        height = 25.percent()
    } childOf lowerHeader

    val scrollContainer by UIContainer().constrain {
        width = 100.percent()
        height = FillConstraint()
    } childOf paddedMainContent

    val settingScroller by ScrollComponent(pixelsPerScroll = 25f).constrain {
        width = 100.percent()
        height = 100.percent()
    } childOf scrollContainer

    val settingsScrollbarContainer by UIBlock(EvergreenPalette.Greyscale.Dark3.awt).constrain {
        x = 98.percent()
        width = 2.percent()
    } childOf scrollContainer

    val settingScrollbar by UIBlock(EvergreenPalette.Greyscale.Gray1.awt).constrain {
        width = 100.percent()
    } childOf settingsScrollbarContainer

    init {
        settingScroller.setVerticalScrollBarComponent(settingScrollbar)
    }

    val settingsList = mutableListOf<SettingComponent>()

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
                categoryList.forEach {
                    it.deselect()
                }

                label.select()
                setCurrentCategory(category)
            }
            label childOf categoryContainer
            categoryList.add(label)
        }

        setCurrentCategory(categories.first())

        searchField.onKeyType { _, _ ->
            settingUpdatedCallback()
        }
    }

    lateinit var category: String
        private set

    private fun setCurrentCategory(category: String) {
        settingsList.forEach {
            settingScroller.removeChild(it)
        }
        settingsList.clear()

        val settings = config.settings
            .filter { it.category == category }
            .filter { !it.hidden }
            .filter { searchField.text.getText() == searchField.text.placeholder || it.name.contains(searchField.text.getText()) }

        for (setting in settings) {
            val settingComponent by SettingComponent(setting) { settingUpdatedCallback() }
            settingComponent childOf settingScroller
            settingsList.add(settingComponent)
        }

        this.category = category
    }

    private fun settingUpdatedCallback() {
        setCurrentCategory(category)
    }
}
