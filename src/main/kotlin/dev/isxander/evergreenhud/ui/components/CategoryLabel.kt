/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui.components

import dev.isxander.evergreenhud.ui.EvergreenPalette
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.constraint
import gg.essential.elementa.components.GradientComponent
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.universal.ChatColor

class CategoryLabel(val category: String, selected: Boolean) : UIText(ChatColor.BOLD + category, false) {
    var isSelected = selected
        private set

    init {
        constrain {
            color = if (isSelected)
                Color.white.constraint
            else
                Color(0xB4B4B4, false).constraint
        }

        onMouseEnter {
            if (!isSelected) {
                this.animate {
                    setColorAnimation(Animations.IN_OUT_SIN, 0.3f, Color.white.constraint)
                }
            }
        }

        onMouseLeave {
            if (!isSelected) {
                this.animate {
                    setColorAnimation(Animations.IN_OUT_SIN, 0.3f, Color(0xB4B4B4, false).constraint)
                }
            }
        }
    }

    val underline by GradientComponent(EvergreenPalette.Evergreen.Evergreen3.awt, Color(0x00C142, false).awt, GradientComponent.GradientDirection.LEFT_TO_RIGHT).constrain {
        x = CenterConstraint()
        if (isSelected) width = 100.percent()
        y = 100.percent() + 5.percent()
        height = 20.percent()
    } childOf this

    fun select() {
        if (isSelected) return
        isSelected = true

        underline.animate {
            setWidthAnimation(Animations.IN_OUT_SIN, 0.3f, 100.percent())
        }
        this.animate {
            setColorAnimation(Animations.IN_OUT_SIN, 0.3f, Color.white.constraint)
        }
    }

    fun deselect() {
        if (!isSelected) return
        isSelected = false

        underline.animate {
            setWidthAnimation(Animations.IN_OUT_SIN, 0.3f, 0.percent())
        }
        this.animate {
            setColorAnimation(Animations.IN_OUT_SIN, 0.3f, Color(0xB4B4B4, false).constraint)
        }
    }
}
