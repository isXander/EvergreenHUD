/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.event

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.utils.ChannelPipelineManager
import dev.isxander.evergreenhud.utils.CustomChannelHandlerFactory
import dev.isxander.evergreenhud.utils.SharableChannelInboundHandlerAdapter
import dev.isxander.evergreenhud.utils.SharableChannelOutboundHandlerAdapter
import gg.essential.universal.wrappers.message.UTextComponent
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.Packet
import net.minecraft.util.BlockPos
import net.minecraft.util.DamageSource
import net.minecraft.world.World
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent


abstract class Event
abstract class CancelableEvent : Event() {
    var canceled = false
}

class ClientTickEvent : Event() // done

data class RenderHudEvent(val tickDelta: Float) : Event() // done
data class RenderTickEvent(val tickDelta: Float) : Event() // done

data class ClientDamageEntityEvent(val attacker: EntityPlayer, val victim: Entity) : Event() // done
data class ServerDamageEntityEvent(val attacker: Entity, val victim: Entity) : Event() // done
data class EntityDamagedEvent(val entity: EntityLivingBase, val source: DamageSource, val amount: Float) : Event() // done

data class HandlePacketEvent(val packet: Packet<*>) : Event() // done

data class ClientJoinWorldEvent(val world: World) : Event() // done
class ClientDisconnectEvent : Event() // done

data class ClientPlaceBlockEvent(val player: EntityPlayer, val world: World) : Event() // done
data class ClientBreakBlockEvent(val pos: BlockPos) : Event() // done

data class ClientReceivedChatMessage(val text: String) : CancelableEvent() // done

object Events {
    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.START) {
            EvergreenHUD.eventBus.post(ClientTickEvent())
        }
    }

    @SubscribeEvent
    fun onRenderTick(event: TickEvent.RenderTickEvent) {
        if (event.phase == TickEvent.Phase.START) {
            EvergreenHUD.eventBus.post(RenderTickEvent(event.renderTickTime))
        }
    }

    @SubscribeEvent
    fun onGameOverlayRendered(event: RenderGameOverlayEvent.Post) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            EvergreenHUD.eventBus.post(RenderHudEvent(event.partialTicks))
        }
    }

    @SubscribeEvent
    fun onWorldJoin(event: WorldEvent.Load) {
        EvergreenHUD.eventBus.post(ClientJoinWorldEvent(event.world))
    }

    @SubscribeEvent
    fun onWorldLeave(event: WorldEvent.Unload) {
        EvergreenHUD.eventBus.post(ClientDisconnectEvent())
    }

    @SubscribeEvent
    fun onAttackEntity(event: AttackEntityEvent) {
        EvergreenHUD.eventBus.post(ClientDamageEntityEvent(event.entityPlayer, event.target))
    }

    @SubscribeEvent
    fun onMessage(event: ClientChatReceivedEvent) {
        EvergreenHUD.eventBus.post(ClientReceivedChatMessage(UTextComponent.stripFormatting(event.message.unformattedText)))
    }

    @SubscribeEvent
    fun onHurt(event: LivingHurtEvent) {
        EvergreenHUD.eventBus.post(EntityDamagedEvent(event.entityLiving, event.source, event.ammount))
    }

    init {
        ChannelPipelineManager.addHandler(
            CustomChannelHandlerFactory.newInstance()
                .setName("evergreenhud_packet_listener_inbound")
                .setHandler(object : SharableChannelInboundHandlerAdapter() {
                    @Throws(Exception::class)
                    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                        super.channelRead(ctx, msg)
                        if (msg is Packet<*>) EvergreenHUD.eventBus.post(HandlePacketEvent(msg))
                    }
                })
                .setAddBefore("packet_handler")
                .build()
        )
        ChannelPipelineManager.addHandler(
            CustomChannelHandlerFactory.newInstance()
                .setName("evergreenhud_packet_listener_outbound")
                .setHandler(object : SharableChannelOutboundHandlerAdapter() {
                    @Throws(Exception::class)
                    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
                        super.write(ctx, msg, promise)
                        if (msg is Packet<*>) EvergreenHUD.eventBus.post(HandlePacketEvent(msg))
                    }
                })
                .setAddBefore("packet_handler")
                .build()
        )
    }
}
