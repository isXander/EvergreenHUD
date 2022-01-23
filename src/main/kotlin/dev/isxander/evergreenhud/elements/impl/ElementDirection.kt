/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.Facing
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.boolean

@ElementMeta(id = "evergreenhud:direction", name = "Direction", category = "Player", description = "Which way the player is facing.")
class ElementDirection : SimpleTextElement("Direction") {
    var abbreviated by boolean(false) {
        name = "Abbreviated"
        category = "Direction"
        description = "Make the name of the direction shorter."
    }

    override fun calculateValue(): String {
        if (mc.player == null) return "Unknown"

        val facing = Facing.parse(mc.player!!.yaw)
        return if (abbreviated) facing.abbreviated else facing.full
    }
}
