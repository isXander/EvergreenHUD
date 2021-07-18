package dev.isxander.evergreenhud.compatibility.fabric11701.impl

import dev.isxander.evergreenhud.compatibility.fabric11701.mc
import dev.isxander.evergreenhud.compatibility.universal.impl.AIMouseHelper

class MouseHelperImpl : AIMouseHelper() {
    override val mouseX: Float get() = mc.mouse.x.toFloat()
    override val mouseY: Float get() = mc.mouse.y.toFloat()
    override val wasLeftMouseDown: Boolean get() = mc.mouse.wasLeftButtonClicked()
    override val wasRightMouseDown: Boolean get() = mc.mouse.wasRightButtonClicked()
}