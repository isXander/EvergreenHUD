/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.api.mc
import dev.isxander.evergreenhud.elements.ElementMeta
import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.Facing
import dev.isxander.settxi.impl.boolean

@ElementMeta(id = "DIRECTION", name = "Direction", category = "Player", description = "Which way the player is facing.")
class ElementDirection : SimpleTextElement() {
    var abbreviated by boolean(
        default = false,
        name = "Abbreviated",
        category = "Direction",
        description = "Make the name of the direction shorter."
    )

    override var title = "Direction"

    override fun calculateValue(): String {
        if (mc.player.equals(null)) return "Unknown"

        val facing = Facing.parse(mc.player.yaw)
        return if (abbreviated) facing.abbreviated else facing.full
    }
}