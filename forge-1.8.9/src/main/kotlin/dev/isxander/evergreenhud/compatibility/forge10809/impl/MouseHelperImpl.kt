/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/gpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

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