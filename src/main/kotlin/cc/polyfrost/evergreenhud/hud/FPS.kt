package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.evergreenhud.utils.FrameTimeHelper
import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Dropdown
import cc.polyfrost.oneconfig.config.annotations.Exclude
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.SingleTextHud
import net.minecraft.client.Minecraft
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.roundToInt

class FPS : Config(Mod("FPS", ModType.HUD), "evergreenhud/fps.json") {
    @HUD(name = "FPS", category = "FPS")
    var fps = FPSHud()

    @HUD(name = "Frame Consistency", category = "Frame Consistency")
    var frameConsistency = FrameConsistencyHud()

    @HUD(name = "Frame Time", category = "Frame Time")
    var frameTime = FrameTimeHud()

    init {
        initialize()
    }

    class FPSHud : SingleTextHud("FPS", true) {
        @Switch(
            name = "Update Fast"
        )
        var updateFast = false

        @Dropdown(
            name = "Average Method",
            options = ["Mean", "Median", "99th Percentile", "95th Percentile"]
        )
        var averageMethod = 0

        private val frameTimes by FrameTimeHelper()

        private fun average(list: List<Double>): Double = when (averageMethod) {
            0 -> list.average()
            1 -> percentile(list, 0.5)
            2 -> percentile(list, 0.99)
            3 -> percentile(list, 0.95)
            else -> 0.0
        }

        private fun percentile(list: List<Double>, percentile: Double): Double {
            val index = ceil(list.size * percentile).toInt() - 1
            return list.sorted()[index]
        }

        override fun getText(): String {
            return if (updateFast) {
                val fps = (1000 / (average(frameTimes).takeUnless { it.isNaN() } ?: 1.0)).roundToInt().toString()
                frameTimes.clear()
                fps
            } else {
                Minecraft.getDebugFPS().toString()
            }
        }
    }

    class FrameConsistencyHud : SingleTextHud("Frame Consistency", false) {

        private val frameTimes by FrameTimeHelper()

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

        override fun getText(): String {
            val consistency = ((1 - frameTimes.consistency()) * 100).roundToInt()
            frameTimes.clear()
            return "$consistency%"
        }
    }

    class FrameTimeHud : SingleTextHud("Frame Time", false) {

        @Dropdown(
            name = "Average Method",
            options = ["Mean", "Median", "99th Percentile", "95th Percentile"]
        )
        var averageMethod = 0

        private val frameTimes by FrameTimeHelper()

        private fun average(list: List<Double>): Double = when (averageMethod) {
            0 -> list.average()
            1 -> percentile(list, 0.5)
            2 -> percentile(list, 0.99)
            3 -> percentile(list, 0.95)
            else -> 0.0
        }

        private fun percentile(list: List<Double>, percentile: Double): Double {
            val index = ceil(list.size * percentile).toInt() - 1
            return list.sorted()[index]
        }

        override fun getText(): String {
            // converts average to FPS
            val frameTime = (average(frameTimes).takeUnless { it.isNaN() } ?: 1.0).roundToInt()
            frameTimes.clear()
            return frameTime.toString() + "ms"
        }
    }
}
