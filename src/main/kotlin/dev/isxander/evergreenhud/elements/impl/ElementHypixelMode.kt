/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.formatEnum
import dev.isxander.evergreenhud.utils.hypixel.isHypixel
import dev.isxander.settxi.impl.string

@ElementMeta(id = "evergreenhud:hypixel_mode", name = "Hypixel Mode", description = "Display the current mode of the game you are playing.", category = "Hypixel")
class ElementHypixelMode : SimpleTextElement("Mode") {
    var noHypixelMessage by string("None") {
        name = "No Hypixel Message"
        description = "Text to display when you are not in Hypixel."
        category = "Hypixel Game"
    }

    var inLobbyMessage by string("None") {
        name = "In Lobby Message"
        description = "Text to display when you are in a Hypixel lobby."
        category = "Hypixel Game"
    }

    override fun calculateValue(): String {
        if (!isHypixel) return noHypixelMessage

        val location = EvergreenHUD.locrawManager.currentLocation
        val mode = location.mode

        if (mode == null || location.isLobby)
            return inLobbyMessage

        return mode.formatEnum()
    }
}

