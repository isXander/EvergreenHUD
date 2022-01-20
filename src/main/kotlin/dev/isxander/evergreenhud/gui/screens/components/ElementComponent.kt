/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.gui.screens.components

import dev.isxander.evergreenhud.elements.Element
import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.elements.type.BackgroundElement
import dev.isxander.evergreenhud.utils.ofIdentifier
import dev.isxander.evergreenhud.utils.resource
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.universal.UMatrixStack
import org.lwjgl.glfw.GLFW
import java.awt.Color

class ElementComponent(
    private val element: Element,
    private val selected: (ElementComponent) -> Boolean
) : UIComponent() {

    private var dragging = false
    private var clickPos: Pair<Float, Float>? = null

    init {
        constrainSelf() effect OutlineEffect(Color.WHITE, 0.5f)

        onMouseClick {
            if (selected(this@ElementComponent)) {
                dragging = true
                clickPos = if (it.relativeX < 0 || it.relativeY < 0 || it.relativeX > getWidth() || it.relativeY > getHeight()) {
                    null
                } else {
                    it.relativeX to it.relativeY
                }
            }
        }.onMouseRelease {
            dragging = false
        }.onMouseDrag { mouseX, mouseY, button ->
            if (clickPos == null) return@onMouseDrag
            if (!dragging) return@onMouseDrag
            if (!selected(this@ElementComponent)) return@onMouseDrag
            if (button == 0) {
                updatePosition(mouseX, mouseY)
            }
        }.onKeyType { _, keyCode ->
            if (selected(this@ElementComponent)) {
                when(keyCode) {
                    GLFW.GLFW_KEY_LEFT -> {  }
                    GLFW.GLFW_KEY_UP -> {  }
                    GLFW.GLFW_KEY_DOWN -> {  }
                    GLFW.GLFW_KEY_RIGHT -> {  }
                }
            }
        }

        val hoverable = UIContainer().apply {
            constrain {
                width = 100.percent()
                height = 100.percent()
                color = Color(255, 255, 255, 255).toConstraint()
            }

            animateBeforeHide {
                setColorAnimation(Animations.OUT_CUBIC, 0.5f, Color(255, 255, 255, 0).toConstraint())
            }.animateAfterUnhide {
                setColorAnimation(Animations.IN_CUBIC, 0.5f, Color(255, 255, 255, 255).toConstraint())
            } childOf this@ElementComponent

            hide(true)
        }

        onMouseEnter {
            hoverable.unhide()
        }.onMouseLeave {
            hoverable.hide()
        }

        val overlay = UIRoundedRectangle(if (element is BackgroundElement) element.cornerRadius else 0f).constrain {
            width = 100.percent()
            height = 100.percent()
            color = Color(255, 255, 255, 100).toConstraint()
        } childOf hoverable

        val settingsButton = UIImage.ofIdentifier(resource("textures/settings.png")).constrain {
            height = 50.percent()
            width = height as RelativeConstraint
            x = 2.pixels()
            y = 50.percent() - 2.pixels()
        }.onMouseClick {
            if (it.mouseButton != GLFW.GLFW_MOUSE_BUTTON_1) return@onMouseClick
        } childOf hoverable
    }

    override fun draw(matrixStack: UMatrixStack) {
        element.render(matrixStack.toMC(), RenderOrigin.GUI)
        super.draw(matrixStack)
        constrainSelf()
    }

    override fun onWindowResize() {
        super.onWindowResize()
        constrainSelf()
    }

    fun updatePosition(newX: Float, newY: Float) {
        val paddingLeft = if (element is BackgroundElement) element.paddingLeft else 0f
        val paddingTop = if (element is BackgroundElement) element.paddingTop else 0f
        val posX = this@ElementComponent.getLeft() + newX - clickPos!!.first
        val posY = this@ElementComponent.getTop() + newY - clickPos!!.second

        element.position.rawX = posX - paddingLeft
        element.position.rawY = posY - paddingTop
        println("X: ${element.position.rawX}")
        println("Y: ${element.position.rawY}")

        this@ElementComponent.constrain {
            x = posX.pixels()
            y = posY.pixels()
        }
    }

    private fun constrainSelf(): ElementComponent {
        return constrain {
            val hitbox = element.calculateHitBox(1f, element.position.scale)
            width = hitbox.width.pixels()
            height = hitbox.height.pixels()
            x = element.position.rawX.pixels() - if (element is BackgroundElement) element.paddingLeft.pixels() else 0.pixels()
            y = element.position.rawY.pixels() - if (element is BackgroundElement) element.paddingTop.pixels() else 0.pixels()
        }
    }
}
