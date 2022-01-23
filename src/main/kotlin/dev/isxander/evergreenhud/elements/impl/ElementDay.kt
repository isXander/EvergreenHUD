/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc

@ElementMeta(id = "evergreenhud:day", name = "Day Counter", description = "Displays the current day in the world.", category = "World")
class ElementDay : SimpleTextElement("Day") {
    override fun calculateValue(): String {
        if (mc.world == null) return "0"
        return (mc.world!!.timeOfDay / 24000L).toString()
    }
}
