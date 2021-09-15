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

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.api.impl.CustomKeybind
import dev.isxander.evergreenhud.api.impl.registerKeybind
import dev.isxander.evergreenhud.api.keybindManager
import dev.isxander.evergreenhud.elements.ElementMeta
import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.Input
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer

@ElementMeta(id = "TIMER", name = "Timer", category = "Simple", description = "Set a timer.")
class ElementTimer : SimpleTextElement() {
    var keybind: CustomKeybind? = null

    var elapsed = 0L
    var stopped = true
    val timer = timer(period = 1) {
        if (!stopped) elapsed++
    }

    init {
        title = "Timer"
    }

    override fun calculateValue(): String {
        val date = Date(elapsed)

        var format = "s"
        if (elapsed >= TimeUnit.SECONDS.toMillis(10)) format = "s$format"
        if (elapsed >= TimeUnit.MINUTES.toMillis(1)) format = "m:$format"
        if (elapsed >= TimeUnit.MINUTES.toMillis(10)) format = "m$format"
        if (elapsed >= TimeUnit.HOURS.toMillis(1)) format = "h:$format"
        if (elapsed >= TimeUnit.HOURS.toMillis(10)) format = "h$format"

        return SimpleDateFormat(format).format(date)
    }

    override fun onAdded() {
        super.onAdded()

        keybind = registerKeybind {
            key = Input.KEY_L
            name = "Toggle ${metadata.name}"
            category = "EvergreenHUD Timers"

            onDown {
                stopped = !stopped
            }
        }
    }

    override fun onRemoved() {
        super.onRemoved()
        stopped = true

        if (keybind != null) keybindManager.unregisterKeybind(keybind!!)
    }
}