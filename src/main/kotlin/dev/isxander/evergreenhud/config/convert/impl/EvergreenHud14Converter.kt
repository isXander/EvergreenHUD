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
import dev.isxander.evergreenhud.elements.impl.*
import dev.isxander.evergreenhud.elements.type.BackgroundElement
import dev.isxander.evergreenhud.elements.type.MultiLineTextElement
import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.elements.type.TextElement
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.decode
import dev.isxander.evergreenhud.utils.json
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.evergreenhud.utils.position.ZonedPosition
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import java.io.File

object EvergreenHud14Converter : ConfigConverter {
    override val name = "EvergreenHUD 1.4"
    val configFile = File(mc.runDirectory, "config/evergreenhud/elements.json")

    val idMap = mapOf(
        "ARMOUR" to "evergreenhud:armour_hud",
        "BIOME" to "evergreenhud:biome",
        "BLOCK_ABOVE" to "evergreenhud:block_above",
        "COMBO" to "evergreenhud:combo",
        "COORDS" to "evergreenhud:coordinates",
        "CPS" to "evergreenhud:cps",
        "DAY" to "evergreenhud:day",
        "DIRECTION" to "evergreenhud:direction",
        "FPS" to "evergreenhud:fps",
        "HYPIXEL_GAME" to "evergreenhud:hypixel_game",
        "HYPIXEL_MAP" to "evergreenhud:hypixel_map",
        "HYPIXEL_MODE" to "evergreenhud:hypixel_mode",
        "IMAGE" to "evergreenhud:image",
        "MEMORY" to "evergreenhud:memory",
        "PING" to "evergreenhud:ping",
        "PITCH" to "evergreenhud:pitch",
        "PLAYER_PREVIEW" to "evergreenhud:player_preview",
        "POTION_HUD" to "evergreenhud:potion_hud",
        "REACH" to "evergreenhud:reach",
        "SATURATION" to "evergreenhud:saturation",
        "SERVER" to "evergreenhud:server_ip",
        "SPEED" to "evergreenhud:speed",
        "TEXT" to "evergreenhud:text",
        "TIME" to "evergreenhud:irl_time",
        "YAW" to "evergreenhud:yaw"
    )

