/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.item.ItemStack


/*
 * Taken from KronHUD under CC Attribution-NonCommercial 3.0.
 * Modified in Kotlin
 * https://github.com/Moulberry/NotEnoughUpdates/blob/master/LICENSE
 */
fun drawItemStack(stack: ItemStack?, x: Int, y: Int) {
    if (stack == null) return
    val itemRender = Minecraft.getMinecraft().renderItem
    RenderHelper.enableGUIStandardItemLighting()
    itemRender.zLevel = -145f //Negates the z-offset of the below method.
    itemRender.renderItemAndEffectIntoGUI(stack, x, y)
    itemRender.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRendererObj, stack, x, y, null)
    itemRender.zLevel = 0f
    RenderHelper.disableStandardItemLighting()
}
