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

package dev.isxander.evergreenhud.compatibility.fabric11701.impl

import dev.isxander.evergreenhud.compatibility.fabric11701.mc
import dev.isxander.evergreenhud.compatibility.universal.RESOLUTION
import dev.isxander.evergreenhud.compatibility.universal.impl.UMouseHelper
import org.lwjgl.glfw.GLFW

class MouseHelperImpl : UMouseHelper() {
    override val mouseX: Float get() = (mc.mouse.x * (RESOLUTION.scaledWidth / RESOLUTION.displayWidth)).toFloat()
    override val mouseY: Float get() = (mc.mouse.y * (RESOLUTION.scaledHeight / RESOLUTION.displayHeight)).toFloat()
    override val wasLeftMouseDown: Boolean get() = mc.mouse.wasLeftButtonClicked()
    override val wasRightMouseDown: Boolean get() = mc.mouse.wasRightButtonClicked()

    override fun isButtonDown(button: Int): Boolean {
        return GLFW.glfwGetMouseButton(mc.window.handle, button) == GLFW.GLFW_PRESS
    }
}