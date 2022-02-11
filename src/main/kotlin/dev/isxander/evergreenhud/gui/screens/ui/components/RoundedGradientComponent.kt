/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.gui.screens.ui.components

import gg.essential.elementa.components.GradientComponent
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.ScissorEffect
import java.awt.Color

class RoundedGradientComponent(
    startColor: Color = Color.WHITE,
    endColor: Color = Color.WHITE,
    direction: GradientComponent.GradientDirection = GradientComponent.GradientDirection.TOP_TO_BOTTOM,
    radius: Float,
) : UIRoundedRectangle(radius) {
    init {
        this effect ScissorEffect()
    }

    val gradient by GradientComponent(startColor, endColor, direction).constrain {
        width = 100.percent()
        height = 100.percent()
    } childOf this
}
