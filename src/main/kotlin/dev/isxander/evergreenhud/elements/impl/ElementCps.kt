/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.event.RenderTickEvent
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.OptionContainer
import dev.isxander.settxi.impl.option
import io.ejekta.kambrik.ext.client.getBoundKey
import net.minecraft.client.util.math.MatrixStack
import org.lwjgl.glfw.GLFW
import kotlin.collections.ArrayDeque

@ElementMeta(id = "evergreenhud:cps", name = "Cps Counter", category = "Combat", description = "How many times you click in a second.")
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

    val renderTickEvent by event<RenderTickEvent> {
        var pressed = GLFW.glfwGetMouseButton(mc.window.handle, mc.options.keyAttack.getBoundKey().code) == GLFW.GLFW_PRESS

        if (pressed != leftPressed) {
            leftPressed = pressed
            if (pressed) left.add(System.currentTimeMillis())
        }

        pressed = GLFW.glfwGetMouseButton(mc.window.handle, mc.options.keyUse.getBoundKey().code) == GLFW.GLFW_PRESS

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

    override fun calculateValue(): String {
        return when (button) {
            MouseButton.LEFT -> left.size.toString()
            MouseButton.RIGHT -> right.size.toString()
            MouseButton.BOTH -> "${left.size} | ${right.size}"
            else -> throw IllegalStateException()
        }
    }

    object MouseButton : OptionContainer() {
        val LEFT = option("Left Click")
        val RIGHT = option("Right Click")
        val BOTH = option("Both Buttons")
    }
}
