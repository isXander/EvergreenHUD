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
import dev.isxander.evergreenhud.utils.capitalizeEnum
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.hypixel.isHypixel
import dev.isxander.evergreenhud.utils.hypixel.locraw.GameType
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.string

@ElementMeta(id = "evergreenhud:hypixel_game", name = "Hypixel Game", description = "Display the current game you are playing.", category = "Hypixel")
class ElementHypixelGame : SimpleTextElement("Game") {
    var noHypixelMessage by string("None") {
        name = "No Hypixel Message"
        description = "Text to display when you are not in Hypixel."
        category = "Hypixel Game"
    }

    override fun calculateValue(): String {
        if (!isHypixel) return noHypixelMessage

        val location = EvergreenHUD.locrawManager.currentLocation
        val gameType = location.gameType
        var name = gameType.friendlyName

        if (gameType !in setOf(GameType.UNKNOWN, GameType.LIMBO, GameType.MAIN) && location.isLobby)
            name += " Lobby"

        return name
    }
}
