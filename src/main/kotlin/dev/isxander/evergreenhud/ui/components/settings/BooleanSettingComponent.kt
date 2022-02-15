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
import dev.isxander.settxi.impl.BooleanSetting
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.constraints.AspectConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect

class BooleanSettingComponent(val component: SettingComponent, val setting: BooleanSetting) : UIBlock(EvergreenPalette.Greyscale.Dark2.awt) {
    init {
        constrain {
            width = AspectConstraint(2.2f)
        } effect OutlineEffect(Color.black.awt, 2f, drawInsideChildren = true)

        onMouseClick { toggle() }
    }

    val toggle by UIBlock().constrain {
        x = if (setting.get()) 60.percent() else 0.percent()
        width = 40.percent()
        height = 100.percent()
        color = if (setting.get())
            EvergreenPalette.Evergreen.Evergreen3.constraint
        else
            Color(0xD32828, false).constraint
    } effect OutlineEffect(Color.black.withAlpha(0.4f).awt, 1f, drawInsideChildren = true) childOf this

    fun toggle() {
        setting.set(!setting.get())

        toggle.animate {
            if (setting.get()) {
                setXAnimation(Animations.IN_OUT_SIN, 0.3f, 60.percent())
                setColorAnimation(Animations.IN_OUT_SIN, 0.3f, EvergreenPalette.Evergreen.Evergreen3.constraint)
            } else {
                setXAnimation(Animations.IN_OUT_SIN, 0.3f, 0.percent())
                setColorAnimation(Animations.IN_OUT_SIN, 0.3f, Color(0xD32828, false).constraint)
            }

            onComplete {
                component.onChange()
            }
        }
    }
}
