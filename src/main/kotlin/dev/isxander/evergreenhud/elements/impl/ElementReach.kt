/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.event.ClientDamageEntityEvent
import dev.isxander.evergreenhud.event.ClientTickEvent
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.boolean
import dev.isxander.settxi.impl.int
import dev.isxander.settxi.impl.string
import net.minecraft.entity.projectile.ProjectileUtil
import java.text.DecimalFormat

@ElementMeta(id = "evergreenhud:reach", name = "Reach Display", description = "Displays the reach of the last attacked entity.", category = "Player")
class ElementReach : SimpleTextElement("Reach") {
    var trailingZeros by boolean(false) {
        name = "Trailing Zeros"
        description = "Keeps the number of decimal places consistent to the accuracy."
        category = "Reach Display"
    }

    var accuracy by int(1) {
        name = "Accuracy"
        description = "The number of decimal places to show."
        category = "Reach Display"
        range = 0..15
    }

    var discardTime by int(3000) {
        name = "Discard Time"
        description = "The time in milliseconds to wait before discarding the last reach value."
        category = "Reach Display"
        range = 0..10000
    }

    var noHitMessage by string("0") {
        name = "No Hit Message"
        description = "The message to display when there is no last hit."
        category = "Reach Display"
    }

    private var lastHit = System.currentTimeMillis()

    var reach: String? by eventReturnable<ClientDamageEntityEvent, String>(noHitMessage, { it.attacker == mc.player }) { event ->
        val maxReach = 6.0

        val cameraPos = event.attacker.getCameraPosVec(mc.tickDelta)
        val rotationVec = event.attacker.getRotationVec(1f)
        val reachCamera = cameraPos.add(rotationVec.multiply(maxReach))
        val box = event.attacker.boundingBox.stretch(rotationVec.multiply(maxReach)).expand(1.0)

        val hitResult = ProjectileUtil.raycast(event.attacker, cameraPos, reachCamera, box, { it == event.victim }, maxReach * maxReach) ?: return@eventReturnable reach

        val reach = cameraPos.distanceTo(hitResult.pos)
        lastHit = System.currentTimeMillis()
        getDecimalFormat().format(reach)
    }

    val clientTickEvent by event<ClientTickEvent> {
        if (System.currentTimeMillis() - lastHit > discardTime) {
            lastHit = System.currentTimeMillis()
            reach = noHitMessage
        }
    }

    override fun calculateValue(): String {
        return reach!!
    }

    private fun getDecimalFormat(): DecimalFormat {
        val formatter = if (trailingZeros) "0" else "#"
        val formatBuilder = StringBuilder(if (accuracy < 1) formatter else "$formatter.")
        for (i in 0 until accuracy) formatBuilder.append(formatter)
        return DecimalFormat(formatBuilder.toString())
    }
}
