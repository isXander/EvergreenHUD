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

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.compatibility.universal.*
import dev.isxander.evergreenhud.compatibility.universal.impl.UPotion
import dev.isxander.evergreenhud.elements.ElementMeta
import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.elements.type.BackgroundElement
import dev.isxander.evergreenhud.elements.type.TextElement
import dev.isxander.evergreenhud.settings.SettingAdapter
import dev.isxander.evergreenhud.settings.impl.*
import dev.isxander.evergreenhud.settings.providers.AdapterProvider
import dev.isxander.evergreenhud.settings.settingAdapter
import dev.isxander.evergreenhud.utils.drawString
import dev.isxander.evergreenhud.utils.getRoman
import dev.isxander.evergreenhud.utils.ticksToTime
import gg.essential.universal.ChatColor
import java.awt.Color
import java.util.*
import kotlin.reflect.full.primaryConstructor

@ElementMeta(id = "POTIONS", name = "PotionHUD", category = "Player", description = "Display potions.")
class ElementPotionHUD : BackgroundElement() {

    @BooleanSetting(name = "Visible", category = "Title", description = "If the title should be rendered.")
    var titleVisible = true

    @ColorSetting(name = "Color", category = "Title", description = "Color of the name of the potion effect.")
    var titleColor = Color(255, 255, 255)

    @OptionSetting(name = "Style", category = "Title", description = "In what style should the text be rendered.")
    var titleStyle = TextElement.TextStyle.SHADOW

    @BooleanSetting(name = "Chroma", category = "Title", description = "Makes the text rainbow barf.")
    var titleChroma = false

    @FloatSetting(name = "Chroma Speed", category = "Title", description = "How fast should the chroma wave be?", min = 500f, max = 10000f)
    val titleChromaSpeed = settingAdapter(2000f) {
        depends { titleChroma }
    }

    @BooleanSetting(name = "Bold", category = "Title", description = "If the title should be bold.")
    var titleBold = true

    @BooleanSetting(name = "Underlined", category = "Title", description = "If the title should be underlined.")
    var titleUnderlined = false

    @BooleanSetting(name = "Italic", category = "Title", description = "If the title should be slanted.")
    var titleItalic = false

    @BooleanSetting(name = "Show Amplifier", category = "Title", description = "Show the amplifier of the potion effect.")
    var amplifier = true

    @OptionSetting(name = "Amplifier Style", category = "Title", description = "The style of amplifier to use.")
    var amplifierStyle = AmplifierText.ARABIC

    @BooleanSetting(name = "Show LVL 1", category = "Title", description = "Show the amplifier if it is level 1.")
    var showLvl1 = true


    @BooleanSetting(name = "Visible", category = "Duration", description = "If the duration should be rendered.")
    var durationVisible = true

    @ColorSetting(name = "Color", category = "Duration", description = "Color of the duration.")
    var durationColor = Color(255, 255, 180)

    @OptionSetting(name = "Style", category = "Duration", description = "In what style should the text be rendered.")
    var durationStyle = TextElement.TextStyle.SHADOW

    @BooleanSetting(name = "Chroma", category = "Duration", description = "Makes the text rainbow barf.")
    var durationChroma = false

    @FloatSetting(name = "Chroma Speed", category = "Duration", description = "How fast should the chroma wave be?", min = 500f, max = 10000f)
    val durationChromaSpeed = settingAdapter(2000f) {
        depends { durationChroma }
    }

    @BooleanSetting(name = "Bold", category = "Duration", description = "If the duration should be bold.")
    var durationBold = true

    @BooleanSetting(name = "Underlined", category = "Duration", description = "If the duration should be underlined.")
    var durationUnderlined = false

    @BooleanSetting(name = "Italic", category = "Duration", description = "If the duration should be slanted.")
    var durationItalic = false

    @StringSetting(name = "Permanent Text", category = "Duration", description = "The text to display when the effect is permanent.")
    var permanentText = "**:**"

    @IntSetting(name = "Blinking Time", category = "Duration", description = "How many seconds remaining before the duration starts to flash.", min = 0, max = 30, suffix = " secs")
    var blinkingTime = 5

    @IntSetting(name = "Blinking Speed", category = "Duration", description = "How fast the time blinks.", min = 10, max = 45)
    var blinkingSpeed = 30


    @BooleanSetting(name = "Show Icon", category = "Miscellaneous", description = "Show the potion icon.")
    var showIcon = true

    @OptionSetting(name = "Sort", category = "Miscellaneous", description = "How the potion effects should be sorted.")
    var sort = PotionSorting.DURATION

    @BooleanSetting(name = "Invert Sort", category = "Miscellaneous", description = "Inverts the sorting method.")
    var invertSort = false

    @IntSetting(name = "Vertical Spacing", category = "Miscellaneous", description = "How far apart each potion is.", min = 1, max = 9, suffix = " px")
    var verticalSpacing = 2

    val effectToggles = hashMapOf<Int, SettingAdapter<Boolean>>()

    override fun preinit() {
        for (potion in POTIONS.registeredPotions) {
            if (potion.instant) continue

            val name = TRANSLATION[potion.translation]
            val setting = settingAdapter(true) {}

            settings.add(
                BooleanSettingWrapped(
                    // you can only instantiate an annotation through reflection
                    BooleanSetting::class.primaryConstructor!!.call(
                        name,
                        arrayOf("Toggles"),
                        "Display $name on the list.",
                        true
                    ),
                    AdapterProvider(setting)
                )
            )

            effectToggles[potion.id] = setting
        }
    }

