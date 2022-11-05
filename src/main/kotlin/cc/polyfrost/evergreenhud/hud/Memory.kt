package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.evergreenhud.utils.decimalFormat
import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.DualOption
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.SingleTextHud

class Memory: Config(Mod("Memory", ModType.HUD), "evergreenhud/memory.json", false) {
    @HUD(name = "Main")
    var hud = MemoryHud()

    init {
        initialize()
    }

    class MemoryHud: SingleTextHud("Memory", true, 60, 90) {
        @DualOption(
            name = "Display Type",
            left = "Absolute",
            right = "Percentage"
        )
        var displayType = false

        @Switch(
            name = "Trailing Zeros"
        )
        var trailingZeros = false

        private fun bytesToMb(bytes: Long): Long {
            return bytes / 1024L / 1024L
        }

        override fun getText(example: Boolean): String {
            return if (!displayType) {
                val df = decimalFormat(1, trailingZeros)

                df.format(bytesToMb(Runtime.getRuntime().totalMemory() -
                        Runtime.getRuntime().freeMemory()) / 1024f) + " GB"
            } else {
                val df = decimalFormat(1, trailingZeros, true)

                df.format(getPercent(bytesToMb(Runtime.getRuntime().totalMemory() -
                        Runtime.getRuntime().freeMemory()), 0, bytesToMb(Runtime.getRuntime().maxMemory())))
            }
        }

        /**
         * Returns number between 0 - 1 depending on the range and value given
         *
         * @param num the value
         * @param min minimum of what the value can be
         * @param max maximum of what the value can be
         * @return converted percentage
         * @author isXander
         */
        private fun getPercent(num: Long, min: Long = 0, max: Long = 100): Long {
            return (num - min) / (max - min)
        }

    }
}