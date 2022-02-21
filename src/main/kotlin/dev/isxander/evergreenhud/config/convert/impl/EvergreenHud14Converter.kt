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

        val config = json.decodeFromString<RootConfig>(configFile.readText())

        for (elementConfig in config.elements) {
            val element = idMap[elementConfig.id]?.let { EvergreenHUD.elementManager.getNewElementInstance<Element>(it) }
                ?: continue

            println("Converting element ${element.metadata.id}...")

            val settings = elementConfig.settings
            element.position = ZonedPosition.scaledPositioning(
                settings.x,
                settings.y,
                settings.scale,
            )

            val dynamic = settings.dynamic

            dynamic.decode<Boolean>("showinchat")?.let { element.showInChat = it }
            dynamic.decode<Boolean>("showinf3")?.let { element.showInDebug = it }
            dynamic.decode<Boolean>("showunderguis")?.let { element.showUnderGui = it }

            if (element is BackgroundElement) {
                element.backgroundColor = if (dynamic.decode<Boolean>("bgenabled") != false) {
                    Color(
                        dynamic.decode<Int>("bgred") ?: element.backgroundColor.red,
                        dynamic.decode<Int>("bggreen") ?: element.backgroundColor.green,
                        dynamic.decode<Int>("bgblue") ?: element.backgroundColor.blue,
                        dynamic.decode<Int>("bgalpha") ?: element.backgroundColor.alpha,
                    )
                } else {
                    Color.none
                }

                dynamic.decode<Float>("paddingleft")?.let { element.paddingLeft = it }
                dynamic.decode<Float>("paddingright")?.let { element.paddingRight = it }
                dynamic.decode<Float>("paddingtop")?.let { element.paddingTop = it }
                dynamic.decode<Float>("paddingbottom")?.let { element.paddingBottom = it }

                dynamic.decode<Float>("cornerradius")?.let { element.cornerRadius = it }

                dynamic.decode<Boolean>("outlineenabled")?.let { element.outlineEnabled = it }
                element.outlineColor = Color(
                    dynamic.decode<Int>("outlinered") ?: element.outlineColor.red,
                    dynamic.decode<Int>("outlinegreen") ?: element.outlineColor.green,
                    dynamic.decode<Int>("outlineblue") ?: element.outlineColor.blue,
                    dynamic.decode<Int>("outlinealpha") ?: element.outlineColor.alpha,
                )
                dynamic.decode<Float>("outlineweight")?.let { element.outlineThickness = it }
            }

            if (element is TextElement) {
                dynamic.decode<Boolean>("brackets")?.let { element.brackets = it }
                dynamic.decode<String>("title")?.let { element.title = it }
                element.textColor = Color(
                    dynamic.decode<Int>("textred") ?: element.textColor.red,
                    dynamic.decode<Int>("textgreen") ?: element.textColor.green,
                    dynamic.decode<Int>("textblue") ?: element.textColor.blue,
                    dynamic.decode<Int>("textalpha") ?: element.textColor.alpha,
                    if (dynamic.decode<Boolean>("chromatext") == true) Color.ChromaProperties.default else Color.ChromaProperties.none
                )
                dynamic.decode<Int>("textmode")?.let { element.textStyle = TextElement.TextStyle.options[it] }
                dynamic.decode<Int>("alignment")?.let {
                    when (it) {
                        0 -> TextElement.Alignment.LEFT
                        1 -> TextElement.Alignment.CENTER
                        2 -> TextElement.Alignment.RIGHT
                        else -> TextElement.Alignment.LEFT
                    }
                }
            }

            if (element is SimpleTextElement) {
                element.titleLocation =
                    if (dynamic.decode<Boolean>("invertedtitle") == true)
                        SimpleTextElement.TitleLocation.END
                    else
                        SimpleTextElement.TitleLocation.BEGINNING
            }

            if (element is MultiLineTextElement) {
                dynamic.decode<Int>("verticalspacing")?.let { element.verticalSpacing = it }
            }

            if (element is ElementArmourHUD) {
                dynamic.decode<Boolean>("showhelmet")?.let { element.showHelmet = it }
                dynamic.decode<Boolean>("showchestplate")?.let { element.showChestplate = it }
                dynamic.decode<Boolean>("showleggings")?.let { element.showLeggings = it }
                dynamic.decode<Boolean>("showboots")?.let { element.showBoots = it }
                val showItem = dynamic.decode<Boolean>("showitem")
                showItem?.let {
                    element.showMainHand = it
                    element.showOffHand = it
                }
                dynamic.decode<Int>("spacing")?.let { element.padding = it }
                dynamic.decode<Int>("listtype")?.let {
                    element.displayType = ElementArmourHUD.DisplayType.options[it]
                }
                element.extraInfo = when (dynamic.decode<Int>("text")) {
                    0 -> ElementArmourHUD.ExtraInfo.DurabilityAbsolute
                    1 -> ElementArmourHUD.ExtraInfo.Name
                    2 -> ElementArmourHUD.ExtraInfo.None
                    else -> element.extraInfo
                }

                element.textColor = Color(
                    dynamic.decode<Int>("textred") ?: element.textColor.red,
                    dynamic.decode<Int>("textgreen") ?: element.textColor.green,
                    dynamic.decode<Int>("textblue") ?: element.textColor.blue,
                    dynamic.decode<Int>("textalpha") ?: element.textColor.alpha,
                    if (dynamic.decode<Boolean>("chromatext") == true) Color.ChromaProperties.default else Color.ChromaProperties.none
                )
                element.textStyle = TextElement.TextStyle.options[dynamic.decode("textmode")!!]
                element.alignment = when (dynamic.decode<Int>("alignment")) {
                    0, 1 -> ElementArmourHUD.Alignment.LEFT
                    2 -> ElementArmourHUD.Alignment.RIGHT
                    else -> ElementArmourHUD.Alignment.LEFT
                }
            }

            if (element is ElementBlockAbove) {
                dynamic.decode<Boolean>("notify")?.let { element.notify = it }
                dynamic.decode<Int>("notifyheight")?.let { element.notifyHeight = it }
                dynamic.decode<Int>("checkamount")?.let { element.checkHeight = it }
            }

            if (element is ElementPlaceCount) {
                dynamic.decode<Int>("interval")?.let { element.interval = it }
            }

            if (element is ElementCombo) {
                dynamic.decode<Int>("discardtime")?.let { element.discardTime = it }
                dynamic.decode<String>("nohitmessage")?.let { element.noHitMessage = it }
            }

            if (element is ElementCoordinates) {
                dynamic.decode<Int>("mode")?.let { element.displayMode = ElementCoordinates.DisplayMode.options[it] }
                dynamic.decode<Boolean>("showname")?.let { element.showAxis = it }
                dynamic.decode<Boolean>("showdirection")?.let { element.showDirection = it }
                dynamic.decode<Boolean>("showx")?.let { element.showX = it }
                dynamic.decode<Boolean>("showy")?.let { element.showY = it }
                dynamic.decode<Boolean>("showz")?.let { element.showZ = it }
                dynamic.decode<Int>("accuracy")?.let { element.accuracy = it }
                dynamic.decode<Boolean>("trailingzeros")?.let { element.trailingZeros = it }
            }

            if (element is ElementCps) {
                dynamic.decode<Int>("button")?.let { element.button = ElementCps.MouseButton.options[it] }
            }

            if (element is ElementDirection) {
                dynamic.decode<Boolean>("abbreviated")?.let { element.abbreviated = it }
            }

            if (element is ElementHypixelGame) {
                dynamic.decode<String>("nothypixelmessage")?.let { element.noHypixelMessage = it }
            }

            if (element is ElementHypixelMap) {
                dynamic.decode<String>("nothypixelmessage")?.let { element.noHypixelMessage = it }
            }

            if (element is ElementHypixelMode) {
                dynamic.decode<String>("nothypixelmessage")?.let { element.noHypixelMessage = it }
            }

            if (element is ElementImage) {
                dynamic.decode<Boolean>("autoscale")?.let { element.autoScale = it }
                dynamic.decode<Boolean>("mirror")?.let { element.mirror = it }
                dynamic.decode<Float>("rotation")?.let { element.rotation = it }
                dynamic.decode<String>("filepath")?.let { element.file = File(it) }
            }

            if (element is ElementMemory) {
                dynamic.decode<Int>("mode")?.let { element.displayType = ElementMemory.DisplayType.options[it] }
                dynamic.decode<Boolean>("trailingzeros")?.let { element.trailingZeros = it }
            }

            if (element is ElementPing) {
                dynamic.decode<Boolean>("hideinsingleplayer")?.let { element.showInSinglePlayer = !it }
            }

            if (element is ElementPitch) {
                element.accuracy = 1
                dynamic.decode<Boolean>("trailingzeros")?.let { element.trailingZeros = it }
            }

            if (element is ElementPlayerPreview) {
                dynamic.decode<Int>("rotation")?.let { element.rotation = 360 - it }
            }

            if (element is ElementPotionHUD) {
                dynamic.decode<Boolean>("showtitle")?.let { element.titleVisible = it }
                element.titleColor = Color(
                    dynamic.decode<Int>("titlered") ?: element.titleColor.red,
                    dynamic.decode<Int>("titlegreen") ?: element.titleColor.green,
                    dynamic.decode<Int>("titleblue") ?: element.titleColor.blue,
                    dynamic.decode<Int>("titlealpha") ?: element.titleColor.alpha,
                    if (dynamic.decode<Boolean>("titlechroma") == true) Color.ChromaProperties.default else Color.ChromaProperties.none
                )
                dynamic.decode<Boolean>("titlebold")?.let { element.titleBold = it }
                dynamic.decode<Boolean>("titleitalic")?.let { element.titleItalic = it }
                dynamic.decode<Boolean>("titleunderlined")?.let { element.titleUnderlined = it }
                dynamic.decode<Int>("titlemode")?.let { element.titleStyle = TextElement.TextStyle.options[it] }
                dynamic.decode<Boolean>("showamplifier")?.let { element.amplifier = it }
                dynamic.decode<Boolean>("showlvl1")?.let { element.showLvl1 = it }

                dynamic.decode<Boolean>("showtime")?.let { element.durationVisible = it }
                element.durationColor = Color(
                    dynamic.decode<Int>("timered") ?: 255,
                    dynamic.decode<Int>("timegreen") ?: 255,
                    dynamic.decode<Int>("timeblue") ?: 180,
                    dynamic.decode<Int>("timealpha") ?: 255,
                    if (dynamic.decode<Boolean>("timechroma") == true) Color.ChromaProperties.default else Color.ChromaProperties.none
                )
                dynamic.decode<Boolean>("timebold")?.let { element.durationBold = it }
                dynamic.decode<Boolean>("timeitalic")?.let { element.durationItalic = it }
                dynamic.decode<Boolean>("timeunderlined")?.let { element.durationUnderlined = it }
                dynamic.decode<Int>("timemode")?.let { element.durationStyle = TextElement.TextStyle.options[it] }
                dynamic.decode<String>("permanenttext")?.let { element.permanentText = it }
                dynamic.decode<Int>("blinkingtime")?.let { element.blinkingTime = it }
                dynamic.decode<Int>("blinkingspeed")?.let { element.blinkingSpeed = it }

                dynamic.decode<Boolean>("showicon")?.let { element.showIcon = it }
                dynamic.decode<Int>("verticalalign")?.let { element.invertSort = it != 0 }
                dynamic.decode<Int>("sort")?.let { element.sort = ElementPotionHUD.PotionSorting.options[it] }
                dynamic.decode<Int>("verticalspacing")?.let { element.verticalSpacing = it }
            }

            if (element is ElementReach) {
                dynamic.decode<Boolean>("trailingzeros")?.let { element.trailingZeros = it }
            }

            EvergreenHUD.elementManager.addElement(element)
        }

        return null
    }

    override fun detect(): Boolean {
        return configFile.exists() && json.decodeFromString<JsonObject>(configFile.readText())["version"]!!.jsonPrimitive.int == 3
    }

    @Serializable
    data class RootConfig(val version: Int, val elements: List<ConfigSchema>)

    @Serializable
    data class ConfigSchema(@SerialName("type") val id: String, val settings: ElementSettingsSchema)

    @Serializable
    data class ElementSettingsSchema(val x: Float, val y: Float, val scale: Float, val dynamic: JsonObject)
}
