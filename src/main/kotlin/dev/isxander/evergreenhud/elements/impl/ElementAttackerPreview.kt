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
import dev.isxander.evergreenhud.event.ClientDamageEntityEvent
import dev.isxander.evergreenhud.event.ClientTickEvent
import dev.isxander.evergreenhud.event.EntityDamagedEvent
import dev.isxander.evergreenhud.event.ServerDamageEntityEvent
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.evergreenhud.utils.renderEntity
import dev.isxander.settxi.impl.int
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.EntityDamageSource

@ElementMeta(id = "ATTACKER_PREVIEW", name = "Attacker Preview", description = "Show the model of the person who is currently attacking you.", category = "Player")
class ElementAttackerPreview : BackgroundElement() {
    var rotation by int(0) {
        name = "Rotation"
        description = "The rotation of the player model."
        category = "Player Preview"
        range = 0..360
    }

    override val hitboxWidth = 80f
    override val hitboxHeight = 120f

    private var lastHit = 0L

    var attacker by eventReturnable<EntityDamagedEvent, LivingEntity?>(null, { it.entity == mc.player && it.source is EntityDamageSource }) {
        lastHit = System.currentTimeMillis()
        (it.source as EntityDamageSource).attacker as? LivingEntity
    }

    val clientTickEvent by event<ClientTickEvent> {
        if (System.currentTimeMillis() - lastHit > 3000) {
            attacker = null
        }
    }

    override fun render(matrices: MatrixStack, renderOrigin: RenderOrigin) {
        val entity = attacker ?: mc.player?.takeIf { renderOrigin == RenderOrigin.GUI } ?: return
        val size = position.scale * 50
        val rotation = 360f - rotation

        super.render(matrices, renderOrigin)

        entity.renderEntity(
            position.rawX + hitboxWidth / 2 * position.scale,
            position.rawY + hitboxHeight * position.scale - 15f * position.scale,
            size,
            rotation,
        )
    }
}
