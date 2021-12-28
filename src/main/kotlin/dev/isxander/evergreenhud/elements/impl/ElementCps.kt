/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.OptionContainer
import dev.isxander.settxi.impl.option
import net.minecraft.client.util.math.MatrixStack
import org.lwjgl.glfw.GLFW
import kotlin.collections.ArrayDeque

@ElementMeta(id = "CPS", name = "Cps Counter", category = "Combat", description = "How many times you click in a second.")
class ElementCps : SimpleTextElement("CPS") {
    var button by option(MouseButton.BOTH) {
        name = "Button"
        category = "CPS"
        description = "Which button to track."
    }

    private val left = ArrayDeque<Long>()
    private var leftPressed = false
    private val right = ArrayDeque<Long>()
    private var rightPressed = false

    override fun calculateValue(): String {
        return when (button) {
            MouseButton.LEFT -> left.size.toString()
            MouseButton.RIGHT -> right.size.toString()
            MouseButton.BOTH -> "${left.size} | ${right.size}"
            else -> throw IllegalStateException()
        }
    }

    override fun onRenderTick(matrices: MatrixStack, tickDelta: Float) {
        var pressed = GLFW.glfwGetMouseButton(mc.window.handle, 0) == GLFW.GLFW_PRESS

        if (pressed != leftPressed) {
            leftPressed = pressed
            if (pressed) left.add(System.currentTimeMillis())
        }

        pressed = GLFW.glfwGetMouseButton(mc.window.handle, 1) == GLFW.GLFW_PRESS

        if (pressed != rightPressed) {
            rightPressed = pressed
            if (pressed) right.add(System.currentTimeMillis())
        }

        val currentTime = System.currentTimeMillis()
        if (!left.isEmpty()) {
            while ((currentTime - left.first()) > 1000) {
                left.removeFirst()
                if (left.isEmpty()) break
            }
        }
        if (!right.isEmpty()) {
            while ((currentTime - right.first()) > 1000) {
                right.removeFirst()
                if (right.isEmpty()) break
            }
        }
    }

    object MouseButton : OptionContainer() {
        val LEFT = option("Left Click")
        val RIGHT = option("Right Click")
        val BOTH = option("Both Buttons")
    }
}
