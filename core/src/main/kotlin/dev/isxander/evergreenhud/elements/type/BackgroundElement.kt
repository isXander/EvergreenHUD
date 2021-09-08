/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.elements.type

import dev.isxander.evergreenhud.api.gl
import dev.isxander.evergreenhud.elements.Element
import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.settxi.SettingAdapter
import dev.isxander.evergreenhud.utils.HitBox2D
import dev.isxander.settxi.impl.*
import gg.essential.elementa.utils.withIndex
import java.awt.Color

abstract class BackgroundElement : Element() {

    var backgroundEnabled by boolean(
        default = true,
        name = "Enabled",
        category = "Background",
        description = "If the background is rendered."
    ) {
        set { enabled ->
            val new = if (enabled) Color(backgroundColor.red, backgroundColor.green, backgroundColor.blue, 100)
            else Color(backgroundColor.red, backgroundColor.green, backgroundColor.blue, 0)
            if (backgroundColor != new) backgroundColor = new

            return@set enabled
        }
    }

    var backgroundColor: Color by color(
        default = Color(0, 0, 0, 100),
        name = "Color",
        category = "Background",
        description = "The color of the background."
    ) {
        set {
            val enabled = it.alpha != 0
            if (backgroundEnabled != enabled) backgroundEnabled = enabled
            return@set it
        }
    }

    var outlineEnabled by boolean(
        default = false,
        name = "Enabled",
        category = "Outline",
        description = "If the background is rendered."
    ) {
        set { enabled ->
            val new = if (enabled) Color(outlineColor.red, outlineColor.green, outlineColor.blue, 255)
            else Color(outlineColor.red, outlineColor.green, outlineColor.blue, 0)
            if (outlineColor != new) outlineColor = new

            return@set enabled
        }
    }

    var outlineColor: Color by color(
        default = Color(0, 0, 0, 0),
        name = "Color",
        category = "Outline",
        description = "The color of the outline."
    ) {
        set {
            val enabled = it.alpha != 0
            if (outlineEnabled != enabled) outlineEnabled = enabled
            return@set it
        }
    }

    var outlineThickness by float(
        default = 1f,
        name = "Thickness",
        category = "Outline",
        description = "How thick the outline is.",
        min = 0.5f,
        max = 8f
    )

    var paddingLeft by float(
        default = 4f,
        name = "Padding (Left)",
        category = "Background",
        subcategory = "Padding",
        description = "How far the background extends to the left.",
        min = 0f,
        max = 12f
    )

    var paddingRight by float(
        default = 4f,
        name = "Padding (Right)",
        category = "Background",
        subcategory = "Padding",
        description = "How far the background extends to the right.",
        min = 0f,
        max = 12f
    )

    var paddingTop by float(
        default = 4f,
        name = "Padding (Top)",
        category = "Background",
        subcategory = "Padding",
        description = "How far the background extends to the top.",
        min = 0f,
        max = 12f
    )

    var paddingBottom by float(
        default = 4f,
        name = "Padding (Bottom)",
        category = "Background",
        subcategory = "Padding",
        description = "How far the background extends to the bottom.",
        min = 0f,
        max = 12f
    )

    var cornerRadius by float(
        default = 0f,
        name = "Corner Radius",
        category = "Background",
        description = "How rounded the edges of the background are.",
        min = 0f,
        max = 6f
    )

    override fun render(deltaTicks: Float, renderOrigin: RenderOrigin) {
        val bgCol = backgroundColor
        val outlineCol = outlineColor

        val scale = position.scale
        val hitbox = calculateHitBox(1f, scale)

        if (backgroundEnabled) {
            gl.roundedRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height, bgCol.rgb, cornerRadius)
        }
        if (outlineEnabled) {
            gl.borderRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height, outlineCol.rgb, outlineThickness)
        }
    }

    override fun calculateHitBox(glScale: Float, drawScale: Float): HitBox2D {
        val width = hitboxWidth * drawScale
        val height = hitboxHeight * drawScale

        val top = paddingTop * drawScale
        val bottom = paddingBottom * drawScale
        val left = paddingLeft * drawScale
        val right = paddingRight * drawScale

        val x = position.rawX / glScale
        val y = position.rawY / glScale

        return HitBox2D(x - left, y - top, width + left + right, height + top + bottom)
    }

}
