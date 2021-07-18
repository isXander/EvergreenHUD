package dev.isxander.evergreenhud.compatibility.forge10809.impl

import dev.isxander.evergreenhud.compatibility.universal.RESOLUTION
import dev.isxander.evergreenhud.compatibility.universal.impl.AIMouseHelper
import org.lwjgl.input.Mouse

class MouseHelperImpl : AIMouseHelper() {
    override val mouseX: Float
        get() = Mouse.getX().toFloat() * RESOLUTION.scaledWidth.toFloat() / RESOLUTION.displayWidth.toFloat()
    override val mouseY: Float
        get() = RESOLUTION.scaledHeight.toFloat() - Mouse.getY().toFloat() * RESOLUTION.scaledHeight.toFloat() / RESOLUTION.displayHeight.toFloat() - 1f
    override val wasLeftMouseDown: Boolean
        get() = Mouse.isButtonDown(0)
    override val wasRightMouseDown: Boolean
        get() = Mouse.isButtonDown(1)
}