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
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffects
import java.lang.IllegalArgumentException

class PotionsImpl : UPotions() {
    override val registeredPotions: List<UPotion> =
        UStatusEffects.values().map { UPotion(it.ordinal, 0, 0, false, it.effect.translationKey, it.effect.isInstant) }

    override fun getEffectsForEntity(entity: UEntity): List<UPotion> {
        val entity = getEntity(entity) as? LivingEntity ?: throw IllegalArgumentException("Entity was not living!")
        return entity.activeStatusEffects.map { (status, instance) -> UPotion(UStatusEffects.getFromVanilla(status).ordinal, instance.duration, instance.amplifier, instance.isPermanent, status.translationKey, status.isInstant) }
    }

    override fun drawPotionIcon(potion: UPotion, x: Float, y: Float) {
        val effectManager = mc.statusEffectSpriteManager
        val sprite = effectManager.getSprite(UStatusEffects.values()[potion.id].effect)
        RenderSystem.setShaderTexture(0, sprite.atlas.id)
        DrawableHelper.drawSprite(Main.matrices, x.toInt(), y.toInt(), 0, 18, 18, sprite)
    }
}

private enum class UStatusEffects(val effect: StatusEffect) {
    SPEED(StatusEffects.SPEED),
    SLOWNESS(StatusEffects.SLOWNESS),
    HASTE(StatusEffects.HASTE),
    MINING_FATIGUE(StatusEffects.MINING_FATIGUE),
    STRENGTH(StatusEffects.STRENGTH),
    INSTANT_HEALTH(StatusEffects.INSTANT_HEALTH),
    INSTANT_DAMAGE(StatusEffects.INSTANT_DAMAGE),
    JUMP_BOOST(StatusEffects.JUMP_BOOST),
    NAUSEA(StatusEffects.NAUSEA),
    REGENERATION(StatusEffects.REGENERATION),
    RESISTANCE(StatusEffects.RESISTANCE),
    FIRE_RESISTANCE(StatusEffects.FIRE_RESISTANCE),
    WATER_BREATHING(StatusEffects.WATER_BREATHING),
    INVISIBILITY(StatusEffects.INVISIBILITY),
    BLINDNESS(StatusEffects.BLINDNESS),
    NIGHT_VISION(StatusEffects.NIGHT_VISION),
    HUGER(StatusEffects.HUNGER),
    WEAKNESS(StatusEffects.WEAKNESS),
    POISON(StatusEffects.POISON),
    WITHER(StatusEffects.WITHER),
    HEALTH_BOOST(StatusEffects.HEALTH_BOOST),
    ABSORPTION(StatusEffects.ABSORPTION),
    SATURATION(StatusEffects.SATURATION),
    GLOWING(StatusEffects.GLOWING),
    LEVITATION(StatusEffects.LEVITATION),
    LUCK(StatusEffects.LUCK),
    UNLUCK(StatusEffects.UNLUCK),
    SLOW_FALLING(StatusEffects.SLOW_FALLING),
    CONDUIT_POWER(StatusEffects.CONDUIT_POWER),
    DOLPHINS_GRACE(StatusEffects.DOLPHINS_GRACE),
    BAD_OMEN(StatusEffects.BAD_OMEN),
    HERO_OF_THE_VILLAGE(StatusEffects.HERO_OF_THE_VILLAGE);

    companion object {
        fun getFromVanilla(effect: StatusEffect): UStatusEffects {
            for (u in values()) if (u.effect.type == effect.type) return u
            throw IllegalArgumentException("Not a known status effect!")
        }
    }
}