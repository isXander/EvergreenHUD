/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.elements.type.BackgroundElement
import dev.isxander.evergreenhud.elements.type.TextElement.TextStyle
import dev.isxander.evergreenhud.settings.color
import dev.isxander.evergreenhud.utils.*
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.settxi.impl.*
import gg.essential.universal.ChatColor
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.ResourceLocation


@ElementMeta(id = "evergreenhud:potion_hud", name = "PotionHUD", category = "Player", description = "Display potions.")
class ElementPotionHUD : BackgroundElement() {
    val EFFECTS_RESOURCE = ResourceLocation("textures/gui/container/inventory.png")

    var titleVisible by boolean(true) {
        name = "Visible"
        category = "Title"
        description = "If the title should be rendered."
    }

    var titleColor by color(Color.white) {
        name = "Color"
        category = "Title"
        description = "Color of the name of the potion effect."
        canHaveChroma = true
    }

    var titleStyle by option(TextStyle.SHADOW) {
        name = "Style"
        category = "Title"
        description = "In what style should the text be rendered."
    }

    var titleBold by boolean(true) {
        name = "Bold"
        category = "Title"
        description = "If the title should be bold."
    }

    var titleUnderlined by boolean(false) {
        name = "Underlined"
        category = "Title"
        description = "If the title should be underlined."
    }

    var titleItalic by boolean(false) {
        name = "Italic"
        category = "Title"
        description = "If the title should be slanted."
    }

    var amplifier by boolean(true) {
        name = "Show Amplifier"
        category = "Title"
        description = "Show the amplified of the potion effect."
    }

    var amplifierStyle by option(AmplifierText.ARABIC) {
        name = "Amplifier Style"
        category = "Title"
        description = "The style of the amplifier to use."
    }

    var showLvl1 by boolean(true) {
        name = "Show LVL 1"
        category = "Title"
        description = "Show the amplifier if it's level is 1."
    }


    var durationVisible by boolean(true) {
        name = "Visible"
        category = "Duration"
        description = "If the duration should be rendered."
    }

    var durationColor by color(Color.white) {
        name = "Color"
        category = "Duration"
        description = "Color of the name of the duration."
        canHaveChroma = true
    }

    var durationStyle by option(TextStyle.SHADOW) {
        name = "Style"
        category = "Duration"
        description = "In what style should the text be rendered."
    }

    var durationBold by boolean(true) {
        name = "Bold"
        category = "Duration"
        description = "If the duration should be bold."
    }

    var durationUnderlined by boolean(false) {
        name = "Underlined"
        category = "Duration"
        description = "If the duration should be underlined."
    }

    var durationItalic by boolean(false) {
        name = "Italic"
        category = "Duration"
        description = "If the duration should be slanted."
    }

    var permanentText by string("**:**") {
        name = "Permanent Text"
        category = "Duration"
        description = "The text to display when the effect is permanent."
    }

    var blinkingTime by int(5) {
        name = "Blinking Time"
        category = "Duration"
        description = "How many seconds have to remain before the duration starts to flash."
        range = 0..30
    }

    var blinkingSpeed by int(30) {
        name = "Blinking Speed"
        category = "Duration"
        description = "How fast the duration blinks."
        range = 10..45
    }

    var showIcon by boolean(true) {
        name = "Show Icon"
        category = "Miscellaneous"
        description = "Show the potion icon."
    }

    var sort by option(PotionSorting.DURATION) {
        name = "Sort"
        category = "Miscellaneous"
        description = "How the potion effects should be sorted."
    }

    var invertSort by boolean(false) {
        name = "Invert Sort"
        category = "Miscellaneous"
        description = "Inverts the sorting method."
    }

    var verticalSpacing by int(2) {
        name = "Vertical Spacing"
        category = "Miscellaneous"
        description = "How far apart each potion is."
        range = 1..9
    }

    val effectToggles = hashMapOf<Int, BooleanSetting>()

    init {
        for (potion in Potion.potionTypes.toList()) {
            if (potion.isInstant) continue

            val name = I18n.format(potion.name)
            effectToggles[potion.id] = boolean(true) {
                this.name = name
                category = "Toggles"
                description = "Display $name on the list when effected."
            }
        }
    }

    private val iconSize = 18

    private var w = hitboxWidth
    private var h = hitboxHeight
    override val hitboxWidth: Float
        get() = w
    override val hitboxHeight: Float
        get() = h

