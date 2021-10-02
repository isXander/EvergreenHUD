/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.gui.components

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.elements.Element
import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.elements.type.BackgroundElement
import dev.isxander.evergreenhud.gui.effects.InvertedEffect
import dev.isxander.evergreenhud.utils.Notifications
import dev.isxander.evergreenhud.utils.color
import dev.isxander.evergreenhud.utils.ofMinecraftResource
import dev.isxander.evergreenhud.utils.resource
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIPoint
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.constraints.ColorConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.constraints.animation.AnimationStrategy
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.universal.UMatrixStack
import java.awt.Color
import java.io.File

class ElementComponent(private val element: Element) : UIComponent() {

    private var clickPos: Pair<Float, Float>? = null

    init {
        val self = this

        constrainSelf() effect OutlineEffect(Color.white, 0.5f)

        onMouseClick {
            clickPos = if (it.relativeX < 0 || it.relativeY < 0 || it.relativeX > getWidth() || it.relativeY > getHeight()) {
                null
            } else {
                it.relativeX to it.relativeY
            }
        }
        onMouseRelease {
            clickPos = null
        }
        onMouseDrag { mouseX, mouseY, button ->
            if (clickPos == null) return@onMouseDrag

            if (button == 0) {
                val paddingLeft = if (element is BackgroundElement) element.paddingLeft else 0f
                val paddingTop = if (element is BackgroundElement) element.paddingTop else 0f
                val posX = this@ElementComponent.getLeft() + mouseX - clickPos!!.first
                val posY = this@ElementComponent.getTop() + mouseY - clickPos!!.second

                element.position.rawX = posX - paddingLeft
                element.position.rawY = posY - paddingTop
                println("X: ${element.position.rawX}")
                println("Y: ${element.position.rawY}")

                this@ElementComponent.constrain {
                    x = posX.pixels()
                    y = posY.pixels()
                }
            }
        }

        val hoverable = UIContainer().constrain {
            width = 100.percent()
            height = 100.percent()
            color = color(255, 255, 255, 255)
        }.onMouseLeave {
            hide()
        }.onMouseEnter {
            unhide()
        }.animateBeforeHide {
            this.setColorAnimation(Animations.OUT_EXP, 0.25f, color(255, 255, 255, 0))
        }.animateAfterUnhide {
            this.setColorAnimation(Animations.IN_EXP, 0.25f, color(255, 255, 255, 255))
        }

        val overlay = UIRoundedRectangle(if (element is BackgroundElement) element.cornerRadius else 0f).constrain {
            width = 100.percent()
            height = 100.percent()
            color = color(255, 255, 255, 100)
        } childOf hoverable

        val settingsButton = UIImage.ofMinecraftResource(resource("settings.png")).constrain {
            height = 50.percent()
            width = height as RelativeConstraint
            x = 2.pixels()
            y = 50.percent() - 2.pixels()
        }.onMouseClick {
            if (it.mouseButton != 0) return@onMouseClick

            Notifications.push("EvergreenHUD", "This feature is yet to be added.")
        } childOf hoverable effect InvertedEffect()
    }

    override fun draw(matrixStack: UMatrixStack) {
        element.render(1f, RenderOrigin.GUI)
        super.draw(matrixStack)
    }

    override fun onWindowResize() {
        super.onWindowResize()
        constrainSelf()
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
