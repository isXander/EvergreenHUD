/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.ui.components.CategoryLabel
import dev.isxander.evergreenhud.ui.components.ElementDescriptionComponent
import dev.isxander.evergreenhud.utils.mc
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*

class AddElementUI : MainUI() {
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

    val elementScroller by ScrollComponent(pixelsPerScroll = 25f).constrain {
        width = 100.percent()
        height = 100.percent()
    } childOf scrollContainer

    val elementsScrollbarContainer by UIBlock(EvergreenPalette.Greyscale.Dark3.awt).constrain {
        x = 98.percent()
        width = 2.percent()
    } childOf scrollContainer

    val elementScrollbar by UIBlock(EvergreenPalette.Greyscale.Gray1.awt).constrain {
        width = 100.percent()
    } childOf elementsScrollbarContainer

    init {
        elementScroller.setVerticalScrollBarComponent(elementScrollbar)
    }

    val elementList = mutableListOf<ElementDescriptionComponent>()

    init {
        // get all categories and remove duplicates
        val categories = EvergreenHUD.elementManager.availableElements.values.map { it.category }.toSet()
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
        elementList.forEach {
            it.parent.hide()
        }
        elementList.clear()

        val elements = EvergreenHUD.elementManager.availableElements.values
            .filter { it.category == category }
            .filter { searchField.text.getText() == searchField.text.placeholder || it.name.contains(searchField.text.getText()) }

        for ((i, element) in elements.withIndex()) {
            val left = i % 2 == 0

            val container = if (left)
                UIContainer().constrain {
                    y = SiblingConstraint(10f)
                    width = 100.percent()
                    height = 15.percent()
                } childOf elementScroller
            else elementList.last().parent

            val elementComponent by ElementDescriptionComponent(element).constrain {
                if (i % 2 == 1)
                    x = 50.percent() + 5.pixels()
                width = 50.percent() - 5.pixels()
                height = 100.percent()
            }
            elementComponent.onMouseClick {
                mc.displayGuiScreen(ElementDisplay())
            }
            elementComponent childOf container
            elementList.add(elementComponent)
        }

        this.category = category
    }

    private fun settingUpdatedCallback() {
        setCurrentCategory(category)
    }
}
