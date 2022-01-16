/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.settings

import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.json
import dev.isxander.settxi.Setting
import dev.isxander.settxi.serialization.ConfigProcessor
import kotlinx.serialization.json.*

class ColorSetting internal constructor(
    default: Color,
    lambda: ColorSetting.() -> Unit = {},
) : Setting<Color>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override lateinit var description: String
    override var shouldSave: Boolean = true
    var canHaveChroma = false

    override var serializedValue: JsonElement
        get() = json.encodeToJsonElement(value)
        set(new) { value = json.decodeFromJsonElement(new) }

    override val defaultSerializedValue: JsonElement =
        json.encodeToJsonElement(default)

    init {
        this.apply(lambda)
    }
}

@JvmName("colorSetting")
fun ConfigProcessor.color(default: Color, lambda: ColorSetting.() -> Unit): ColorSetting {
    return ColorSetting(default, lambda).also { settings.add(it) }
}
