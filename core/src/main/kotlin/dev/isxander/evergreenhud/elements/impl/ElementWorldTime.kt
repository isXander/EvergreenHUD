/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.api.world
import dev.isxander.evergreenhud.elements.ElementMeta
import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.settxi.impl.BooleanSetting
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@ElementMeta(id = "WORLD_TIME", name = "World Time", description = "Show the current time in-game.", category = "World")
class ElementWorldTime : SimpleTextElement() {

    @BooleanSetting(name = "Time", category = "Time", description = "If the clock will be 12 hour of 24 hour.")
    var twelveHour = false

    @BooleanSetting(name = "Seconds", category = "Time", description = "Show the seconds.")
    var seconds = false

    override var title: String = "Time"

    override fun calculateValue(): String {
        if (world.isNull) return "06:00${if (seconds) ":00" else ""}"

        // ticks to ticks in day to seconds to millis plus six hours (time 0 = 6am)
        val date = Date(world.time % 24000L / 20 * 1000 + TimeUnit.HOURS.toMillis(6))
        return SimpleDateFormat(String.format(if (twelveHour) "hh:mm%s a" else "HH:mm%s", if (seconds) ":ss" else ""))
            .format(date).uppercase()
    }

}