    private val iconSize = 18

    private var w = hitboxWidth
    private var h = hitboxHeight
    override val hitboxWidth: Float
        get() = w
    override val hitboxHeight: Float
        get() = h

    override fun render(deltaTicks: Float, renderOrigin: RenderOrigin) {
        GL.color(1f, 1f, 1f, 1f)

        val x = position.rawX
        val y = position.rawY

        val potionEffects = arrayListOf<UPotion>()
        if (MC.player.equals(null)) return
        potionEffects.addAll(POTIONS.getEffectsForEntity(MC.player).filter {
            effectToggles[it.id]?.get() ?: false
        })

        when (sort) {
            PotionSorting.ALPHABETICAL -> potionEffects.sortWith(Comparator.comparing { TRANSLATION[it.translation] })
            PotionSorting.DURATION -> {
                potionEffects.sortWith(Comparator.comparingInt(UPotion::duration))
                potionEffects.reverse()
            }
        }

        if (invertSort) potionEffects.reverse()

        var yOff = 0f
        var xOff = 0f
        val yAmt = iconSize.toFloat() + verticalSpacing

        h = potionEffects.size * yAmt - verticalSpacing
        super.render(deltaTicks, renderOrigin)
        w = 10f

        GL.push()
        GL.scale(position.scale, position.scale, 1f)
        for (effect in potionEffects) {
            var iconX = x
            iconX /= position.scale
            GL.color(1f, 1f, 1f, 1f)
            if (showIcon) {
                POTIONS.drawPotionIcon(effect, iconX, (y + yOff) / position.scale)
                xOff = iconSize * position.scale
                w = w.coerceAtLeast(xOff / position.scale)
            }
            if (titleVisible) {
                if (showIcon) xOff = (iconSize + 4) * position.scale
                val titleSb = StringBuilder()
                if (titleBold) titleSb.append(ChatColor.BOLD)
                if (titleItalic) titleSb.append(ChatColor.ITALIC)
                if (titleUnderlined) titleSb.append(ChatColor.UNDERLINE)
                titleSb.append(TRANSLATION[effect.translation])
                val amplifier = 1.coerceAtLeast(effect.amplifier + 1)
                if (this.amplifier && (amplifier != 1 || showLvl1)) {
                    titleSb.append(" ")
                    if (amplifierStyle === AmplifierText.ROMAN) titleSb.append(getRoman(amplifier))
                    else titleSb.append(amplifier)
                }
                val builtTitle = titleSb.toString()
                val titleWidth: Int = FONT_RENDERER.width(builtTitle)
                w = w.coerceAtLeast(xOff / position.scale + titleWidth)
                var titleX = x + xOff
                titleX /= position.scale
                var titleY = y + yOff
                if (!durationVisible) titleY += FONT_RENDERER.fontHeight / 2f
                titleY /= position.scale
                drawString(
                    builtTitle,
                    titleX,
                    titleY,
                    titleColor.rgb,
                    titleStyle == TextElement.TextStyle.SHADOW,
                    false,
                    titleStyle == TextElement.TextStyle.BORDER,
                    titleChroma,
                    titleChromaSpeed.get()
                )
            }
            if (durationVisible) {
                if (showIcon) xOff = (iconSize + 4) * position.scale
                val durationSb = StringBuilder()
                if (durationItalic) durationSb.append(ChatColor.ITALIC)
                if (durationUnderlined) durationSb.append(ChatColor.UNDERLINE)
                if (effect.permanent) durationSb.append(permanentText) else durationSb.append(ticksToTime(effect.duration))
                val builtDuration = durationSb.toString()
                val durationWidth = FONT_RENDERER.width(builtDuration)
                w = w.coerceAtLeast(xOff / position.scale + durationWidth)
                var timeX = x + xOff
                timeX /= position.scale
                var timeY: Float = y + yOff + FONT_RENDERER.fontHeight * position.scale + 1
                if (!titleVisible) timeY -= FONT_RENDERER.fontHeight / 2f
                timeY /= position.scale
                if (effect.duration / 20f > blinkingTime || effect.duration % (50 - blinkingSpeed) <= (50 - blinkingSpeed) / 2f) {
                    drawString(
                        builtDuration,
                        timeX,
                        timeY,
                        durationColor.rgb,
                        durationStyle == TextElement.TextStyle.SHADOW,
                        false,
                        durationStyle === TextElement.TextStyle.BORDER,
                        durationChroma,
                        durationChromaSpeed.get(),
                    )
                }
            }
            yOff += yAmt * position.scale
        }
        GL.pop()
    }

    object AmplifierText : OptionContainer() {
        val ARABIC = option("Arabic", "Normal numbers.")
        val ROMAN = option("Roman", "Numbers like IX")
    }

    object PotionSorting : OptionContainer() {
        val ALPHABETICAL = option("Alphabetical", "Sort the potions by the alphabetical order of the name.")
        val DURATION = option("Duration", "Sort the potions by how long they have left.")
        val VANILLA = option("Sort the potions in the way vanilla minecraft does.")
    }

}