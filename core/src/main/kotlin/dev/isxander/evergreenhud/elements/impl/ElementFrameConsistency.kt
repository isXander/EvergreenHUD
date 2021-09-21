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

import dev.deamsy.eventbus.api.listener.EventListener
import dev.isxander.evergreenhud.annotations.ElementMeta
import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.event.RenderTickEvent
import kotlin.math.abs
import kotlin.math.roundToInt

@ElementMeta(id = "FRAME_CONSISTENCY", name = "Frame Consistency", category = "Simple", description = "Displays how consistent your frame times are.")
class ElementFrameConsistency : SimpleTextElement() {
    private var lastTime = System.currentTimeMillis().toDouble()
    private val frameTimes = ArrayList<Double>()

    init {
        title = "Consistency"
        cacheTime = 20
    }

    override fun calculateValue(): String {
        val consistency = ((1 - frameTimes.consinstency()) * 100).roundToInt()
        frameTimes.clear()
        return "$consistency%"
    }

    @EventListener
    fun onRender(event: RenderTickEvent) {
        frameTimes.add(System.currentTimeMillis() - lastTime)
        lastTime = System.currentTimeMillis().toDouble()
    }

    private fun List<Double>.consinstency(): Double {
        if (this.size <= 1) return 0.0
        var change = 0.0
        var count = 0
        var previous: Double? = null
        this.forEach {
            if (previous != null) {
                change += abs(it - previous!!)
                count++
            }
            previous = it
        }
        return change / count / this.sum()
    }
}
