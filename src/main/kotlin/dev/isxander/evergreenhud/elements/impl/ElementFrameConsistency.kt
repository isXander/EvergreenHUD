/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import net.minecraft.client.util.math.MatrixStack
import kotlin.math.abs
import kotlin.math.roundToInt

@ElementMeta(id = "FRAME_CONSISTENCY", name = "Frame Consistency", category = "Simple", description = "Displays how consistent your frame times are.")
class ElementFrameConsistency : SimpleTextElement("Consistency") {
    private var lastTime = System.currentTimeMillis().toDouble()
    private val frameTimes = ArrayList<Double>()

    override fun calculateValue(): String {
        val consistency = ((1 - frameTimes.consinstency()) * 100).roundToInt()
        frameTimes.clear()
        return "$consistency%"
    }

    override fun onRenderTick(matrices: MatrixStack, tickDelta: Float) {
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
