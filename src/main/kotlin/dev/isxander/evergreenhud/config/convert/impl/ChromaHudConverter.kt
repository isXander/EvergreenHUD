/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.config.convert.impl

import com.electronwill.nightconfig.core.Config
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.config.convert.ConfigConverter
import dev.isxander.evergreenhud.elements.Element
import dev.isxander.evergreenhud.elements.impl.ElementCoordinates
import dev.isxander.evergreenhud.elements.impl.ElementCps
import dev.isxander.evergreenhud.elements.impl.ElementIRLTime
import dev.isxander.evergreenhud.elements.impl.ElementText
import dev.isxander.evergreenhud.elements.type.TextElement
import dev.isxander.evergreenhud.utils.*
import java.awt.Color
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

        val config = Config.of(jsonFormat)
        EvergreenHUD.elementManager.enabled = config["enabled"]

        for (elementJson in config.get<List<Config>>("elements")) {
            val position = scaledPosition {
                x = elementJson["x"]
                y = elementJson["y"]
                scale = elementJson["scale"]
            }

            var textColor = Color(elementJson["color"])
            if (elementJson.get<Boolean>("rgb") == true)
                textColor = Color(
                    elementJson["red"] ?: 255,
                    elementJson["green"] ?: 255,
                    elementJson["blue"] ?: 255,
                    255
                )

            val chroma = elementJson["chroma"] ?: false
            val shadow = elementJson["shadow"] ?: false
            val useBg = elementJson["highlighted"] ?: false

            var i = 0
            val changeY = (mc.textRenderer.fontHeight + 4) / mc.window.scaledHeight
            for (item in elementJson.get<List<Config>>("items")) {
                val id = ids[item["type"] ?: ""] ?: continue
                val element = EvergreenHUD.elementManager.getNewElementInstance<Element>(id) ?: continue

                element.position = position
                element.position.scaledY = element.position.scaledY + (i * changeY)

                if (element is TextElement) {
                    element.textColor = textColor
                    element.chroma = chroma
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
                    is ElementCoordinates -> element.accuracy = item["precision"] ?: 0
                    is ElementText -> element.text = item["text"] ?: "Sample Text"
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
