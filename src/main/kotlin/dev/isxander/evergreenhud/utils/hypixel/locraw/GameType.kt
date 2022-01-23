/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils.hypixel.locraw

import kotlinx.serialization.Serializable

@Serializable
enum class GameType(val friendlyName: String) {
    BEDWARS("Bedwars"),
    SKYBLOCK("Skyblock"),
    PROTOTYPE("Prototype"),
    ARCADE("Arcade"),
    PIT("The Pit"),
    MAIN("Main Lobby"),
    SKYWARS("Skywars"),
    MURDER_MYSTERY("Murder Mystery"),
    HOUSING("Housing"),
    BUILD_BATTLE("Build Battle"),
    DUELS("Duels"),
    UHC("UHC Champions"),
    TNTGAMES("TNT Games"),
    LEGACY("Classic Games"),
    MCGO("Cops and Crims"),
    SURVIVAL_GAMES("Blitz SG"),
    WALLS3("Mega Walls"),
    SUPER_SMASH("Smash Heroes"),
    BATTLEGROUNDS("Warlords"),
    LIMBO("Limbo"),
    SMP("SMP"),
    TOURNAMENT("Tournament Hall"),
    UNKNOWN("Unknown");

    companion object {
        fun getType(name: String?): GameType {
            return try {
                valueOf(name!!)
            } catch (e: IllegalArgumentException) {
                UNKNOWN
            }
        }
    }
}
