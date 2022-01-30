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
import gg.essential.api.utils.Multithreading
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.client.network.OldServerPinger
import java.net.UnknownHostException
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


object ServerPinger {
    val pingers = Collections.synchronizedList(mutableListOf<Pinger>())

    fun createListener(interval: () -> Int, predicate: () -> Boolean = { true }): Pinger {
        val pinger = Pinger(interval, predicate)
        pingers.add(pinger)
        return pinger
    }

    class Pinger(private val interval: () -> Int, private val predicate: () -> Boolean) : ReadOnlyProperty<Any?, Int?> {
        private val serverPinger = OldServerPinger()
        private val serverUpdateTime: HashMap<String, Long> = HashMap()
        private val serverUpdateStatus: HashMap<String, Boolean> = HashMap()
        private var serverPing: Int? = null
        private var ticks = 0
        val clientTickEvent by EvergreenHUD.eventBus.register<ClientTickEvent>({ predicate() }) {
            ticks++

            if (ticks % interval() == 0) {
                updateManually(mc.currentServerData)
            }
        }

        private fun updateManually(server: ServerData?) {
            if (server != null) {
                serverUpdateStatus[server.serverIP] = true
                Multithreading.runAsync {
                    try {
                        serverPinger.ping(server)
                    } catch (e: UnknownHostException) {
                        e.printStackTrace()
                    }
                    serverUpdateStatus[server.serverIP] = false
                    serverUpdateTime[server.serverIP] = System.currentTimeMillis()
                }
                serverPing = server.pingToServer.toInt()
            }
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>) = serverPing
    }
}
