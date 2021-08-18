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

package dev.isxander.evergreenhud.config.convert.impl

import com.uchuhimo.konf.Config
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.universal.FONT_RENDERER
import dev.isxander.evergreenhud.compatibility.universal.RESOLUTION
import dev.isxander.evergreenhud.config.convert.ConfigConverter
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

        val config = Config()
        EvergreenHUD.elementManager.enabled = config["enabled"]

        for (elementJson in config.get<List<Config>>("elements")) {
            val position = scaledPosition {
                x = elementJson["x"]
                y = elementJson["y"]
                scale = elementJson["scale"]
            }

            var textColor = Color(elementJson["color"])
            if (elementJson.getOrNull<Boolean>("rgb") == true)
                textColor = Color(
                    elementJson.getOrNull("red") ?: 255,
                    elementJson.getOrNull("green") ?: 255,
                    elementJson.getOrNull("blue") ?: 255, 255
                )

            val chroma = elementJson.getOrNull("chroma") ?: false
            val shadow = elementJson.getOrNull("shadow") ?: false
            val useBg = elementJson.getOrNull("highlighted") ?: false

            var i = 0
            val changeY = (FONT_RENDERER.fontHeight + 4) / RESOLUTION.scaledHeight
            for (item in elementJson.get<List<Config>>("items")) {
                val id = ids[item.getOrNull("type") ?: ""] ?: continue
                val element = EvergreenHUD.elementManager.getNewElementInstance(id) ?: continue

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
                        element.backgroundColor.set(Color(0, 0, 0, 0))
                    }
                }

                when (element) {
                    is ElementCoordinates -> element.accuracy = item.getOrNull("precision") ?: 0
                    is ElementText -> element.text = item.getOrNull("text") ?: "Sample Text"
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