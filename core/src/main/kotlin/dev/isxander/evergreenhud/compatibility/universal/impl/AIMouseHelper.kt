package dev.isxander.evergreenhud.compatibility.universal.impl

abstract class AIMouseHelper {

    abstract fun getMouseX(): Float
    abstract fun getMouseY(): Float

    abstract fun wasLeftMouseDown(): Boolean
    abstract fun wasRightMouseDown(): Boolean

}