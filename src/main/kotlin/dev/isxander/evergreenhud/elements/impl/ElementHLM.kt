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
import dev.isxander.evergreenhud.utils.AsyncCacheHolder
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.http
import dev.isxander.evergreenhud.utils.hypixel.locraw.GameType
import dev.isxander.settxi.impl.string
import gg.essential.api.EssentialAPI
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking

@ElementMeta(id = "evergreenhud:height_limit", name = "Height Limit", description = "Show the Bedwars height limit.", category = "Hypixel", credits = "Pinkulu")
class ElementHLM : SimpleTextElement("Height") {
    var notBedwarsText by string("0") {
        name = "Not Bedwars Text"
        description = "Text to show when you are not in a bedwars game."
        category = "Height Limit"
    }

    override fun calculateValue(): String {
        if (EssentialAPI.getMinecraftUtil().isHypixel()) {
            val location = EvergreenHUD.locrawManager.currentLocation
            if (location.gameType == GameType.BEDWARS && !location.isLobby && location.map != null) {
                return cache?.get(location.map.split(" ").joinToString("_") { it.lowercase() })?.toString()
                    ?: "Unknown"
            }
        }
        return notBedwarsText
    }

    companion object {
        val cache by AsyncCacheHolder(3600000) {
            val json = runBlocking { http.get("https://api.pinkulu.com/HeightLimitMod/Limits").body<Map<String, Map<String, Int>>>() }
            json["bedwars"]!!.mapKeys { (k, _) -> k.lowercase() }
        }
    }
}
