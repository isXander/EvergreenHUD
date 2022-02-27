/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc

@ElementMeta(id = "evergreenhud:server_ip", name = "Server IP", category = "Server", description = "Shows the IP of the server.")
class ElementServerIP : SimpleTextElement("Server") {
    override fun calculateValue(): String {
        return mc.currentServerData?.serverIP ?: "127.0.0.1"
    }
}
