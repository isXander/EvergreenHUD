/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.event

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.utils.mc
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.Packet
import net.minecraft.network.listener.PacketListener
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket

class ServerDamageEntityEventManager : EventListener {
    private var attacker: Entity? = null
    private var targetId = -1

    override fun <T : PacketListener> onHandlePacket(packet: Packet<T>) {
        if (packet is EntityStatusS2CPacket) {
            if (packet.status.toInt() != 2) return

            val target = packet.getEntity(mc.world) ?: return
            if (attacker != null && targetId == target.id) {
                EvergreenHUD.eventBus.post { onServerDamageEntity(attacker!!, target) }
                attacker = null
                targetId = -1
            }
        }
    }

    override fun onClientDamageEntity(attacker: PlayerEntity, victim: Entity) {
        this.attacker = attacker
        targetId = victim.id
    }
}
