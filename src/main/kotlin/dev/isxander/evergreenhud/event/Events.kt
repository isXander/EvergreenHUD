/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.event

import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.MessageType
import net.minecraft.network.Packet
import net.minecraft.network.listener.PacketListener
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import java.util.*

abstract class Event
abstract class CancelableEvent : Event() {
    var canceled = false
}

class ClientTickEvent : Event()

data class RenderHudEvent(val matrices: MatrixStack, val tickDelta: Float) : Event()
data class RenderTickEvent(val matrices: MatrixStack, val tickDelta: Float) : Event()

data class ClientDamageEntityEvent(val attacker: PlayerEntity, val victim: Entity) : Event()
data class ServerDamageEntityEvent(val attacker: Entity, val victim: Entity) : Event()
data class EntityDamagedEvent(val entity: LivingEntity, val source: DamageSource, val amount: Float) : Event()

data class HandlePacketEvent<T : PacketListener>(val packet: Packet<T>) : Event()

data class ClientJoinWorldEvent(val world: ClientWorld) : Event()
class ClientDisconnectEvent : Event()

data class ClientPlaceBlockEvent(val player: ClientPlayerEntity, val world: ClientWorld, val hand: Hand, val hitResult: BlockHitResult) : Event()
data class ClientBreakBlockEvent(val pos: BlockPos) : Event()

data class ClientReceivedChatMessage(val type: MessageType, val text: Text, val sender: UUID) : CancelableEvent()
