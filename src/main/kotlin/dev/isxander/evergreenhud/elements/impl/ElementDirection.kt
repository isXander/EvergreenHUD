/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.annotations.ElementMeta
import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.Facing
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.boolean

@ElementMeta(id = "DIRECTION", name = "Direction", category = "Player", description = "Which way the player is facing.")
class ElementDirection : SimpleTextElement() {
    var abbreviated by boolean(
        default = false,
        name = "Abbreviated",
        category = "Direction",
        description = "Make the name of the direction shorter."
    )

    init {
        title = "Direction"
    }

    override fun calculateValue(): String {
        if (mc.player == null) return "Unknown"

        val facing = Facing.parse(mc.player!!.yaw)
        return if (abbreviated) facing.abbreviated else facing.full
    }
}
