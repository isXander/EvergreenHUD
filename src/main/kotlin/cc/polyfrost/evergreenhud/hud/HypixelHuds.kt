package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Exclude
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Text
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.SingleTextHud
import cc.polyfrost.oneconfig.utils.NetworkUtils
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo
import java.util.*
import kotlin.collections.HashMap

class HypixelHuds: Config(Mod("Hypixel HUDs", ModType.HUD), "evergreenhud/hypixelhuds.json") {
    @HUD(
        name = "Game Mode",
        category = "Game Mode"
    )
    var gamemode = GameModeHud()

    @HUD(
        name = "Game Type",
        category = "Game Type"
    )
    var gametype = GameTypeHud()

    @HUD(
        name = "Game Map",
        category = "Game Map"
    )
    var gamemap = GameMapHud()

    @HUD(
        name = "Height Limit",
        category = "Height Limit"
    )
    var heightlimit = HeightLimitHud()

    init {
        initialize()
    }

    class GameModeHud: SingleTextHud("Mode", false) {
        @Text(
            name = "No Hypixel Message"
        )
        var noHypixelMessage = "None"

        @Text(
            name = "In Lobby Message"
        )
        var inLobbyMessage = "None"

        override fun getText(): String {
            if (!HypixelUtils.INSTANCE.isHypixel) return noHypixelMessage

            HypixelUtils.INSTANCE.locrawInfo?.let { location ->
                val mode = location.gameMode

                if (mode == null || location.gameType == null || location.gameType == LocrawInfo.GameType.UNKNOWN || location.gameType == LocrawInfo.GameType.LIMBO)
                    return inLobbyMessage

                return mode.split("_").joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }.trimEnd()
            }
            return ""
        }

    }

    class GameMapHud: SingleTextHud("Map", false) {
        @Text(
            name = "No Hypixel Message"
        )
        var noHypixelMessage = "None"

        @Text(
            name = "In Lobby Message"
        )
        var inLobbyMessage = "None"

        override fun getText(): String {
            if (!HypixelUtils.INSTANCE.isHypixel) return noHypixelMessage

            HypixelUtils.INSTANCE.locrawInfo?.let { location ->
                val map = location.mapName

                if (map == null ||location.gameMode == null || location.gameType == null || location.gameType == LocrawInfo.GameType.UNKNOWN || location.gameType == LocrawInfo.GameType.LIMBO)
                    return inLobbyMessage

                return map
            }
            return ""
        }

    }

    class GameTypeHud: SingleTextHud("Game", false) {
        @Text(
            name = "No Hypixel Message"
        )
        var noHypixelMessage = "None"

        @Transient private val lobbyPlaces = setOf(LocrawInfo.GameType.UNKNOWN, LocrawInfo.GameType.LIMBO, LocrawInfo.GameType.MAIN)

        override fun getText(): String {
            if (!HypixelUtils.INSTANCE.isHypixel) return noHypixelMessage

            HypixelUtils.INSTANCE.locrawInfo?.let { location ->
                val gameType = location.gameType
                var name = gameType.name.split("_").joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }.trimEnd()

                if (gameType !in lobbyPlaces && (location.gameMode == null || location.gameType == null || location.gameType == LocrawInfo.GameType.UNKNOWN || location.gameType == LocrawInfo.GameType.LIMBO))
                    name += " Lobby"
                return name
            }
            return ""
        }
    }

    class HeightLimitHud: SingleTextHud("Height", false) {
        @Text(
            name = "Not Supported Text"
        )
        var notBedwarsText = "0"

        override fun getText(): String {
            if (HypixelUtils.INSTANCE.isHypixel) {
                HypixelUtils.INSTANCE.locrawInfo?.let { location ->
                    if (location.gameType == LocrawInfo.GameType.BEDWARS && location.mapName != null) {
                        return cache[location.gameType?.serverName?.lowercase(Locale.ENGLISH) ?: ""]?.get(location.mapName.split(" ").joinToString("_") { it.lowercase() })?.toString()
                            ?: "Unknown"
                    }
                }
            }
            return notBedwarsText
        }

        @Exclude
        companion object {
            @Transient val cache: HashMap<String, HashMap<String, Int>> = HashMap()

            init {
                val json = NetworkUtils.getJsonElement("https://maps.pinkulu.com/")?.asJsonObject
                if (json != null) {
                    for (mode in json.entrySet()) {
                        val maps = HashMap<String, Int>()
                        for (map in mode.value.asJsonObject.entrySet()) {
                            maps[map.key] = map.value.asInt
                        }
                        cache[mode.key] = maps
                    }
                }
            }
        }
    }
}