    override fun render(renderOrigin: RenderOrigin) {
        GlStateManager.color(1f, 1f, 1f, 1f)

        val x = position.rawX
        val y = position.rawY

        val potionEffects = arrayListOf<PotionEffect>()
        if (mc.thePlayer == null) return
        potionEffects.addAll(
            mc.thePlayer!!.activePotionEffects
                .filter { effect: PotionEffect ->
                    effectToggles[effect.potionID]?.get() ?: false
                }
        )

        if (potionEffects.isEmpty()) {
            if (renderOrigin != RenderOrigin.GUI) return

            potionEffects.add(PotionEffect(Potion.damageBoost.id, 50, 4))
            potionEffects.add(PotionEffect(Potion.moveSpeed.id, 5000, 2))
            val effect = PotionEffect(Potion.saturation.id, 10000000, 0)
            effect.setPotionDurationMax(true)
            potionEffects.add(effect)
            potionEffects.add(PotionEffect(Potion.absorption.id, 242, 1))
        }

        when (sort) {
            PotionSorting.ALPHABETICAL -> potionEffects.sortBy { I18n.format(it.effectName) }
            PotionSorting.DURATION -> potionEffects.sortByDescending(PotionEffect::getDuration)
        }

        if (invertSort) potionEffects.reverse()

        var yOff = 0f
        var xOff = 0f
        val yAmt = iconSize.toFloat() + verticalSpacing

        h = potionEffects.size * yAmt - verticalSpacing
        super.render(renderOrigin)
        w = 10f

        GlStateManager.pushMatrix()
        GlStateManager.scale(position.scale, position.scale, 1f)
        for (effect in potionEffects) {
            val potion = Potion.potionTypes[effect.potionID]
            var iconX = x
            iconX /= position.scale
            GlStateManager.color(1f, 1f, 1f, 1f)
            if (showIcon) {
                mc.textureManager.bindTexture(EFFECTS_RESOURCE)
                drawTexturedModalRectF(iconX, (y + yOff) / position.scale, potion.statusIconIndex % 8 * 18, 198 + potion.statusIconIndex / 8 * 18, 18F, 18F)
                xOff = iconSize * position.scale
                w = w.coerceAtLeast(xOff / position.scale)
            }
            if (titleVisible) {
                if (showIcon) xOff = (iconSize + 4) * position.scale
                val titleSb = StringBuilder()
                if (titleBold) titleSb.append(ChatColor.BOLD)
                if (titleItalic) titleSb.append(ChatColor.ITALIC)
                if (titleUnderlined) titleSb.append(ChatColor.UNDERLINE)
                titleSb.append(I18n.format(effect.effectName))
                val amplifier = 1.coerceAtLeast(effect.amplifier + 1)
                if (this.amplifier && (amplifier != 1 || showLvl1)) {
                    titleSb.append(" ")
                    if (amplifierStyle === AmplifierText.ROMAN) titleSb.append(getRoman(amplifier))
                    else titleSb.append(amplifier)
                }
                val builtTitle = titleSb.toString()
                val titleWidth: Int = mc.fontRendererObj.getStringWidth(builtTitle)
                w = w.coerceAtLeast(xOff / position.scale + titleWidth)
                var titleX = x + xOff
                titleX /= position.scale
                var titleY = y + yOff
                if (!durationVisible) titleY += mc.fontRendererObj.FONT_HEIGHT / 2f
                titleY /= position.scale
                drawString(
                    builtTitle,
                    titleX,
                    titleY,
                    titleColor.rgba,
                    titleStyle == TextStyle.SHADOW,
                    false,
                    titleStyle == TextStyle.BORDER,
                    titleColor.chroma.hasChroma,
                    titleColor.chroma.chromaSpeed
                )
            }
            if (durationVisible) {
                if (showIcon) xOff = (iconSize + 4) * position.scale
                val durationSb = StringBuilder()
                if (durationBold) durationSb.append(ChatColor.BOLD)
                if (durationItalic) durationSb.append(ChatColor.ITALIC)
                if (durationUnderlined) durationSb.append(ChatColor.UNDERLINE)
                if (effect.isPotionDurationMax) durationSb.append(permanentText) else durationSb.append(ticksToTime(effect.duration))
                val builtDuration = durationSb.toString()
                val durationWidth = mc.fontRendererObj.getStringWidth(builtDuration)
                w = w.coerceAtLeast(xOff / position.scale + durationWidth)
                var timeX = x + xOff
                timeX /= position.scale
                var timeY: Float = y + yOff + mc.fontRendererObj.FONT_HEIGHT * position.scale + 1
                if (!titleVisible) timeY -= mc.fontRendererObj.FONT_HEIGHT / 2f
                timeY /= position.scale
                if (effect.duration / 20f > blinkingTime || effect.duration % (50 - blinkingSpeed) <= (50 - blinkingSpeed) / 2f) {
                    drawString(
                        builtDuration,
                        timeX,
                        timeY,
                        durationColor.rgba,
                        durationStyle == TextStyle.SHADOW,
                        false,
                        durationStyle == TextStyle.BORDER,
                        durationColor.chroma.hasChroma,
                        durationColor.chroma.chromaSpeed,
                    )
                }
            }
            yOff += yAmt * position.scale
        }
        GlStateManager.popMatrix()
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
