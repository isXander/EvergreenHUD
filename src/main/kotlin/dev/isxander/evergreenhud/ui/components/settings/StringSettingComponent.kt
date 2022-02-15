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
import dev.isxander.settxi.impl.StringSetting
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.AspectConstraint
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect

class StringSettingComponent(val component: SettingComponent, val setting: StringSetting) : UIBlock(EvergreenPalette.Greyscale.Dark2.awt) {
    init {
        constrain {
            width = AspectConstraint(10.4f)
        } effect OutlineEffect(Color.black.awt, 1f)

        onMouseClick { input.grabWindowFocus() }
    }

    val input by UITextInput(shadow = false).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 95.percent()
        height = 75.percent()
        textScale = (0.8).pixels()
    } childOf this

    init {
        input.onKeyType { _, _ ->
            setting.set(input.getText())
        }

        input.setText(setting.get())
    }
}
