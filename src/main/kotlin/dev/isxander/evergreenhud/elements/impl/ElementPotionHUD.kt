/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.elements.impl

import com.mojang.blaze3d.systems.RenderSystem
import dev.isxander.evergreenhud.annotations.ElementMeta
import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.elements.type.BackgroundElement
import dev.isxander.evergreenhud.elements.type.TextElement.*
import dev.isxander.settxi.impl.*
import dev.isxander.evergreenhud.utils.drawString
import dev.isxander.evergreenhud.utils.getRoman
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.evergreenhud.utils.ticksToTime
import net.minecraft.client.resource.language.I18n
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.awt.Color

@ElementMeta(id = "POTIONS", name = "PotionHUD", category = "Player", description = "Display potions.")
class ElementPotionHUD : BackgroundElement() {
    var titleVisible by boolean(
        default = true,
        name = "Visible",
        category = "Title",
        description = "If the title should be rendered."
    )

    var titleColor by color(
        default = Color(255, 255, 255, 255),
        name = "Color",
        category = "Title",
        description = "Color of the name of the potion effect."
    )

    var titleStyle by option(
        default = TextStyle.SHADOW,
        name = "Style",
        category = "Title",
        description = "In what style should the text be rendered."
    )

    var titleChroma by boolean(
        default = false,
        name = "Chroma",
        category = "Title",
        description = "Makes the text rainbow barf."
    )

    var titleChromaSpeed by float(
        default = 2000f,
        name = "Chroma Speed",
        category = "Title",
        description = "How fast should the chroma wave be?",
        min = 500f,
        max = 10000f
    ) {
        depends { titleChroma }
    }

    var titleBold by boolean(
        default = true,
        name = "Bold",
        category = "Title",
        description = "If the title should be bold."
    )

    var titleUnderlined by boolean(
        default = false,
        name = "Underlined",
        category = "Title",
        description = "If the title should be underlined."
    )

    var titleItalic by boolean(
        default = false,
        name = "Italic",
        category = "Title",
        description = "If the title should be slanted."
    )

    var amplifier by boolean(
        default = true,
        name = "Show Amplifier",
        category = "Title",
        description = "Show the amplified of the potion effect."
    )

    var amplifierStyle by option(
        default = AmplifierText.ARABIC,
        name = "Amplifier Style",
        category = "Title",
        description = "The style of the amplifier to use."
    )

    var showLvl1 by boolean(
        default = true,
        name = "Show LVL 1",
        category = "Title",
        description = "Show the amplifier if it's level is 1."
    )


    var durationVisible by boolean(
        default = true,
        name = "Visible",
        category = "Duration",
        description = "If the duration should be rendered."
    )

    var durationColor by color(
        default = Color(255, 255, 255, 255),
        name = "Color",
        category = "Duration",
        description = "Color of the name of the duration."
    )

    var durationStyle by option(
        default = TextStyle.SHADOW,
        name = "Style",
        category = "Duration",
        description = "In what style should the text be rendered."
    )

    var durationChroma by boolean(
        default = false,
        name = "Chroma",
        category = "Duration",
        description = "Makes the text rainbow barf."
    )

    var durationChromaSpeed by float(
        default = 2000f,
        name = "Chroma Speed",
        category = "Duration",
        description = "How fast should the chroma wave be?",
        min = 500f,
        max = 10000f
    ) {
        depends { durationChroma }
    }

    var durationBold by boolean(
        default = true,
        name = "Bold",
        category = "Duration",
        description = "If the duration should be bold."
    )

    var durationUnderlined by boolean(
        default = false,
        name = "Underlined",
        category = "Duration",
        description = "If the duration should be underlined."
    )

    var durationItalic by boolean(
        default = false,
        name = "Italic",
        category = "Duration",
        description = "If the duration should be slanted."
    )

    var permanentText by string(
        default = "**:**",
        name = "Permanent Text",
        category = "Duration",
        description = "The text to display when the effect is permanent."
    )

    var blinkingTime by int(
        default = 5,
        name = "Blinking Time",
        category = "Duration",
        description = "How many seconds have to remain before the duration starts to flash.",
        min = 0,
        max = 30
    )

    var blinkingSpeed by int(
        default = 30,
        name = "Blinking Speed",
        category = "Duration",
        description = "How fast the duration blinks.",
        min = 10,
        max = 45
    )

    var showIcon by boolean(
        default = true,
        name = "Show Icon",
        category = "Miscellaneous",
        description = "Show the potion icon."
    )

    var sort by option(
        default = PotionSorting.DURATION,
        name = "Sort",
        category = "Miscellaneous",
        description = "How the potion effects should be sorted."
    )

    var invertSort by boolean(
        default = false,
        name = "Invert Sort",
        category = "Miscellaneous",
        description = "Inverts the sorting method.",
    )

    var verticalSpacing by int(
        default = 2,
        name = "Vertical Spacing",
        category = "Miscellaneous",
        description = "How far apart each potion is.",
        min = 1,
        max = 9
    )

    val effectToggles = hashMapOf<Identifier, BooleanSetting>()

    init {
        for (potion in Registry.STATUS_EFFECT.toList()) {
            if (potion.isInstant) continue

            val name = I18n.translate(potion.translationKey)
            effectToggles[Registry.STATUS_EFFECT.getId(potion)!!] = boolean(
                default = true,
                name = name,
                category = "Toggles",
                description = "Display $name on the list when effected.",
            )
        }
    }

    private val iconSize = 18

