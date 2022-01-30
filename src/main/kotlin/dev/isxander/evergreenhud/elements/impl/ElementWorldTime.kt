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
import dev.isxander.settxi.impl.boolean
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@ElementMeta(id = "evergreenhud:world_time", name = "World Time", description = "Show the current time in-game.", category = "World")
class ElementWorldTime : SimpleTextElement("Time") {
    var twelveHour by boolean(false) {
        name = "Twelve Hour"
        category = "Time"
        description = "If the clock should display AM or PM or go into 13:00+"
    }

    var seconds by boolean(false) {
        name = "Seconds"
        category = "Time"
        description = "Show the seconds."
    }

    override fun calculateValue(): String {
        if (mc.theWorld == null) return "06:00${if (seconds) ":00" else ""} AM"

        // ticks to ticks in day to seconds to millis plus six hours (time 0 = 6am)
        val date = Date(mc.theWorld!!.worldTime / 20 * 1000 + TimeUnit.HOURS.toMillis(6))
        return SimpleDateFormat(String.format(if (twelveHour) "hh:mm%s a" else "HH:mm%s", if (seconds) ":ss" else ""))
            .format(date).uppercase()
    }
}
