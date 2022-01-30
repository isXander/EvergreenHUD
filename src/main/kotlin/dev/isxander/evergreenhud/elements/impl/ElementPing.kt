/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.ServerPinger
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.boolean
import dev.isxander.settxi.impl.int

@ElementMeta(id = "evergreenhud:ping", name = "Ping", description = "Displays the quality of your connection.", category = "Server")
class ElementPing : SimpleTextElement("Ping") {
    var interval by int(20) {
        name = "Backup Interval"
        description = "How often the ping should be updated."
        category = "Ping"
        range = 20..120
    }

    var showInSinglePlayer by boolean(true) {
        name = "Show in Single Player"
        description = "Should the ping be shown in single player?"
        category = "Ping"
    }

    val ping by ServerPinger.createListener({ interval * 20 }, { isAdded })

    override fun render(renderOrigin: RenderOrigin) {
        if (mc.isSingleplayer && !showInSinglePlayer && renderOrigin == RenderOrigin.HUD) return

        super.render(renderOrigin)
    }

    override fun calculateValue(): String {
        return ping?.toString() ?: "N/A"
    }
}
