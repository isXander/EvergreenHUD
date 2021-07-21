/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/gpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.compatibility.forge10809.impl

import dev.isxander.evergreenhud.compatibility.forge10809.mc
import dev.isxander.evergreenhud.compatibility.universal.PROFILER
import dev.isxander.evergreenhud.compatibility.universal.impl.entity.AIEntity
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.Vec3
import java.lang.IllegalStateException


class EntityImpl(val entity: Entity) : AIEntity() {

    override fun isNull(): Boolean = false

    override fun getX(): Double = entity.posX
    override fun getY(): Double = entity.posY
    override fun getZ(): Double = entity.posZ

    override fun getPrevX(): Double = entity.prevPosX
    override fun getPrevY(): Double = entity.prevPosY
    override fun getPrevZ(): Double = entity.prevPosZ

    override fun getId(): Int = entity.entityId

    override fun getYaw(): Float = entity.rotationYaw
    override fun getPitch(): Float = entity.rotationPitch

    override fun getReachDistFromEntity(entity: AIEntity): Double {
        PROFILER.push("Calculate Reach Distance")
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

        PROFILER.pop()
        return dist
    }

}