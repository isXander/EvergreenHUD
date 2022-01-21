/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.event.ClientTickEvent
import net.minecraft.client.network.Address
import net.minecraft.client.network.AllowedAddressResolver
import net.minecraft.client.network.ServerAddress
import net.minecraft.client.network.ServerInfo
import net.minecraft.network.ClientConnection
import net.minecraft.network.NetworkState
import net.minecraft.network.listener.ClientQueryPacketListener
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket
import net.minecraft.network.packet.c2s.query.QueryRequestC2SPacket
import net.minecraft.network.packet.s2c.query.QueryPongS2CPacket
import net.minecraft.network.packet.s2c.query.QueryResponseS2CPacket
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Util
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

object ServerPinger {
    val pingers = Collections.synchronizedList(mutableListOf<Pinger>())

    fun createListener(interval: () -> Int, predicate: () -> Boolean = { true }, serverGetter: () -> ServerInfo?): Pinger {
        val pinger = Pinger(interval, predicate, serverGetter)
        pingers.add(pinger)
        return pinger
    }

    class Pinger(private val interval: () -> Int, private val predicate: () -> Boolean, private val serverGetter: () -> ServerInfo?) : ReadOnlyProperty<Any?, Int?> {
        private var ping: Int? = null

        private var ticks = 0
        val clientTickEvent by EvergreenHUD.eventBus.register<ClientTickEvent>({ predicate() }) {
            ticks++

            if (ticks % interval() == 0) {
                serverGetter()?.let(this::ping)
            }
        }

        init {
            serverGetter()?.let(this::ping)
        }

        private fun ping(server: ServerInfo) {
            val serverAddress = ServerAddress.parse(server.address)
            val clientConnection = ClientConnection.connect(AllowedAddressResolver.DEFAULT.resolve(serverAddress).map(Address::getInetSocketAddress).orElseThrow(), false)

            clientConnection.packetListener = object : ClientQueryPacketListener {
                private var startTime = -1L
                private var queried = false
                private var received = false

                override fun onDisconnected(reason: Text) {
                    if (!queried) {
                        error("Failed to query server: $reason")
                    }
                }

                override fun getConnection(): ClientConnection = clientConnection

                override fun onResponse(packet: QueryResponseS2CPacket) {
                    if (received) {
                        clientConnection.disconnect(TranslatableText("multiplayer.status.unrequested"))
                        return
                    }
                    received = true
                    startTime = Util.getMeasuringTimeMs()
                    clientConnection.send(QueryPingC2SPacket(startTime))
                    queried = true
                }

                override fun onPong(packet: QueryPongS2CPacket) {
                    ping = (Util.getMeasuringTimeMs() - startTime).toInt()
                }
            }

            clientConnection.send(HandshakeC2SPacket(serverAddress.address, serverAddress.port, NetworkState.STATUS))
            clientConnection.send(QueryRequestC2SPacket())
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>) = ping
    }
}
