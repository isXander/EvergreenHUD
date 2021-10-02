/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.api.forge10809.events

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.api.forge10809.impl.EntityImpl
import dev.isxander.evergreenhud.api.forge10809.mc
import dev.isxander.evergreenhud.event.ServerDamageEntity
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S19PacketEntityStatus
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

class ServerDamageEntityEventManager {
    private var attacker: EntityPlayer? = null
    private var targetId = -1

    @SubscribeEvent
    fun clientTick(event: TickEvent.ClientTickEvent) {
        if (mc.netHandler != null) {
            val pipeline = mc.netHandler.networkManager.channel().pipeline()
            if (pipeline["evergreenhud_packet_inbound"] == null) {
                pipeline.addBefore("packet_handler", "evergreenhud_packet_inbound",
                    @ChannelHandler.Sharable
                    object : ChannelInboundHandlerAdapter() {
                        override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
                            super.channelRead(ctx, msg)
                            if (msg is Packet<*>) {
                                onPacket(msg)
                            }
                        }
                    }
                )
            }
        }
    }

    private fun onPacket(packet: Packet<*>) {
        if (packet is S19PacketEntityStatus) {
            if (packet.opCode.toInt() != 2) return

            val target = packet.getEntity(mc.theWorld) ?: return
            if (attacker != null && targetId == target.entityId) {
                EvergreenHUD.eventBus.post(ServerDamageEntity(EntityImpl(attacker!!), EntityImpl(target)))
                attacker = null
                targetId = -1
            }
        }
    }

    @SubscribeEvent
    fun onAttack(event: AttackEntityEvent) {
        if (event.entityPlayer.entityId == mc.thePlayer.entityId) {
            attacker = event.entityPlayer
            targetId = event.target.entityId
        }
    }
}
