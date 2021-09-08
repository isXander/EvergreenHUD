package dev.isxander.evergreenhud.utils

import dev.isxander.evergreenhud.api.gl
import java.awt.Color

fun Color.applyToGL() {
    gl.color(this.red / 255f, this.green / 255f, this.blue / 255f, this.alpha / 255f)
}

fun resetGlColor() {
    gl.color(1f, 1f, 1f, 1f)
}