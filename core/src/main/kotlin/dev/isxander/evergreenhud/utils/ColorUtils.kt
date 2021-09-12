/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.utils

import dev.isxander.evergreenhud.api.gl
import java.awt.Color

fun Color.applyToGL() {
    gl.color(this.red / 255f, this.green / 255f, this.blue / 255f, this.alpha / 255f)
}

fun resetGlColor() {
    gl.color(1f, 1f, 1f, 1f)
}