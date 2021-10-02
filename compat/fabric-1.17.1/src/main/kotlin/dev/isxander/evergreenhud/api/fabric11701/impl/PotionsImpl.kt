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

package dev.isxander.evergreenhud.api.fabric11701.impl

import com.mojang.blaze3d.systems.RenderSystem
import dev.isxander.evergreenhud.api.impl.UEntity
import dev.isxander.evergreenhud.api.impl.UPotion
import dev.isxander.evergreenhud.api.impl.UPotions
import dev.isxander.evergreenhud.api.fabric11701.Main
import dev.isxander.evergreenhud.api.fabric11701.mc
import dev.isxander.evergreenhud.api.fabric11701.utils.getEntity
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.entity.LivingEntity
import net.minecraft.util.registry.Registry
import java.lang.IllegalArgumentException

class PotionsImpl : UPotions() {
    override val registeredPotions: List<UPotion> = run {
        Registry.STATUS_EFFECT
            .toList()
            .map {
                UPotion(Registry.STATUS_EFFECT.getRawId(it), 0, 0, false, it.translationKey, it.isInstant)
            }
    }

    override fun getEffectsForEntity(_entity: UEntity): List<UPotion> {
        val entity = getEntity(_entity) as? LivingEntity ?: throw IllegalArgumentException("Entity was not living!")
        return entity.activeStatusEffects.map { (status, instance) -> UPotion(
                Registry.STATUS_EFFECT.getRawId(status),
                instance.duration,
                instance.amplifier,
                instance.isPermanent,
                status.translationKey,
                status.isInstant,
       )}
    }

    override fun drawPotionIcon(potion: UPotion, x: Float, y: Float) {
        val effectManager = mc.statusEffectSpriteManager
        val sprite = effectManager.getSprite(Registry.STATUS_EFFECT.find { Registry.STATUS_EFFECT.getRawId(it) == potion.id })
        RenderSystem.setShaderTexture(0, sprite.atlas.id)
        DrawableHelper.drawSprite(Main.matrices, x.toInt(), y.toInt(), 0, 18, 18, sprite)
    }
}
