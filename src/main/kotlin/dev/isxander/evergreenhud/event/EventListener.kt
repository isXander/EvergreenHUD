/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.event

import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.Packet
import net.minecraft.network.listener.PacketListener

interface EventListener {
    fun onClientTick() {}

    fun onRenderHud(matrices: MatrixStack, tickDelta: Float) {}
    fun onRenderTick(matrices: MatrixStack, tickDelta: Float) {}

    fun onClientDamageEntity(attacker: PlayerEntity, victim: Entity) {}
    fun onServerDamageEntity(attacker: Entity, victim: Entity) {}

    fun <T : PacketListener> onHandlePacket(packet: Packet<T>) {}

    fun onClientJoinWorld(world: ClientWorld) {}
    fun onClientDisconnect() {}
}
