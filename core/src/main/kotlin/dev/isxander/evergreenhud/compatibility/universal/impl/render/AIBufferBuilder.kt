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

package dev.isxander.evergreenhud.compatibility.universal.impl.render

import gg.essential.universal.UMatrixStack

abstract class AIBufferBuilder {

    abstract fun vertex(x: Double, y: Double, z: Double): AIBufferBuilder
    abstract fun color(r: Float, g: Float, b: Float, a: Float): AIBufferBuilder
    abstract fun tex(u: Double, v: Double): AIBufferBuilder

    abstract fun next(): AIBufferBuilder
    abstract fun end(): AIBufferBuilder

    abstract fun begin(mode: DrawMode, format: VertexFormats): AIBufferBuilder

    abstract fun draw()

}