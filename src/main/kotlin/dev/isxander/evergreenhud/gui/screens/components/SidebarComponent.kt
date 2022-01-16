/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.gui.screens.components

import dev.isxander.evergreenhud.gui.screens.effects.RotationEffect
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import java.awt.Color

class SidebarComponent : UIComponent() {

    private val container = UIContainer().constrain {
        width = RelativeConstraint()
        height = RelativeConstraint()
    } childOf this
    val background = UIBlock(Color(21, 21, 21, 190)).constrain {
        width = RelativeConstraint()
        height = RelativeConstraint()
    } childOf container
    val navigation = NavigationComponent(this) childOf container
    val expandArrow = ArrowComponent().constrain {
        x = 3.pixels()
        y = 3.pixels()
        width = 32.pixels()
        height = 32.pixels()
    }.onMouseClick {
        expand()
    } childOf this

    init {
        expandArrow.hide()
        constrain {
            width = 250.pixels()
            height = RelativeConstraint()
        }
    }

    fun expand() {
        expandArrow.hide()
        container.animate {
            setXAnimation(Animations.OUT_CUBIC, 1f, 0.pixels())
        }
    }

    fun collapse() {
        container.animate {
            setXAnimation(Animations.OUT_CUBIC, 1f, 0.pixels(alignOutside = true))
            onComplete {
                expandArrow.unhide(true)
            }
        }
    }

    inner class NavigationComponent(
        sidebar: SidebarComponent
    ) : UIComponent() {
        private val collapseArrow = ArrowComponent().constrain {
            x = 3.pixels()
            y = 3.pixels()
            width = 32.pixels()
            height = 32.pixels()
        }.onMouseClick {
            sidebar.collapse()
        } effect RotationEffect(180f) childOf this
        private val divider = UIBlock().constrain {
            y = 0.pixels(true)
            width = RelativeConstraint()
            height = 2.pixels()
        } childOf this

        init {
            constrain {
                width = RelativeConstraint()
                height = ChildBasedSizeConstraint()
            }
        }
    }
}
