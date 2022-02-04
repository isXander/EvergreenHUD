/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.gui.screens.ui.components

import dev.isxander.evergreenhud.gui.screens.ui.EvergreenPalette
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.constraint
import dev.isxander.evergreenhud.utils.plus
import gg.essential.elementa.components.GradientComponent
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import net.minecraft.util.Formatting

class CategoryLabel(val category: String, selected: Boolean) : UIText(Formatting.BOLD + category, false) {
    init {
        constrain {
            color = Color.white.withAlpha(if (selected) 1f else 0.7f).constraint
        }
    }

    val underline by GradientComponent(EvergreenPalette.Evergreen.Evergreen3.awt, Color(0x00C142).awt, GradientComponent.GradientDirection.LEFT_TO_RIGHT).constrain {
        x = CenterConstraint()
        if (selected) width = 100.percent()
        y = 100.percent() - 5.percent()
        height = 5.percent()
    } childOf this

    var selected = selected
        set(value) {
            field = value
            underline.animate {
                setWidthAnimation(Animations.IN_OUT_SIN, 0.3f, if (selected) 100.percent() else 0.percent())
            }
            this.animate {
                setColorAnimation(Animations.IN_OUT_SIN, 0.3f, Color.white.withAlpha(if (selected) 1f else 0.7f).constraint)
            }
        }
}
