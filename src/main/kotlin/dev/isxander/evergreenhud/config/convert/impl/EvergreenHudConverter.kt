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
import dev.isxander.evergreenhud.utils.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import java.io.File

object EvergreenHudConverter {
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

    fun convertElementSettings(element: Element, customSettings: JsonObject) {
        println("Converting element ${element.metadata.id}...")

        customSettings.decode<Boolean>("showinchat")?.let { element.showInChat = it }
        customSettings.decode<Boolean>("showinf3")?.let { element.showInDebug = it }
        customSettings.decode<Boolean>("showunderguis")?.let { element.showUnderGui = it }

        if (element is BackgroundElement) {
            element.backgroundColor = if (customSettings.decode<Boolean>("bgenabled") != false) {
                Color(
                    customSettings.decode<Int>("bgred") ?: element.backgroundColor.red,
                    customSettings.decode<Int>("bggreen") ?: element.backgroundColor.green,
                    customSettings.decode<Int>("bgblue") ?: element.backgroundColor.blue,
                    customSettings.decode<Int>("bgalpha") ?: element.backgroundColor.alpha,
                )
            } else {
                Color.none
            }

            customSettings.decode<Float>("paddingleft")?.let { element.paddingLeft = it }
            customSettings.decode<Float>("paddingright")?.let { element.paddingRight = it }
            customSettings.decode<Float>("paddingtop")?.let { element.paddingTop = it }
            customSettings.decode<Float>("paddingbottom")?.let { element.paddingBottom = it }

            customSettings.decode<Float>("cornerradius")?.let { element.cornerRadius = it }

            customSettings.decode<Boolean>("outlineenabled")?.let { element.outlineEnabled = it }
            element.outlineColor = Color(
                customSettings.decode<Int>("outlinered") ?: element.outlineColor.red,
                customSettings.decode<Int>("outlinegreen") ?: element.outlineColor.green,
                customSettings.decode<Int>("outlineblue") ?: element.outlineColor.blue,
                customSettings.decode<Int>("outlinealpha") ?: element.outlineColor.alpha,
            )
            customSettings.decode<Float>("outlineweight")?.let { element.outlineThickness = it }
        }

        if (element is TextElement) {
            customSettings.decode<Boolean>("brackets")?.let { element.brackets = it }
            customSettings.decode<String>("title")?.let { element.title = it }
            element.textColor = Color(
                customSettings.decode<Int>("textred") ?: element.textColor.red,
                customSettings.decode<Int>("textgreen") ?: element.textColor.green,
                customSettings.decode<Int>("textblue") ?: element.textColor.blue,
                customSettings.decode<Int>("textalpha") ?: element.textColor.alpha,
                if (customSettings.decode<Boolean>("chromatext") == true) Color.ChromaProperties.default else Color.ChromaProperties.none
            )
            customSettings.decode<Int>("textmode")?.let { element.textStyle = TextElement.TextStyle.options[it] }
            customSettings.decode<Int>("alignment")?.let {
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
                if (customSettings.decode<Boolean>("invertedtitle") == true)
                    SimpleTextElement.TitleLocation.END
                else
                    SimpleTextElement.TitleLocation.BEGINNING
        }

        if (element is MultiLineTextElement) {
            customSettings.decode<Int>("verticalspacing")?.let { element.verticalSpacing = it }
        }

        if (element is ElementArmourHUD) {
            customSettings.decode<Boolean>("showhelmet")?.let { element.showHelmet = it }
            customSettings.decode<Boolean>("showchestplate")?.let { element.showChestplate = it }
            customSettings.decode<Boolean>("showleggings")?.let { element.showLeggings = it }
            customSettings.decode<Boolean>("showboots")?.let { element.showBoots = it }
            val showItem = customSettings.decode<Boolean>("showitem")
            showItem?.let {
                element.showMainHand = it
                element.showOffHand = it
            }
            customSettings.decode<Int>("spacing")?.let { element.padding = it }
            customSettings.decode<Int>("listtype")?.let {
                element.displayType = ElementArmourHUD.DisplayType.options[it]
            }
            element.extraInfo = when (customSettings.decode<Int>("text")) {
                0 -> ElementArmourHUD.ExtraInfo.DurabilityAbsolute
                1 -> ElementArmourHUD.ExtraInfo.Name
                2 -> ElementArmourHUD.ExtraInfo.None
                else -> element.extraInfo
            }

            element.textColor = Color(
                customSettings.decode<Int>("textred") ?: element.textColor.red,
                customSettings.decode<Int>("textgreen") ?: element.textColor.green,
                customSettings.decode<Int>("textblue") ?: element.textColor.blue,
                customSettings.decode<Int>("textalpha") ?: element.textColor.alpha,
                if (customSettings.decode<Boolean>("chromatext") == true) Color.ChromaProperties.default else Color.ChromaProperties.none
            )
            element.textStyle = TextElement.TextStyle.options[customSettings.decode("textmode")!!]
            element.alignment = when (customSettings.decode<Int>("alignment")) {
                0, 1 -> ElementArmourHUD.Alignment.LEFT
                2 -> ElementArmourHUD.Alignment.RIGHT
                else -> ElementArmourHUD.Alignment.LEFT
            }
        }

        if (element is ElementBlockAbove) {
            customSettings.decode<Boolean>("notify")?.let { element.notify = it }
            customSettings.decode<Int>("notifyheight")?.let { element.notifyHeight = it }
            customSettings.decode<Int>("checkamount")?.let { element.checkHeight = it }
        }

        if (element is ElementPlaceCount) {
            customSettings.decode<Int>("interval")?.let { element.interval = it }
        }

        if (element is ElementCombo) {
            customSettings.decode<Int>("discardtime")?.let { element.discardTime = it }
            customSettings.decode<String>("nohitmessage")?.let { element.noHitMessage = it }
        }

        if (element is ElementCoordinates) {
            customSettings.decode<Int>("mode")?.let { element.displayMode = ElementCoordinates.DisplayMode.options[it] }
            customSettings.decode<Boolean>("showname")?.let { element.showAxis = it }
            customSettings.decode<Boolean>("showdirection")?.let { element.showDirection = it }
            customSettings.decode<Boolean>("showx")?.let { element.showX = it }
            customSettings.decode<Boolean>("showy")?.let { element.showY = it }
            customSettings.decode<Boolean>("showz")?.let { element.showZ = it }
            customSettings.decode<Int>("accuracy")?.let { element.accuracy = it }
            customSettings.decode<Boolean>("trailingzeros")?.let { element.trailingZeros = it }
        }

        if (element is ElementCps) {
            customSettings.decode<Int>("button")?.let { element.button = ElementCps.MouseButton.options[it] }
        }

        if (element is ElementDirection) {
            customSettings.decode<Boolean>("abbreviated")?.let { element.abbreviated = it }
        }

        if (element is ElementHypixelGame) {
            customSettings.decode<String>("nothypixelmessage")?.let { element.noHypixelMessage = it }
        }

        if (element is ElementHypixelMap) {
            customSettings.decode<String>("nothypixelmessage")?.let { element.noHypixelMessage = it }
        }

        if (element is ElementHypixelMode) {
            customSettings.decode<String>("nothypixelmessage")?.let { element.noHypixelMessage = it }
        }

        if (element is ElementImage) {
            customSettings.decode<Boolean>("autoscale")?.let { element.autoScale = it }
            customSettings.decode<Boolean>("mirror")?.let { element.mirror = it }
            customSettings.decode<Float>("rotation")?.let { element.rotation = it }
            customSettings.decode<String>("filepath")?.let { element.file = File(it) }
        }

        if (element is ElementMemory) {
            customSettings.decode<Int>("mode")?.let { element.displayType = ElementMemory.DisplayType.options[it] }
            customSettings.decode<Boolean>("trailingzeros")?.let { element.trailingZeros = it }
        }

        if (element is ElementPing) {
            customSettings.decode<Boolean>("hideinsingleplayer")?.let { element.showInSinglePlayer = !it }
        }

        if (element is ElementPitch) {
            element.accuracy = 1
            customSettings.decode<Boolean>("trailingzeros")?.let { element.trailingZeros = it }
        }

        if (element is ElementPlayerPreview) {
            customSettings.decode<Int>("rotation")?.let { element.rotation = 360 - it }
        }

        if (element is ElementPotionHUD) {
            customSettings.decode<Boolean>("showtitle")?.let { element.titleVisible = it }
            element.titleColor = Color(
                customSettings.decode<Int>("titlered") ?: element.titleColor.red,
                customSettings.decode<Int>("titlegreen") ?: element.titleColor.green,
                customSettings.decode<Int>("titleblue") ?: element.titleColor.blue,
                customSettings.decode<Int>("titlealpha") ?: element.titleColor.alpha,
                if (customSettings.decode<Boolean>("titlechroma") == true) Color.ChromaProperties.default else Color.ChromaProperties.none
            )
            customSettings.decode<Boolean>("titlebold")?.let { element.titleBold = it }
            customSettings.decode<Boolean>("titleitalic")?.let { element.titleItalic = it }
            customSettings.decode<Boolean>("titleunderlined")?.let { element.titleUnderlined = it }
            customSettings.decode<Int>("titlemode")?.let { element.titleStyle = TextElement.TextStyle.options[it] }
            customSettings.decode<Boolean>("showamplifier")?.let { element.amplifier = it }
            customSettings.decode<Boolean>("showlvl1")?.let { element.showLvl1 = it }

            customSettings.decode<Boolean>("showtime")?.let { element.durationVisible = it }
            element.durationColor = Color(
                customSettings.decode<Int>("timered") ?: 255,
                customSettings.decode<Int>("timegreen") ?: 255,
                customSettings.decode<Int>("timeblue") ?: 180,
                customSettings.decode<Int>("timealpha") ?: 255,
                if (customSettings.decode<Boolean>("timechroma") == true) Color.ChromaProperties.default else Color.ChromaProperties.none
            )
            customSettings.decode<Boolean>("timebold")?.let { element.durationBold = it }
            customSettings.decode<Boolean>("timeitalic")?.let { element.durationItalic = it }
            customSettings.decode<Boolean>("timeunderlined")?.let { element.durationUnderlined = it }
            customSettings.decode<Int>("timemode")?.let { element.durationStyle = TextElement.TextStyle.options[it] }
            customSettings.decode<String>("permanenttext")?.let { element.permanentText = it }
            customSettings.decode<Int>("blinkingtime")?.let { element.blinkingTime = it }
            customSettings.decode<Int>("blinkingspeed")?.let { element.blinkingSpeed = it }

            customSettings.decode<Boolean>("showicon")?.let { element.showIcon = it }
            customSettings.decode<Int>("verticalalign")?.let { element.invertSort = it != 0 }
            customSettings.decode<Int>("sort")?.let { element.sort = ElementPotionHUD.PotionSorting.options[it] }
            customSettings.decode<Int>("verticalspacing")?.let { element.verticalSpacing = it }
        }

        if (element is ElementReach) {
            customSettings.decode<Boolean>("trailingzeros")?.let { element.trailingZeros = it }
        }
    }

    object V14 : ConfigConverter {
        override val name = "EvergreenHUD 1.4"
        val configFile = File(mc.runDirectory, "config/evergreenhud/elements.json")

        override fun process(): String? {
            if (configFile.isDirectory || !configFile.exists())
                return "Invalid EvergreenHUD 1.4 configuration."

            val config = json.decodeFromString<RootConfig>(configFile.readText())

            for (elementConfig in config.elements) {
                val element = idMap[elementConfig.id]?.let { EvergreenHUD.elementManager.getNewElementInstance<Element>(it) }
                    ?: continue

                val settings = elementConfig.settings
                element.position = OriginedPosition.scaledPositioning(
                    settings.x,
                    settings.y,
                    settings.scale,
                )

                convertElementSettings(element, settings.dynamic)

                EvergreenHUD.elementManager.addElement(element)
            }

            EvergreenHUD.elementManager.elementConfig.save()
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

    object V13 : ConfigConverter {
        override val name = "EvergreenHUD 1.3"
        val configFolder = File(mc.runDirectory, "config/evergreenhud/elements")

        override fun process(): String? {
            if (!detect())
                return "Invalid EvergreenHUD 1.3 configuration."

            for (elementConfigFile in configFolder.listFiles()!!) {
                if (!elementConfigFile.isDirectory)
                    continue

                val config = json.decodeFromString<ElementConfig>(elementConfigFile.readText())

                if (config.version == 1 && config.enabled) {
                    val elementId = elementConfigFile.nameWithoutExtension
                    val element = idMap[elementId]?.let { EvergreenHUD.elementManager.getNewElementInstance<Element>(it) }
                        ?: continue

                    element.position = OriginedPosition.scaledPositioning(
                        config.x,
                        config.y,
                        config.scale
                    )

                    (element as? BackgroundElement)?.apply {
                        backgroundColor = config.bgColor.toColor()
                    }

                    (element as? TextElement)?.apply {
                        if (!config.showTitle)
                            title = ""
                        brackets = config.showBrackets
                        textColor = config.textColor.toColor()
                    }

                    (element as? SimpleTextElement)?.apply {
                        titleLocation = if (config.inverted) SimpleTextElement.TitleLocation.END else SimpleTextElement.TitleLocation.BEGINNING
                    }

                    convertElementSettings(element, config.custom)

                    EvergreenHUD.elementManager.addElement(element)
                }
            }

            EvergreenHUD.elementManager.elementConfig.save()

            return null
        }

        override fun detect(): Boolean {
            return configFolder.exists() && configFolder.isDirectory
        }
    }

    @Serializable
    data class ElementConfig(
        val version: Int,
        val enabled: Boolean,
        val x: Float,
        val y: Float,
        val scale: Float,
        @SerialName("title") val showTitle: Boolean,
        @SerialName("brackets") val showBrackets: Boolean,
        val inverted: Boolean,
        val chroma: Boolean,
        val shadow: Boolean,
        val alignment: Int,
        val textColor: ColorConfig,
        val bgColor: ColorConfig,
        val custom: JsonObject
    )

    @Serializable
    data class ColorConfig(
        val r: Int,
        val g: Int,
        val b: Int,
        val a: Int = 255,
    ) {
        fun toColor(): Color {
            return Color(r, g, b, a)
        }
    }
}
