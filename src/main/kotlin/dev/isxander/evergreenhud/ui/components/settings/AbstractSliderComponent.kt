/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui.components.settings

import dev.isxander.evergreenhud.ui.EvergreenPalette
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.constraint
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.AspectConstraint
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.TextAspectConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect

abstract class AbstractSliderComponent(var percentage: Float, private val current: () -> String) : UIContainer() {
    private var draggingOffset = 0f
    private var mouseDown = false
    private var valueChanged: (Float) -> Unit = {}

    init {
        constrain {
            width = AspectConstraint(10.4f) + (AspectConstraint(10.4f) * 0.04)
        }
    }

    val sliderTrack by UIBlock(EvergreenPalette.Greyscale.Dark2.awt).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 100.percent() - 4.percent()
        height = 50.percent()
    } effect OutlineEffect(Color.black.withAlpha(0.4f).awt, 1f, drawInsideChildren = true) childOf this

    val slider by UIBlock(EvergreenPalette.Evergreen.Evergreen3.awt).constrain {
        x = basicXConstraint {
            (sliderTrack.getLeft() + sliderTrack.getWidth() * percentage) - it.getWidth() / 2
        }
        y = CenterConstraint()
        width = 4.percent() boundTo sliderTrack
        height = 100.percent()
    } effect OutlineEffect(Color.black.withAlpha(0.4f).awt, 1f, drawInsideChildren = true) childOf this

    val currentText by UIText(current(), false).constrain {
        x = 0.pixels(alignOpposite = true) - 105.percent()
        y = CenterConstraint()
        width = TextAspectConstraint()
        height = 40.percent()
    } childOf this

    init {
        slider.apply {
            onMouseClick {
                mouseDown = true
                draggingOffset = it.relativeX - slider.getWidth() / 2
                it.stopPropagation()
            }

            onMouseRelease {
                mouseDown = false
                draggingOffset = 0f
            }

            onMouseDrag { mouseX, _, _ ->
                if (!mouseDown) return@onMouseDrag

                val clamped = (mouseX + slider.getLeft() - draggingOffset).coerceIn(sliderTrack.getLeft()..sliderTrack.getRight())
                percentage = ((clamped - sliderTrack.getLeft()) / sliderTrack.getWidth()).coerceIn(0f..1f)
                valueChanged(percentage)

                currentText.setText(current())
            }
        }
    }

    fun valueChanged(listener: (Float) -> Unit) {
        valueChanged = listener
    }
}
