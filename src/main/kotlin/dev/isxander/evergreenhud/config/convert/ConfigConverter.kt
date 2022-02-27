/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.config.convert

import dev.isxander.evergreenhud.config.convert.impl.ChromaHudConverter
import dev.isxander.evergreenhud.config.convert.impl.EvergreenHud14Converter
import dev.isxander.evergreenhud.config.convert.impl.KronHudConverter

interface ConfigConverter {
    val name: String

    fun process(): String?
    fun detect(): Boolean

    companion object {
        val all = listOf(ChromaHudConverter, KronHudConverter, EvergreenHud14Converter)
    }
}
