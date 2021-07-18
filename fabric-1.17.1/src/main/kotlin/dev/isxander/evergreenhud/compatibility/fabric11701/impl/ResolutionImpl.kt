package dev.isxander.evergreenhud.compatibility.fabric11701.impl

import dev.isxander.evergreenhud.compatibility.fabric11701.mc
import dev.isxander.evergreenhud.compatibility.universal.impl.AIResolution

class ResolutionImpl : AIResolution() {

    override val displayWidth: Int get() = mc.window.width
    override val displayHeight: Int get() = mc.window.height

    override val scaledWidth: Int get() = mc.window.scaledWidth
    override val scaledHeight: Int get() = mc.window.scaledHeight

    override val scaleFactor: Double get() = mc.window.scaleFactor

}