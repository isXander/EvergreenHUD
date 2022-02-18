/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.decimalFormat
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc

@ElementMeta(id = "evergreenhud:saturation", name = "Saturation", category = "Player", description = "Show how long until your hunger starts to go down.")
class ElementSaturation : SimpleTextElement("Saturation") {
    override fun calculateValue(): String {
        return decimalFormat(1, true).format(mc.player?.hungerManager?.saturationLevel ?: 20f)
    }
}
