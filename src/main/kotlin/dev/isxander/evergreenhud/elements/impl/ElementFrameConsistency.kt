/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.FrameTimeHelper
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import kotlin.math.abs
import kotlin.math.roundToInt

@ElementMeta(id = "evergreenhud:frame_consistency", name = "Frame Consistency", category = "Simple", description = "Displays how consistent your frame times are.")
class ElementFrameConsistency : SimpleTextElement("Consistency") {
    private val frameTimes by FrameTimeHelper { isAdded }

    override fun calculateValue(): String {
        val consistency = ((1 - frameTimes.consistency()) * 100).roundToInt()
        frameTimes.clear()
        return "$consistency%"
    }

    private fun List<Double>.consistency(): Double {
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