    override fun process(): String? {
        if (configFile.isDirectory || !configFile.exists())
            return "Invalid EvergreenHUD 1.4 configuration."

        val config = json.decodeFromString<List<ConfigSchema>>(configFile.readText())

        for (elementConfig in config) {
            val element = idMap[elementConfig.id]?.let { EvergreenHUD.elementManager.getNewElementInstance<Element>(it) }
                ?: continue

            val settings = elementConfig.settings
            element.position = ZonedPosition.scaledPositioning(
                settings.x,
                settings.y,
                settings.scale,
            )

            val dynamic = settings.dynamic

            element.showInChat = dynamic.decode("showinchat")!!
            element.showInDebug = dynamic.decode("showinf3")!!
            element.showUnderGui = dynamic.decode("showunderguis")!!

            if (element is BackgroundElement) {
                element.backgroundColor = if (dynamic.decode("bgenabled")!!) {
                    Color(
                        dynamic.decode<Int>("bgred")!!,
                        dynamic.decode<Int>("bggreen")!!,
                        dynamic.decode<Int>("bgblue")!!,
                        dynamic.decode<Int>("bgalpha")!!
                    )
                } else {
                    Color.none
                }

                element.paddingLeft = dynamic.decode("paddingleft")!!
                element.paddingRight = dynamic.decode("paddingright")!!
                element.paddingTop = dynamic.decode("paddingtop")!!
                element.paddingBottom = dynamic.decode("paddingbottom")!!

                element.cornerRadius = dynamic.decode("cornerradius")!!

                element.outlineEnabled = dynamic.decode("outlineenabled")!!
                element.outlineColor = Color(
                    dynamic.decode<Int>("outlinered")!!,
                    dynamic.decode<Int>("outlinegreen")!!,
                    dynamic.decode<Int>("outlineblue")!!,
                    dynamic.decode<Int>("outlinealpha")!!
                )
                element.outlineThickness = dynamic.decode("outlineweight")!!
            }

            if (element is TextElement) {
                element.brackets = dynamic.decode("brackets")!!
                element.title = dynamic.decode("title")!!
                element.textColor = Color(
                    dynamic.decode<Int>("textred")!!,
                    dynamic.decode<Int>("textgreen")!!,
                    dynamic.decode<Int>("textblue")!!,
                    dynamic.decode<Int>("textalpha")!!,
                    if (dynamic.decode("chromatext")!!) Color.ChromaProperties.default else Color.ChromaProperties.none
                )
                element.textStyle = TextElement.TextStyle.options[dynamic.decode("textmode")!!]
                element.alignment = when (dynamic.decode<Int>("alignment")) {
                    0 -> TextElement.Alignment.LEFT
                    1 -> TextElement.Alignment.CENTER
                    2 -> TextElement.Alignment.RIGHT
                    else -> TextElement.Alignment.LEFT
                }
            }

            if (element is SimpleTextElement) {
                element.titleLocation =
                    if (dynamic.decode("invertedtitle")!!)
                        SimpleTextElement.TitleLocation.END
                    else
                        SimpleTextElement.TitleLocation.BEGINNING
            }

            if (element is MultiLineTextElement) {
                element.verticalSpacing = dynamic.decode("verticalspacing")!!
            }

            if (element is ElementArmourHUD) {
                element.showHelmet = dynamic.decode("showhelmet")!!
                element.showChestplate = dynamic.decode("showchestplate")!!
                element.showLeggings = dynamic.decode("showleggings")!!
                element.showBoots = dynamic.decode("showboots")!!
                val showItem = dynamic.decode<Boolean>("showitem")!!
                element.showMainHand = showItem
                element.showOffHand = showItem
                element.padding = dynamic.decode("spacing")!!
                element.displayType = ElementArmourHUD.DisplayType.options[dynamic.decode("listtype")!!]
                element.extraInfo = when (dynamic.decode<Int>("text")) {
                    0 -> ElementArmourHUD.ExtraInfo.DurabilityAbsolute
                    1 -> ElementArmourHUD.ExtraInfo.Name
                    2 -> ElementArmourHUD.ExtraInfo.None
                    else -> element.extraInfo
                }

                element.textColor = Color(
                    dynamic.decode<Int>("textred")!!,
                    dynamic.decode<Int>("textgreen")!!,
                    dynamic.decode<Int>("textblue")!!,
                    dynamic.decode<Int>("textalpha")!!,
                    if (dynamic.decode("chromatext")!!) Color.ChromaProperties.default else Color.ChromaProperties.none
                )
                element.textStyle = TextElement.TextStyle.options[dynamic.decode("textmode")!!]
                element.alignment = when (dynamic.decode<Int>("alignment")) {
                    0, 1 -> ElementArmourHUD.Alignment.LEFT
                    2 -> ElementArmourHUD.Alignment.RIGHT
                    else -> ElementArmourHUD.Alignment.LEFT
                }
            }

            if (element is ElementBlockAbove) {
                element.notify = dynamic.decode("notify")!!
                element.notifyHeight = dynamic.decode("notifyheight")!!
                element.checkHeight = dynamic.decode("checkamount")!!
            }

            if (element is ElementPlaceCount) {
                element.interval = dynamic.decode("interval")!!
            }

            if (element is ElementCombo) {
                element.discardTime = dynamic.decode("discardtime")!!
                element.noHitMessage = dynamic.decode("nohitmessage")!!
            }

            if (element is ElementCoordinates) {
                element.displayMode = ElementCoordinates.DisplayMode.options[dynamic.decode("mode")!!]
                element.showAxis = dynamic.decode("showname")!!
                element.showDirection = dynamic.decode("showdirection")!!
                element.showX = dynamic.decode("showx")!!
                element.showY = dynamic.decode("showy")!!
                element.showZ = dynamic.decode("showz")!!
                element.accuracy = dynamic.decode("accuracy")!!
                element.trailingZeros = dynamic.decode("trailingzeros")!!
            }

            if (element is ElementCps) {
                element.button = ElementCps.MouseButton.options[dynamic.decode("button")!!]
            }

            if (element is ElementDirection) {
                element.abbreviated = dynamic.decode("abbreviated")!!
            }

            if (element is ElementHypixelGame) {
                element.noHypixelMessage = dynamic.decode("nothypixelmessage")!!
            }

            if (element is ElementHypixelMap) {
                element.noHypixelMessage = dynamic.decode("nothypixelmessage")!!
            }

            if (element is ElementHypixelMode) {
                element.noHypixelMessage = dynamic.decode("nothypixelmessage")!!
            }

            if (element is ElementImage) {
                element.autoScale = dynamic.decode("autoscale")!!
                element.mirror = dynamic.decode("mirror")!!
                element.rotation = dynamic.decode("rotation")!!
                element.file = File(dynamic.decode<String>("filepath")!!)
            }

            if (element is ElementMemory) {
                element.displayType = ElementMemory.DisplayType.options[dynamic.decode("mode")!!]
                element.trailingZeros = dynamic.decode("trailingzeros")!!
            }

            if (element is ElementPing) {
                element.showInSinglePlayer = !dynamic.decode<Boolean>("hideinsingleplayer")!!
            }

            if (element is ElementPitch) {
                element.accuracy = 1
                element.trailingZeros = dynamic.decode("trailingzeros")!!
            }

            if (element is ElementPlayerPreview) {
                element.rotation = 360 - dynamic.decode<Int>("rotation")!!
            }

            // TODO: potion hud i really can't be bothered

            if (element is ElementReach) {
                element.trailingZeros = dynamic.decode("trailingzeros")!!
            }



            EvergreenHUD.elementManager.addElement(element)
        }

        return null
    }

    override fun detect(): Boolean {
        return configFile.exists() && json.decodeFromString<JsonObject>(configFile.readText())["version"]!!.jsonPrimitive.int == 3
    }

    @Serializable
    data class ConfigSchema(@SerialName("type") val id: String, val settings: ElementSettingsSchema)

    @Serializable
    data class ElementSettingsSchema(val x: Float, val y: Float, val scale: Float, val dynamic: JsonObject)
}
