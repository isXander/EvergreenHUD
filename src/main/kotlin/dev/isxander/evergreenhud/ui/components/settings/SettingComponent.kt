/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui.components.settings


import dev.isxander.evergreenhud.settings.ColorSetting
import dev.isxander.evergreenhud.ui.EvergreenPalette
import dev.isxander.evergreenhud.utils.constraint
import dev.isxander.settxi.Setting
import dev.isxander.settxi.impl.*
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.constraints.TextAspectConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect

class SettingComponent(val setting: Setting<*>, val settingList: UIComponent, val onChange: () -> Unit = {}) : UIBlock(
    EvergreenPalette.Greyscale.Dark3.constraint) {
    init {
        constrain {
            y = SiblingConstraint()
            width = 100.percent()
            height = 15.percent()
        }

        this effect OutlineEffect(EvergreenPalette.Greyscale.Dark1.awt, 1f)
    }

    val leftPadded by UIContainer().constrain {
        x = 2.percent()
        y = CenterConstraint()
        width = 45.percent()
        height = ChildBasedSizeConstraint()
    } childOf this

    val titleText by UIText(setting.name, false).constrain {
        width = TextAspectConstraint()
        height = 40.percent() boundTo this@SettingComponent
    } childOf leftPadded

    val descriptionText by UIText(setting.description, false).constrain {
        y = SiblingConstraint(3f)
        width = TextAspectConstraint()
        height = 20.percent() boundTo this@SettingComponent
    } childOf leftPadded

    val rightPadded by UIContainer().constrain {
        x = (100 - 2 - 45).percent()
        y = CenterConstraint()
        width = 45.percent()
        height = 95.percent()
    } childOf this

    val settingComponent by getSettingComponent(setting).constrain {
        x = 0.pixels(alignOpposite = true)
        y = CenterConstraint()
        height = 50.percent()
    } childOf rightPadded

    private fun getSettingComponent(setting: Setting<*>): UIComponent {
        return when (setting) {
            is BooleanSetting -> BooleanSettingComponent(this, setting)
            is StringSetting -> StringSettingComponent(this, setting)
            is OptionSetting -> OptionSettingComponent(this, setting)
            is FloatSetting -> FloatSettingComponent(this, setting)
            is IntSetting -> IntSettingComponent(this, setting)
            is LongSetting -> LongSettingComponent(this, setting)
            is ColorSetting -> ColorSettingComponent(this, setting)
            else -> UIContainer()
        }
    }
}
