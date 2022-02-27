/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.config.convert.impl

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.config.convert.ConfigConverter
import dev.isxander.evergreenhud.elements.Element
import dev.isxander.evergreenhud.elements.impl.ElementPotionHUD
import dev.isxander.evergreenhud.elements.type.BackgroundElement
import dev.isxander.evergreenhud.elements.type.TextElement
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.OriginedPosition
import dev.isxander.evergreenhud.utils.json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import org.apache.commons.io.IOUtils
import java.io.File

object KronHudConverter : ConfigConverter {
    override val name = "KronHUD"
    val file = File("./config/kronhud.json")

    private val ids = mapOf(
        "kronhud:armorhud" to "evergreenhud:armour_hud",
        "kronhud:coordshud" to "evergreenhud:coordinates",
        "kronhud:fpshud" to "evergreenhud:fps",
        "kronhud:pinghud" to "evergreenhud:ping",
        "kronhud:potionshud" to "evergreenhud:potion_hud",
        "kronhud:cpshud" to "evergreenhud:cps",
        // any other kronhud IDs we dont have alternatives for
        // ignore them
    )

    override fun process(): String? {
        if (file.isDirectory || !file.exists())
            return "Invalid KronHUD configuration!"

        val root = json.decodeFromString<JsonObject>(IOUtils.toByteArray(file.inputStream()).decodeToString())

        if (root["configVersion"]!!.jsonPrimitive.content != "2")
            return "Unknown config version, expected version 2."

        for ((old, new) in ids) {
            val hudJson = root[old]!!.jsonObject

            if (!hudJson["enabled"]!!.jsonPrimitive.boolean)
                continue

            val element = EvergreenHUD.elementManager.getNewElementInstance<Element>(new)
                ?: continue

            element.position = OriginedPosition.scaledPositioning(
                x = hudJson["x"]!!.jsonPrimitive.float,
                y = hudJson["y"]!!.jsonPrimitive.float,
                scale = hudJson["scale"]!!.jsonPrimitive.float
            )

            hudJson["textColor"]?.jsonPrimitive?.content?.let {
                (element as? TextElement)?.textColor = parseColor(it)
                (element as? ElementPotionHUD)?.durationColor = parseColor(it)
            }
            hudJson["shadow"]?.jsonPrimitive?.boolean?.let { (element as TextElement).textStyle = if (it) TextElement.TextStyle.SHADOW else TextElement.TextStyle.NORMAL }

            hudJson["background"]?.jsonPrimitive?.boolean?.let { hasBackground ->
                if (hasBackground) {
                    hudJson["backgroundcolor"]?.jsonPrimitive?.content?.let {
                        (element as BackgroundElement).backgroundColor = parseColor(it)
                    }
                }
            }

            when (element) {
                is ElementPotionHUD -> {
                    element.titleVisible = false
                    element.backgroundColor = Color.none
                }
            }

            EvergreenHUD.elementManager.addElement(element)
        }

        return null
    }

    override fun detect(): Boolean {
        return file.exists()
    }

    private fun parseColor(color: String): Color {
        return Color(color.substring(1).toInt(radix = 16))
    }
}
