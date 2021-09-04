/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.api.forge10809.impl

import dev.isxander.evergreenhud.api.forge10809.mc
import dev.isxander.evergreenhud.api.impl.UEntity
import dev.isxander.evergreenhud.api.profiler
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.Vec3
import java.lang.IllegalStateException

class EntityImpl(val entity: Entity) : UEntity() {

    override val isNull: Boolean = false

    override val x: Double = entity.posX
    override val y: Double = entity.posY
    override val z: Double = entity.posZ

    override val prevX: Double = entity.prevPosX
    override val prevY: Double = entity.prevPosY
    override val prevZ: Double = entity.prevPosZ

    override val id: Int = entity.entityId

    override val yaw: Float = entity.rotationYaw
    override val pitch: Float = entity.rotationPitch

    override fun getReachDistFromEntity(entity: UEntity): Double {
        profiler.push("Calculate Reach Distance")
        if (entity !is EntityImpl) {
            throw IllegalStateException("Universal entity type from unknown origin")
        }

        // How far will ray travel before ending
        val maxSize = 6.0 // use 6 because creative mode is 6 and any more is literally reach

        // Bounding box of entity
        val otherBB: AxisAlignedBB = entity.entity.entityBoundingBox
        // This is where people found out that F3+B is not accurate for hitboxes,
        // it makes hitboxes bigger by certain amount
        val collisionBorderSize: Float = entity.entity.collisionBorderSize
        val otherHitbox = otherBB.expand(
            collisionBorderSize.toDouble(),
            collisionBorderSize.toDouble(),
            collisionBorderSize.toDouble()
        )
        // Not quite sure what the difference is between these two vectors
        // In actual code where this is taken from, partialTicks is always 1.0
        // So this won't decrease accuracy
        val eyePos: Vec3 = mc.thePlayer.getPositionEyes(1.0f)
        val lookPos: Vec3 = mc.thePlayer.getLook(1.0f)
        // Get vector for raycast
        val adjustedPos = eyePos.addVector(lookPos.xCoord * maxSize, lookPos.yCoord * maxSize, lookPos.zCoord * maxSize)
        val movingObjectPosition = otherHitbox.calculateIntercept(eyePos, adjustedPos) ?: return -1.0
        // This will trigger if hit distance is more than maxSize
        val otherEntityVec: Vec3 = movingObjectPosition.hitVec
        // finally calculate distance between both vectors
        val dist = eyePos.distanceTo(otherEntityVec)

        profiler.pop()
        return dist
    }

}