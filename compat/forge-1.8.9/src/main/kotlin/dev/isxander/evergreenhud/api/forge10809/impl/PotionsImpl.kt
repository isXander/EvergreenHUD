/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.api.forge10809.impl

import dev.isxander.evergreenhud.api.forge10809.mc
import dev.isxander.evergreenhud.api.forge10809.utils.getEntity
import dev.isxander.evergreenhud.api.gl
import dev.isxander.evergreenhud.api.impl.UEntity
import dev.isxander.evergreenhud.api.impl.UPotion
import dev.isxander.evergreenhud.api.impl.UPotions
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameData
import java.lang.IllegalArgumentException

class PotionsImpl : UPotions() {
    private val effectIcons = ResourceLocation("textures/gui/container/inventory.png")

    override val registeredPotions: List<UPotion> =
        GameData.getPotionRegistry().map { UPotion(it.id, 0, 0, false, it.name, it.isInstant) }

    override fun getEffectsForEntity(entity: UEntity): List<UPotion> {
        val entity = getEntity(entity) as? EntityLivingBase ?: throw IllegalArgumentException("Entity is not living!")
        return entity.activePotionEffects.map { UPotion(it.potionID, it.duration, it.amplifier, it.isPotionDurationMax, it.effectName, false) }
    }

    override fun drawPotionIcon(potion: UPotion, x: Float, y: Float) {
        val index = GameData.getPotionRegistry().getRaw(potion.id).statusIconIndex
        mc.textureManager.bindTexture(effectIcons)
        gl.texModalRect(x, y, index % 8 * 18, 198 + index / 8 * 18, 18f, 18f)
    }
}