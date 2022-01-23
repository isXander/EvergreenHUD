/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils.hypixel.locraw

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.event.ClientReceivedChatMessage
import dev.isxander.evergreenhud.utils.*
import dev.isxander.evergreenhud.utils.hypixel.isHypixel
import kotlinx.serialization.decodeFromString
import net.minecraft.client.gui.ClientChatListener
import net.minecraft.network.MessageType
import net.minecraft.text.Text
import java.util.*

class LocrawManager {
    var currentLocation = LocationParsed.UNKNOWN
        private set(value) {
            logger.info("New Hypixel Location: $field")
            field = value
        }

    private var waitingForLocraw = false

    val onChatMessage by EvergreenHUD.eventBus.register<ClientReceivedChatMessage> {
        if (isHypixel) {
            val string = stripColorCodes(it.text.extractString())

            when (string) {
                "                         ",
                "       ",
                "                                     " ->
                    enqueueUpdate(500)

                "You are sending too many commands! Please try again in a few seconds." -> {
                    if (waitingForLocraw) {
                        waitingForLocraw = false
                        enqueueUpdate(5000)
                    }
                }

                "You are AFK. Move around to return from AFK." ->
                    currentLocation = LocationParsed(gameType = GameType.LIMBO)
            }

            if (string.startsWith('{')) {
                currentLocation = json.decodeFromString(string)

                if (waitingForLocraw) {
                    waitingForLocraw = false
                    it.canceled = true
                }
            }
        }
    }

    private fun enqueueUpdate(ms: Long) {
        if (!waitingForLocraw) {
            waitingForLocraw = true

            scheduleAsync(ms) {
                mc.player?.sendChatMessage("/locraw")
            }
        }
    }
}
