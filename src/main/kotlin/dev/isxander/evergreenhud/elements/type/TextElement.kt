/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.type

import dev.isxander.settxi.impl.*
import dev.isxander.evergreenhud.event.ClientTickEvent
import dev.isxander.evergreenhud.settings.color
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.HitBox2D

abstract class TextElement
@JvmOverloads constructor(title: String, cacheTime: Int = 5) : BackgroundElement() {
    var brackets by boolean(false) {
        name = "Brackets"
        category = "Text"
        description = "Text is displayed within [square brackets]"
    }

    var title by string(title) {
        name = "Title"
        category = "Text"
        description = "What is displayed before or after the actual value."
    }

    var textColor by color(Color.white) {
        name = "Color"
        category = "Text"
        description = "The color of the text."
        canHaveChroma = true
    }

    var textStyle by option(TextStyle.SHADOW) {
        name = "Text Style"
        category = "Text"
        description = "What style text is rendered in."
    }

    var alignment by option(Alignment.LEFT) {
        name = "Alignment"
        category = "Text"
        description = "How the text is aligned."
    }

    var cacheTime by int(cacheTime) {
        name = "Text Cache"
        category = "Text"
        description = "How many client ticks to wait before re-calculating the value. (20 ticks = 1 second)"
        range = 0..20
    }

    private val clientTickEvent by event<ClientTickEvent> {
        clientTicks++
        if (clientTicks > cacheTime)
            clientTicks = 0
    }
    var clientTicks = 0

    override fun calculateHitBox(scale: Float): HitBox2D {
        val width = hitboxWidth.coerceAtLeast(minWidth) * scale
        val height = hitboxHeight.coerceAtLeast(minHeight) * scale

        val top = paddingTop * scale
        val bottom = paddingBottom * scale
        val left = paddingLeft * scale
        val right = paddingRight * scale

        val x = position.rawX
        val y = position.rawY

        return when (alignment) {
            Alignment.LEFT -> HitBox2D(x - left, y - top, width + left + right, height + top + bottom)
            Alignment.RIGHT -> HitBox2D(x - (width / scale) - left, y - top, width + left + right, height + top + bottom)
            Alignment.CENTER -> HitBox2D(x - (width / 2f) - left, y - top, width + left + right, height + top + bottom)
            else -> throw IllegalStateException("Failed to parse alignment.")
        }
    }

    object TextStyle : OptionContainer() {
        val NORMAL = option("Normal", "Just plain text.")
        val SHADOW = option("Shadow", "Text with Minecraft's shadow. This can be modified with mods like patcher.")
        val BORDER = option("Border", "Adds a black border around normal text. (Can cause increased amount of lag)")
    }

    object Alignment : OptionContainer() {
        val LEFT = option("Left", "When the length of text is increased, it expands to the right.")
        val RIGHT = option("Right", "When the length of text is increased, it expands to the left.")
        val CENTER = option("Center", "When the length of text is increased, it expands both left and right equally.")
    }
}
