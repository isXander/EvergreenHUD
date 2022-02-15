/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui.components.settings

import dev.isxander.evergreenhud.utils.percentage
import dev.isxander.settxi.impl.FloatSetting
import java.text.DecimalFormat

class FloatSettingComponent(val component: SettingComponent, val setting: FloatSetting) : AbstractSliderComponent(setting.percentage, { formatter.format(setting.get()) }) {
    init {
        valueChanged {
            setting.percentage = it
        }
    }

    companion object {
        private val formatter = DecimalFormat("#.##")
    }
}
