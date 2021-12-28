/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.int
import dev.isxander.settxi.impl.string
import net.minecraft.entity.Entity

@ElementMeta(id = "COMBO_DISPLAY", name = "Combo Display", description = "Display how many hits you get on a player before they hit you.", category = "Combat")
class ElementCombo : SimpleTextElement("Combo") {
    var discardTime by int(3) {
        name = "Discard Time"
        category = "Combo"
        description = "How many seconds until the combo is reset."
        range = 0..10
    }

    var noHitMessage by string("0") {
        name = "No Hit Message"
        category = "Combo"
        description = "What message is shown when no combo is in progress."
    }

    private var hitTime = 0L
    private var attackId = 0
    private var currentCombo = 0

    override fun calculateValue(): String {
        if (currentCombo == 0) return noHitMessage
        return currentCombo.toString()
    }

    override fun onClientTick() {
        if (System.currentTimeMillis() - hitTime >= discardTime * 1000L) {
            currentCombo = 0
        }
    }

    override fun onServerDamageEntity(attacker: Entity, victim: Entity) {
        if (attacker.id == mc.player?.id) {
            if (victim.id == attackId) {
                currentCombo++
            } else {
                currentCombo = 1
            }

            hitTime = System.currentTimeMillis()
            attackId = victim.id
        }
    }
}
