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
import dev.isxander.evergreenhud.elements.type.TextElement
import dev.isxander.evergreenhud.settings.color
import dev.isxander.evergreenhud.utils.*
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.settxi.impl.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.LiteralText

@ElementMeta(id = "evergreenhud:armour_hud", name = "Armour HUD", description = "Displays the player's currently equipped armour.", category = "Player")
class ElementArmourHUD : BackgroundElement() {
    var showHelmet by boolean(true) {
        name = "Show Helmet"
        description = "Render what you're wearing on your head."
        category = "ArmourHUD"
    }
    var showChestplate by boolean(true) {
        name = "Show Chestplate"
        description = "Render what you're wearing on your chest."
        category = "ArmourHUD"
    }
    var showLeggings by boolean(true) {
        name = "Show Leggings"
        description = "Render what you're wearing on your legs."
        category = "ArmourHUD"
    }
    var showBoots by boolean(true) {
        name = "Show Boots"
        description = "Render what you're wearing on your feed."
        category = "ArmourHUD"
    }
    var showMainHand by boolean(true) {
        name = "Show Main Hand"
        description = "Render what you're holding in your main hand."
        category = "ArmourHUD"
    }
    var showOffHand by boolean(true) {
        name = "Show Off-Hand"
        description = "Render what you're holding in your off-hand."
        category = "ArmourHUD"
    }

    var padding by int(5) {
        name = "Padding"
        description = "The spacing between each item."
        category = "ArmourHUD"
        range = 0..10
    }
    var displayType by option(DisplayType.Down) {
        name = "Display Type"
        description = "How the items are displayed."
        category = "ArmourHUD"
    }
    var extraInfo by option(ExtraInfo.DurabilityAbsolute) {
        name = "Extra Info"
        description = "What other information to display about each armour piece."
        category = "ArmourHUD"
    }
    var showDurabilityBar by boolean(true) {
        name = "Show Durability Bar"
        description = "Render the durability bar for each armour piece."
        category = "ArmourHUD"
    }

    var textColor by color(Color.white) {
        name = "Color"
        category = "Text"
        description = "The color of the text."
        canHaveChroma = true
    }
    var textStyle by option(TextElement.TextStyle.SHADOW) {
        name = "Text Style"
        category = "Text"
        description = "What style text is rendered in."
    }
    var alignment by option(Alignment.LEFT) {
        name = "Alignment"
        category = "Text"
        description = "How the text is aligned."
    }

    override var hitboxWidth: Float = 10f
    override var hitboxHeight: Float = 10f

    override fun render(matrices: MatrixStack, renderOrigin: RenderOrigin) {
        val items: List<ItemStack> = if (mc.player != null && renderOrigin != RenderOrigin.GUI) {
            val inventory = mc.player!!.inventory

            buildList {
                if (showHelmet) inventory.getArmorStack(3).takeIf { !it.isEmpty }?.let { add(it) }
                if (showChestplate) inventory.getArmorStack(2).takeIf { !it.isEmpty }?.let { add(it) }
                if (showLeggings) inventory.getArmorStack(1).takeIf { !it.isEmpty }?.let { add(it) }
                if (showBoots) inventory.getArmorStack(0).takeIf { !it.isEmpty }?.let { add(it) }
                if (showMainHand) mc.player!!.mainHandStack.takeIf { !it.isEmpty }?.let { add(it) }
                if (showOffHand) mc.player!!.offHandStack.takeIf { !it.isEmpty }?.let { add(it) }

                if (displayType == DisplayType.Up) reverse()
            }
        } else {
            buildList {
                if (showHelmet) add(Items.NETHERITE_HELMET.defaultStack)
                if (showChestplate) add(Items.NETHERITE_CHESTPLATE.defaultStack)
                if (showLeggings) add(Items.NETHERITE_LEGGINGS.defaultStack)
                if (showBoots) add(Items.NETHERITE_BOOTS.defaultStack)
                if (showMainHand) add(Items.NETHERITE_SWORD.defaultStack)
                if (showOffHand) add(Items.SHIELD.defaultStack)

                if (displayType == DisplayType.Up) reverse()
            }
        }

        if (items.isNotEmpty()) super.render(matrices, renderOrigin)

        val x = position.rawX
        val y = position.rawY

        val offset = 16f + padding

        val texts = items.map {
            when (extraInfo) {
                ExtraInfo.DurabilityAbsolute -> LiteralText(if (it.isDamageable) (it.maxDamage - it.damage).toString() else "")
                ExtraInfo.DurabilityPercent -> LiteralText(if (it.isDamageable) "${(it.maxDamage - it.damage) / it.maxDamage * 100}%" else "")
                ExtraInfo.Name -> it.name ?: LiteralText("")
                else -> LiteralText("")
            }.let { text ->
                text to mc.textRenderer.getWidth(text)
            }
        }
        hitboxWidth = (texts.maxOfOrNull { mc.textRenderer.getWidth(it.first) } ?: 0) + 2f + 16f
        hitboxHeight = items.size * offset - padding * 2

        var i = -1

        val itemX = when (alignment) {
            Alignment.LEFT -> x + 2f
            Alignment.RIGHT -> x + hitboxWidth - 16f + 6f
            else -> error("Unknown alignment: ${alignment.id}")
        }
        val textAnchor = when (alignment) {
            Alignment.LEFT -> 2f + 16f * position.scale
            Alignment.RIGHT -> hitboxWidth - 16f - 2f
            else -> error("Unknown alignment: ${alignment.id}")
        }
        for (stack in items) {
            i++

            val itemY = y + offset * i * position.scale

            matrices.push()
            matrices.translate(itemX - 4f, itemY - 4f)
            matrices.scale(position.scale, position.scale, 1f)
            renderGuiItemModel(matrices, stack, 0f, 0f)
            renderGuiItemOverlay(matrices, stack, 0f, 0f, null, showDurabilityBar, -1, true)
            matrices.pop()

            val (text, textWidth) = texts[i]

            val textX = when (alignment) {
                Alignment.LEFT -> x + textAnchor
                Alignment.RIGHT -> x + textAnchor - textWidth
                else -> error("Unknown alignment: ${alignment.id}")
            }

            matrices.push()
            matrices.translate(textX, itemY)
            matrices.scale(position.scale, position.scale, 1f)
            drawString(
                matrices,
                text,
                0f, 0f,
                textColor.rgba,
//                centered = alignment == Alignment.CENTER,
                shadow = textStyle == TextElement.TextStyle.SHADOW,
                bordered = textStyle == TextElement.TextStyle.BORDER,
                chroma = textColor.chroma
            )
            matrices.pop()
        }
    }

    object DisplayType : OptionContainer() {
        val Down = option("Down")
        val Up = option("Up")
    }

    object ExtraInfo : OptionContainer() {
        val None = option("None")
        val DurabilityAbsolute = option("Durability (Absolute)")
        val DurabilityPercent = option("Durability (Percent)")
        val Name = option("Name")
    }

    object Alignment : OptionContainer() {
        val LEFT = option("Left")
        val RIGHT = option("Right")
    }
}
