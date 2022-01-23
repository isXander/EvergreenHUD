/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.packets.client

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.packets.ELEMENTS_PACKET
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

fun registerElementsPacket() {
    ClientPlayNetworking.registerGlobalReceiver(ELEMENTS_PACKET) { client, handler, buf, sender ->
        val bannedIds = buf.readList { it.readString() }
        EvergreenHUD.elementManager.blacklistedElements[client.currentServerEntry!!.address] = bannedIds
    }
}
