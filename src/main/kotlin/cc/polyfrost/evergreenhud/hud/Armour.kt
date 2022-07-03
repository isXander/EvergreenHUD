package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.config.data.InfoType
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.Hud
import cc.polyfrost.oneconfig.libs.universal.UGraphics
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.renderer.RenderManager
import cc.polyfrost.oneconfig.utils.dsl.mc
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

class Armour: Config(Mod("ArmourHud", ModType.HUD), "evergreenhud/armour.json") {
    @HUD(name = "Main")
    var hud = ArmourHud()

    init {
        initialize()
    }

    class ArmourHud : Hud(true) {

        @Transient val diamondHelmet = ItemStack(Items.diamond_helmet)
        @Transient val diamondChestplate = ItemStack(Items.diamond_chestplate)
        @Transient val diamondLeggings = ItemStack(Items.diamond_leggings)
        @Transient val diamondBoots = ItemStack(Items.diamond_boots)
        @Transient val diamondSword = ItemStack(Items.diamond_sword)

        @Info(text = "i know this is terrible please beg xander to help me", type = InfoType.INFO)
        var balls = null

        @Switch(
            name = "Show Helmet"
        )
        var showHelmet = true

        @Switch(
            name = "Show Chestplate"
        )
        var showChestplate = true

        @Switch(
            name = "Show Leggings"
        )
        var showLeggings = true

        @Switch(
            name = "Show Boots"
        )
        var showBoots = true

        @Switch(
            name = "Show Hand Item"
        )
        var showMainHand = true

        @Slider(
            name = "Item Padding",
            min = 0F,
            max = 10F
        )
        var padding = 5

        @DualOption(
            name = "Display Type",
            left = "Down", //lol
            right = "Up"
        )
        var displayType = false //todo why the hell is this a boolean diamond???

        @Dropdown(
            name = "Extra Info",
            options = ["None", "Durability (Absolute)", "Durability (Percent)", "Name"]
        )
        var extraInfo = 0

        @Color(
            name = "Text Color"
        )
        var textColor = OneColor(255, 255, 255)


        @Dropdown(name = "Text Type", options = ["No Shadow", "Shadow", "Full Shadow"])
        var textType = 0

        @Dropdown(
            name = "Text Alignment",
            options = ["Left", "Right"]
        )
        var alignment = 0

        @Transient private var actualWidth = 0
        @Transient private var actualHeight = 0

        override fun draw(matrices: UMatrixStack?, x: Int, y: Int, scale: Float) {
            draw(matrices, x, y, scale, false)
        }

        override fun drawExample(matrices: UMatrixStack?, x: Int, y: Int, scale: Float) {
            draw(matrices, x, y, scale, true)
        }

        private fun draw(matrices: UMatrixStack?, x: Int, y: Int, scale: Float, example: Boolean) {
            val items: List<ItemStack> = if (mc.thePlayer != null && !example) {
                val inventory = mc.thePlayer!!.inventory

                arrayListOf<ItemStack>().run {
                    if (showHelmet) inventory.armorInventory[3]?.let { add(it) }
                    if (showChestplate) inventory.armorInventory[2]?.let { add(it) }
                    if (showLeggings) inventory.armorInventory[1]?.let { add(it) }
                    if (showBoots) inventory.armorInventory[0]?.let { add(it) }
                    if (showMainHand) mc.thePlayer.heldItem?.let { add(it) }

                    if (displayType) reverse()
                    return@run this
                }
            } else {
                arrayListOf<ItemStack>().run {
                    if (showHelmet) add(diamondHelmet)
                    if (showChestplate) add(diamondChestplate)
                    if (showLeggings) add(diamondLeggings)
                    if (showBoots) add(diamondBoots)
                    if (showMainHand) add(diamondSword)

                    if (displayType) reverse()
                    return@run this
                }
            }

            val offset = 16f + padding

            val texts = items.map {
                when (extraInfo) {
                    1 -> if (it.isItemStackDamageable) (it.maxDamage - it.itemDamage).toString() else ""
                    2 -> if (it.isItemStackDamageable) "${(it.maxDamage - it.itemDamage) / it.maxDamage * 100}%" else ""
                    3 -> it.displayName ?: ""
                    else -> ""
                }.let { text ->
                    text to mc.fontRendererObj.getStringWidth(text)
                }
            }
            actualWidth = (texts.maxOfOrNull { mc.fontRendererObj.getStringWidth(it.first) } ?: 0) + 2 + 16
            actualHeight = (items.size * offset - padding * 2).toInt()

            val itemX = when (alignment) {
                0 -> x + 2f
                1 -> x + actualWidth - 16f + 6f
                else -> error("Unknown alignment: $alignment")
            }
            val textAnchor = when (alignment) {
                0 -> 2f + 16f * scale
                1 -> actualWidth - 16f - 2f
                else -> error("Unknown alignment: $alignment")
            }
            items.forEachIndexed { i: Int, stack: ItemStack ->
                val itemY = y + offset * i * scale

                UGraphics.GL.pushMatrix()
                UGraphics.GL.translate((itemX - 4f).toDouble(), (itemY - 4f).toDouble(), 0.0)
                UGraphics.GL.scale(scale, scale, 1f)
                RenderHelper.enableGUIStandardItemLighting()
                mc.renderItem.zLevel = 200f
                mc.renderItem.renderItemAndEffectIntoGUI(stack, 0, 0)
                mc.renderItem.renderItemOverlayIntoGUI(mc.fontRendererObj, stack, 0, 0, "")
                RenderHelper.disableStandardItemLighting()
                UGraphics.GL.popMatrix()

                val (text, textWidth) = texts[i]

                val textX = when (alignment) {
                    0 -> x + textAnchor
                    1 -> x + textAnchor - textWidth
                    else -> error("Unknown alignment: $alignment")
                }

                UGraphics.GL.pushMatrix()
                UGraphics.GL.translate(textX.toDouble(), itemY.toDouble(), 0.0)
                RenderManager.drawScaledString(text, 0f, 0f, textColor.rgb, RenderManager.TextType.toType(textType), scale)
                UGraphics.GL.popMatrix()
            }
        }

        override fun getWidth(scale: Float) = actualWidth

        override fun getHeight(scale: Float) = actualHeight

    }
}