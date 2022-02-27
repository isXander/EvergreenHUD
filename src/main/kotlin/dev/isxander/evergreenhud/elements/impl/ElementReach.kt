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
import dev.isxander.evergreenhud.utils.decimalFormat
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.boolean
import dev.isxander.settxi.impl.int
import dev.isxander.settxi.impl.string
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.Vec3


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

    var reach: String? by eventReturnable<ClientDamageEntityEvent, String>(noHitMessage, { it.attacker == mc.thePlayer }) { event ->
        val hitResult = getReachDistanceFromEntity(event.attacker) ?: return@eventReturnable reach
        lastHit = System.currentTimeMillis()
        decimalFormat(accuracy, trailingZeros).format(hitResult)
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

    private fun getReachDistanceFromEntity(entity: Entity): Double? {
        mc.mcProfiler.startSection("Calculate Reach Dist")

        // How far will ray travel before ending
        val maxSize = 6.0 // use 6 because creative mode is 6 and any more is literally reach
        // Bounding box of entity
        val otherBB: AxisAlignedBB = entity.entityBoundingBox
        // This is where people found out that F3+B is not accurate for hitboxes,
        // it makes hitboxes bigger by certain amount
        val collisionBorderSize: Float = entity.collisionBorderSize
        val otherHitbox = otherBB.expand(
            collisionBorderSize.toDouble(),
            collisionBorderSize.toDouble(),
            collisionBorderSize.toDouble()
        )
        // Not quite sure what the difference is between these two vectors
        // In actual code where this is taken from, partialTicks is always 1.0
        // So this won't decrease accuracy
        val eyePos = mc.thePlayer.getPositionEyes(1.0f)
        val lookPos = mc.thePlayer.getLook(1.0f)
        // Get vector for raycast
        val adjustedPos = eyePos.addVector(lookPos.xCoord * maxSize, lookPos.yCoord * maxSize, lookPos.zCoord * maxSize)
        val movingObjectPosition = otherHitbox.calculateIntercept(eyePos, adjustedPos) ?: return null
        // This will trigger if hit distance is more than maxSize
        val otherEntityVec: Vec3 = movingObjectPosition.hitVec
        // finally calculate distance between both vectors
        val dist = eyePos.distanceTo(otherEntityVec)
        mc.mcProfiler.endSection()
        return dist
    }
}
