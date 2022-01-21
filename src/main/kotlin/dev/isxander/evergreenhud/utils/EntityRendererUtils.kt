/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.DiffuseLighting
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.Quaternion
import net.minecraft.util.math.Vec3f

fun LivingEntity.renderEntity(x: Float, y: Float, size: Float, viewRotation: Float = 0f) {
    val matrixStack = RenderSystem.getModelViewStack()
    matrixStack.push()
    matrixStack.translate(
        x,
        y,
        1050f
    )
    matrixStack.scale(1.0f, 1.0f, -1.0f)
    RenderSystem.applyModelViewMatrix()
    val matrixStack2 = MatrixStack()
    matrixStack2.translate(0.0, 0.0, 1000.0)
    matrixStack2.scale(size, size, size)
    val quaternion = Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f)
    matrixStack2.multiply(quaternion)

    val interpolatedYaw = lerp(prevYaw, yaw, mc.tickDelta)
    matrixStack2.multiply(Quaternion(Vec3f(0f, 1f, 0f), interpolatedYaw - 180f + viewRotation, true))
    val prevHeadYaw: Float = prevHeadYaw
    val headYaw: Float = headYaw

    this.headYaw = interpolatedYaw
    this.prevHeadYaw = interpolatedYaw
    DiffuseLighting.method_34742()
    val entityRenderDispatcher = mc.entityRenderDispatcher
    entityRenderDispatcher.rotation = quaternion
    entityRenderDispatcher.setRenderShadows(false)
    val immediate = mc.bufferBuilders.entityVertexConsumers
    RenderSystem.runAsFancy {
        entityRenderDispatcher.render(
            this,
            0.0,
            0.0,
            0.0,
            0f,
            mc.tickDelta,
            matrixStack2,
            immediate,
            0xF000F0
        )
    }
    immediate.draw()
    entityRenderDispatcher.setRenderShadows(true)
    this.prevHeadYaw = prevHeadYaw
    this.headYaw = headYaw
    matrixStack.pop()
    RenderSystem.applyModelViewMatrix()
    DiffuseLighting.enableGuiDepthLighting()
}
