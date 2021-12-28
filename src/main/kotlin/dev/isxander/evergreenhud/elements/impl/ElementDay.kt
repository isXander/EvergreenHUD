/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc

@ElementMeta(id = "DAY_COUNTER", name = "Day Counter", description = "Displays the current day in the world.", category = "World")
class ElementDay : SimpleTextElement("Day") {
    override fun calculateValue(): String {
        if (mc.world == null) return "0"
        return (mc.world!!.timeOfDay / 24000L).toString()
    }
}
