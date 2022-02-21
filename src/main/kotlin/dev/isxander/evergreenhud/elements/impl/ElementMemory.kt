/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.decimalFormat
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.getPercent
import dev.isxander.settxi.impl.OptionContainer
import dev.isxander.settxi.impl.boolean
import dev.isxander.settxi.impl.option

@ElementMeta(id = "evergreenhud:memory", name = "Memory", description = "Show the amount of RAM being used by Minecraft.", category = "Simple")
class ElementMemory : SimpleTextElement("Memory", 20) {
    var displayType by option(DisplayType.Absolute) {
        name = "Display Type"
        description = "How to display the amount of RAM"
        category = "Memory"
    }

    var trailingZeros by boolean(false) {
        name = "Trailing Zeros"
        description = "Make the number the same width."
        category = "Memory"
    }

    override fun calculateValue(): String {
        return when (displayType) {
            DisplayType.Absolute -> {
                val df = decimalFormat(1, trailingZeros)

                df.format(bytesToMb(Runtime.getRuntime().totalMemory() -
                        Runtime.getRuntime().freeMemory()) / 1024f) + " GB"
            }
            DisplayType.Percentage -> {
                val df = decimalFormat(1, trailingZeros, true)

                df.format(getPercent(bytesToMb(Runtime.getRuntime().totalMemory() -
                        Runtime.getRuntime().freeMemory()), 0, bytesToMb(Runtime.getRuntime().maxMemory())))
            }
            else -> "0"
        }
    }

    private fun bytesToMb(bytes: Long): Long {
        return bytes / 1024L / 1024L
    }

    object DisplayType : OptionContainer() {
        val Absolute = option("Absolute")
        val Percentage = option("Percentage")
    }
}
