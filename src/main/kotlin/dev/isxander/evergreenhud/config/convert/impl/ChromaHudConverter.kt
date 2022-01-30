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
import dev.isxander.evergreenhud.elements.impl.ElementCoordinates
import dev.isxander.evergreenhud.elements.impl.ElementCps
import dev.isxander.evergreenhud.elements.impl.ElementIRLTime
import dev.isxander.evergreenhud.elements.impl.ElementText
import dev.isxander.evergreenhud.elements.type.TextElement
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.decode
import dev.isxander.evergreenhud.utils.json
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.evergreenhud.utils.position.ZonedPosition
import gg.essential.universal.UResolution
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.JsonObject
import java.io.File

object ChromaHudConverter : ConfigConverter {
    private val ids = mapOf(
        "CORDS" to "COORDS",
        "PING" to "PING",
        "DIRECTION" to "DIRECTION",
        "CPS" to "CPS",
        "FPS" to "FPS",
        "TEXT" to "TEXT",
        "TIME" to "IRL_TIME",
        "ARMOUR_HUD" to "ARMOUR",
        "C_COUNTER" to "CHUNK_COUNTER"
    )

    override fun process(file: File): String? {
        if (!file.exists() || file.isDirectory) return "Invalid ChromaHUD config."

        val config = json.decodeFromString<JsonObject>(file.readText())
        EvergreenHUD.elementManager.enabled = config.decode("enabled")!!

        for (elementJson in config.decode<List<JsonObject>>("elements")!!) {
            val position = ZonedPosition.scaledPositioning(
                x = elementJson.decode("x")!!,
                y = elementJson.decode("y")!!,
                scale = elementJson.decode("scale")!!,
            )

            var textColor = Color(elementJson.decode("color")!!)
            if (elementJson.decode<Boolean>("rgb") == true)
                textColor = Color(
                    elementJson.decode("red") ?: 255,
                    elementJson.decode("green") ?: 255,
                    elementJson.decode("blue") ?: 255,
                    255
                )

            val chroma = elementJson.decode("chroma") ?: false
            textColor = textColor.withChroma(Color.ChromaProperties(chroma))

            val shadow = elementJson.decode("shadow") ?: false
            val useBg = elementJson.decode("highlighted") ?: false

            var i = 0
            val changeY = (mc.fontRendererObj.FONT_HEIGHT + 4) / UResolution.scaledHeight
            for (item in elementJson.decode<List<JsonObject>>("items") ?: emptyList()) {
                val id = ids[item.decode("type") ?: ""] ?: continue
                val element = EvergreenHUD.elementManager.getNewElementInstance<Element>(id) ?: continue

                element.position = position
                element.position.zoneY = element.position.zoneY + (i * changeY)

                if (element is TextElement) {
                    element.textColor = textColor
                    element.textStyle = if (shadow) TextElement.TextStyle.SHADOW else TextElement.TextStyle.NORMAL

                    if (useBg) {
                        element.paddingLeft = 1f
                        element.paddingRight = 1f
                        element.paddingTop = 1f
                        element.paddingBottom = 1f
                    } else {
                        element.backgroundColor = Color(0, 0, 0, 0)
                    }
                }

                when (element) {
                    is ElementCoordinates -> element.accuracy = item.decode("precision") ?: 0
                    is ElementText -> element.text = item.decode("text") ?: "Sample Text"
                    is ElementIRLTime -> element.seconds = true
                    is ElementCps -> element.button = ElementCps.MouseButton.LEFT
                }

                EvergreenHUD.elementManager.addElement(element)

                i++
            }
        }

        return null
    }
}
