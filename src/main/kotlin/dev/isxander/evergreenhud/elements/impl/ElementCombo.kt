/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.event.ClientTickEvent
import dev.isxander.evergreenhud.event.ServerDamageEntityEvent
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

    val clientTickEvent by event<ClientTickEvent>({ System.currentTimeMillis() - hitTime >= discardTime * 1000L }) {
        currentCombo = 0
    }

    val serverDamageEntityEvent by event<ServerDamageEntityEvent> {
        if (it.attacker == mc.player) {
            if (it.victim.id == attackId) {
                currentCombo++
            } else {
                currentCombo = 1
            }

            hitTime = System.currentTimeMillis()
            attackId = it.victim.id
        } else if (it.victim == mc.player) {
            currentCombo = 0
            attackId = 0
        }
    }

    override fun calculateValue(): String {
        if (currentCombo == 0) return noHitMessage
        return currentCombo.toString()
    }
}
