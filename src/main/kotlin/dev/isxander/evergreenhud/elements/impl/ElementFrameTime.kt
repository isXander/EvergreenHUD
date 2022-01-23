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
import dev.isxander.settxi.impl.OptionContainer
import dev.isxander.settxi.impl.option
import kotlin.math.ceil
import kotlin.math.roundToInt

@ElementMeta(id = "evergreenhud:frame_time", name = "Frame Time", category = "Simple", description = "Display how long it took to render the current frame.")
class ElementFrameTime : SimpleTextElement("Frame Time") {
    var averageMethod by option(AverageMethod.Mean) {
        name = "Average Method"
        description = "Method in which to calculate the average FPS."
        category = "FPS"
    }

    private val frameTimes by FrameTimeHelper { isAdded }

    override fun calculateValue(): String {
        // converts average to FPS
        val frameTime = (average(frameTimes).takeUnless { it.isNaN() } ?: 1.0).roundToInt()
        frameTimes.clear()
        return frameTime.toString() + "ms"
    }

    private fun average(list: List<Double>): Double = when (averageMethod) {
        AverageMethod.Mean -> list.average()
        AverageMethod.Median -> percentile(list, 0.5)
        AverageMethod.Percentile99 -> percentile(list, 0.99)
        AverageMethod.Percentile95 -> percentile(list, 0.95)
        else -> 0.0
    }

    private fun percentile(list: List<Double>, percentile: Double): Double {
        val index = ceil(list.size * percentile).toInt() - 1
        return list.sorted()[index]
    }

    object AverageMethod : OptionContainer() {
        val Mean = option("Mean", "Sum of all values divided by the number of values.")
        val Median = option("Median", "The middle value.")
        val Percentile99 = option("99th Percentile")
        val Percentile95 = option("95th Percentile")
    }
}
