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
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.drawStringExt
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.OptionContainer
import dev.isxander.settxi.impl.boolean
import dev.isxander.settxi.impl.int
import dev.isxander.settxi.impl.option
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.init.Items
import net.minecraft.item.ItemStack


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

    override fun render(renderOrigin: RenderOrigin) {
        val items: List<ItemStack> = if (mc.thePlayer != null && renderOrigin != RenderOrigin.GUI) {
            val inventory = mc.thePlayer!!.inventory

            buildList {
                if (showHelmet) inventory.armorItemInSlot(3).takeIf { it != null }?.let { add(it) }
                if (showChestplate) inventory.armorItemInSlot(2).takeIf { it != null }?.let { add(it) }
                if (showLeggings) inventory.armorItemInSlot(1).takeIf { it != null }?.let { add(it) }
                if (showBoots) inventory.armorItemInSlot(0).takeIf { it != null }?.let { add(it) }
                if (showMainHand) inventory.getCurrentItem().takeIf { it != null }?.let { add(it) }

                if (displayType == DisplayType.Up) reverse()
            }
        } else {
            buildList {
                if (showHelmet) add(ItemStack(Items.diamond_helmet))
                if (showChestplate) add(ItemStack(Items.diamond_chestplate))
                if (showLeggings) add(ItemStack(Items.diamond_leggings))
                if (showBoots) add(ItemStack(Items.diamond_boots))
                if (showMainHand) add(ItemStack(Items.diamond_sword))

                if (displayType == DisplayType.Up) reverse()
            }
        }

        if (items.isNotEmpty()) super.render(renderOrigin)

        val x = position.rawX
        val y = position.rawY

        val offset = 16f + padding

        val texts = items.map {
            when (extraInfo) {
                ExtraInfo.DurabilityAbsolute -> if (it.isItemStackDamageable) (it.maxDamage - it.itemDamage).toString() else ""
                ExtraInfo.DurabilityPercent -> if (it.isItemStackDamageable) "${(it.maxDamage - it.itemDamage) / it.maxDamage * 100}%" else ""
                ExtraInfo.Name -> it.displayName
                else -> ""
            }.let { text ->
                text to mc.fontRendererObj.getStringWidth(text)
            }
        }
        hitboxWidth = (texts.maxOfOrNull { mc.fontRendererObj.getStringWidth(it.first) } ?: 0) + 2f + 16f
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
        val itemRenderer = mc.renderItem
        for (stack in items) {
            i++

            val itemY = y + offset * i * position.scale

            GlStateManager.pushMatrix()
            GlStateManager.translate((itemX - 4f).toDouble(), (itemY - 4f).toDouble(), 0.0)
            GlStateManager.scale(position.scale, position.scale, position.scale)
            RenderHelper.enableGUIStandardItemLighting()
            itemRenderer.zLevel = 200f
            itemRenderer.renderItemAndEffectIntoGUI(stack, 0, 0)
            itemRenderer.renderItemOverlayIntoGUI(mc.fontRendererObj, stack, 0, 0, "")
            RenderHelper.disableStandardItemLighting()
            GlStateManager.popMatrix()

            val (text, textWidth) = texts[i]

            val textX = when (alignment) {
                Alignment.LEFT -> x + textAnchor
                Alignment.RIGHT -> x + textAnchor - textWidth
                else -> error("Unknown alignment: ${alignment.id}")
            }

            GlStateManager.pushMatrix()
            GlStateManager.translate(textX.toDouble(), itemY.toDouble(), 0.0)
            GlStateManager.scale(position.scale, position.scale, position.scale)
            drawStringExt(
                text,
                0f, 0f,
                textColor.rgba,
//                centered = alignment == Alignment.CENTER,
                shadow = textStyle == TextElement.TextStyle.SHADOW,
                bordered = textStyle == TextElement.TextStyle.BORDER,
                chroma = textColor.chroma
            )
            GlStateManager.popMatrix()
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