    private var w = hitboxWidth
    private var h = hitboxHeight
    override val hitboxWidth: Float
        get() = w
    override val hitboxHeight: Float
        get() = h

    override fun render(matrices: MatrixStack, renderOrigin: RenderOrigin) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)

        val x = position.rawX
        val y = position.rawY

        val potionEffects = arrayListOf<StatusEffectInstance>()
        if (mc.player == null) return
        potionEffects.addAll(
            mc.player!!.activeStatusEffects
                .filter { (effect, _) ->
                    effectToggles[Registry.STATUS_EFFECT.getId(effect)]?.get() ?: false
                }
                .map { it.value }
        )

        if (potionEffects.isEmpty()) {
            if (renderOrigin != RenderOrigin.GUI) return

            potionEffects.add(StatusEffectInstance(StatusEffects.STRENGTH, 50, 0))
            potionEffects.add(StatusEffectInstance(StatusEffects.ABSORPTION, 5, 0))
            potionEffects.add(StatusEffectInstance(StatusEffects.HASTE, 2000, 3))
        }

        when (sort) {
            PotionSorting.ALPHABETICAL -> potionEffects.sortBy { I18n.translate(it.translationKey) }
            PotionSorting.DURATION -> potionEffects.sortByDescending(StatusEffectInstance::getDuration)
        }

        if (invertSort) potionEffects.reverse()

        var yOff = 0f
        var xOff = 0f
        val yAmt = iconSize.toFloat() + verticalSpacing

        h = potionEffects.size * yAmt - verticalSpacing
        super.render(matrices, renderOrigin)
        w = 10f

        matrices.push()
        matrices.scale(position.scale, position.scale, 1f)
        for (effect in potionEffects) {
            var iconX = x
            iconX /= position.scale
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
            if (showIcon) {
                val sprite = mc.statusEffectSpriteManager.getSprite(effect.effectType)
                RenderSystem.setShaderTexture(0, sprite.atlas.id)
                matrices.drawSprite(iconX, (y + yOff) / position.scale, 0f, 18f, 18f, sprite)

                xOff = iconSize * position.scale
                w = w.coerceAtLeast(xOff / position.scale)
            }
            if (titleVisible) {
                if (showIcon) xOff = (iconSize + 4) * position.scale
                val titleSb = StringBuilder()
                if (titleBold) titleSb.append(Formatting.BOLD)
                if (titleItalic) titleSb.append(Formatting.ITALIC)
                if (titleUnderlined) titleSb.append(Formatting.UNDERLINE)
                titleSb.append(I18n.translate(effect.translationKey))
                val amplifier = 1.coerceAtLeast(effect.amplifier + 1)
                if (this.amplifier && (amplifier != 1 || showLvl1)) {
                    titleSb.append(" ")
                    if (amplifierStyle === AmplifierText.ROMAN) titleSb.append(getRoman(amplifier))
                    else titleSb.append(amplifier)
                }
                val builtTitle = titleSb.toString()
                val titleWidth: Int = mc.textRenderer.getWidth(builtTitle)
                w = w.coerceAtLeast(xOff / position.scale + titleWidth)
                var titleX = x + xOff
                titleX /= position.scale
                var titleY = y + yOff
                if (!durationVisible) titleY += mc.textRenderer.fontHeight / 2f
                titleY /= position.scale
                drawString(
                    matrices,
                    builtTitle,
                    titleX,
                    titleY,
                    titleColor.rgb,
                    titleStyle == TextStyle.SHADOW,
                    false,
                    titleStyle == TextStyle.BORDER,
                    titleChroma,
                    titleChromaSpeed
                )
            }
            if (durationVisible) {
                if (showIcon) xOff = (iconSize + 4) * position.scale
                val durationSb = StringBuilder()
                if (durationBold) durationSb.append(Formatting.BOLD)
                if (durationItalic) durationSb.append(Formatting.ITALIC)
                if (durationUnderlined) durationSb.append(Formatting.UNDERLINE)
                if (effect.isPermanent) durationSb.append(permanentText) else durationSb.append(ticksToTime(effect.duration))
                val builtDuration = durationSb.toString()
                val durationWidth = mc.textRenderer.getWidth(builtDuration)
                w = w.coerceAtLeast(xOff / position.scale + durationWidth)
                var timeX = x + xOff
                timeX /= position.scale
                var timeY: Float = y + yOff + mc.textRenderer.fontHeight * position.scale + 1
                if (!titleVisible) timeY -= mc.textRenderer.fontHeight / 2f
                timeY /= position.scale
                if (effect.duration / 20f > blinkingTime || effect.duration % (50 - blinkingSpeed) <= (50 - blinkingSpeed) / 2f) {
                    drawString(
                        matrices,
                        builtDuration,
                        timeX,
                        timeY,
                        durationColor.rgb,
                        durationStyle == TextStyle.SHADOW,
                        false,
                        durationStyle == TextStyle.BORDER,
                        durationChroma,
                        durationChromaSpeed,
                    )
                }
            }
            yOff += yAmt * position.scale
        }
        matrices.pop()
    }

    object AmplifierText : OptionContainer() {
        val ARABIC = option("Arabic", "Normal numbers.")
        val ROMAN = option("Roman", "Numbers like IX")
    }

    object PotionSorting : OptionContainer() {
        val ALPHABETICAL = option("Alphabetical", "Sort the potions by the alphabetical order of the name.")
        val DURATION = option("Duration", "Sort the potions by how long they have left.")
        val VANILLA = option("Vanilla", "Sort the potions in the way vanilla minecraft does.")
    }
}
