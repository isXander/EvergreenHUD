/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils.hypixel.locraw

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class LocationParsed(
    val server: String = "unknown",
    @SerialName("gametype")
    val gameType: GameType = GameType.UNKNOWN,
    val mode: String? = null,
    val map: String? = null,
    @SerialName("lobbyname")
    val lobbyName: String? = null
) {
    @Transient
    var isLobby = mode == null || map == null

    override fun toString(): String {
        return "LocationParsed(server='$server', gameType=$gameType, mode=$mode, map=$map, lobbyName=$lobbyName, isLobby=$isLobby)"
    }

    companion object {
        val UNKNOWN = LocationParsed()
    }
}
