package cc.polyfrost.evergreenhud.utils

import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.Stage
import cc.polyfrost.oneconfig.events.event.TickEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ServerAddress
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.network.EnumConnectionState
import net.minecraft.network.NetworkManager
import net.minecraft.network.handshake.client.C00Handshake
import net.minecraft.network.status.INetHandlerStatusClient
import net.minecraft.network.status.client.C00PacketServerQuery
import net.minecraft.network.status.client.C01PacketPing
import net.minecraft.network.status.server.S00PacketServerInfo
import net.minecraft.network.status.server.S01PacketPong
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import java.net.InetAddress
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

object ServerPinger {
    val pingers = Collections.synchronizedList(mutableListOf<Pinger>())

    fun createListener(interval: () -> Int, serverGetter: () -> ServerData?): Pinger {
        val pinger = Pinger(interval, serverGetter)
        pingers.add(pinger)
        return pinger
    }

    class Pinger(private val interval: () -> Int, private val serverGetter: () -> ServerData?) : ReadOnlyProperty<Any?, Int?> {
        private var ping: Int? = null

        private var ticks = 0
        init {
            EventManager.INSTANCE.register(this)
            serverGetter()?.let(this::ping)
        }

        @Subscribe
        private fun onTick(event: TickEvent) {
            if (event.stage == Stage.START) {
                ticks++

                if (ticks % interval() == 0) {
                    serverGetter()?.let(this::ping)
                }
            }
        }

        private fun ping(server: ServerData) {
            val serverAddress = ServerAddress.fromString(server.serverIP)
            val networkmanager = NetworkManager.createNetworkManagerAndConnect(
                InetAddress.getByName(serverAddress.ip), serverAddress.port, false
            )

            networkmanager.netHandler = object : INetHandlerStatusClient {
                private var startTime = -1L
                private var queried = false
                private var received = false

                override fun onDisconnect(reason: IChatComponent?) {
                    if (!queried) {
                        error("Failed to query server: ${reason?.unformattedText ?: "null"}")
                    }
                }

                override fun handleServerInfo(packetIn: S00PacketServerInfo?) {
                    if (received) {
                        networkmanager.closeChannel(ChatComponentText("Received unrequested status"))
                        return
                    }
                    received = true
                    startTime = Minecraft.getSystemTime()
                    networkmanager.sendPacket(C01PacketPing(startTime))
                    queried = true
                }

                override fun handlePong(packetIn: S01PacketPong?) {
                    ping = (Minecraft.getSystemTime() - startTime).toInt()
                }
            }

            networkmanager.sendPacket(
                C00Handshake(
                    47,
                    serverAddress.ip,
                    serverAddress.port,
                    EnumConnectionState.STATUS
                )
            )
            networkmanager.sendPacket(C00PacketServerQuery())
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>) = ping
    }
}