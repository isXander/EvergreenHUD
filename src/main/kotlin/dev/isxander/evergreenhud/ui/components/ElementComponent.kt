/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui.components

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.elements.Element
import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.constraint
import dev.isxander.evergreenhud.utils.ofIdentifier
import dev.isxander.evergreenhud.utils.resource
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.constraints.ImageAspectConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.universal.UMatrixStack

class ElementComponent(val element: Element) : UIComponent() {
    private var clickPos: Pair<Float, Float>? = null

    val settingsButton by UIImage.ofIdentifier(resource("ui/settings.png")).constrain {
        x = 2.percent()
        y = (100 - 4).percent() - (10.pixels() * element.position.scale)
        width = ImageAspectConstraint()
        height = 10.pixels() * element.position.scale
    }.animateAfterUnhide {
        setColorAnimation(Animations.IN_OUT_SIN, 0.3f, Color.white.constraint)
    }.animateBeforeHide {
        setColorAnimation(Animations.IN_OUT_SIN, 0.3f, Color.white.withAlpha(0).constraint)
    } childOf this

    val removeButton by UIImage.ofIdentifier(resource("ui/close.png")).constrain {
        x = 0.pixels(alignOpposite = true) - 2.percent()
        y = (100 - 4).percent() - (10.pixels() * element.position.scale)
        width = ImageAspectConstraint()
        height = 10.pixels() * element.position.scale
    }.onMouseClick {
        EvergreenHUD.elementManager.removeElement(element)
        this@ElementComponent.hide(true)
    }.animateAfterUnhide {
        setColorAnimation(Animations.IN_OUT_SIN, 0.3f, Color.white.constraint)
    }.animateBeforeHide {
        setColorAnimation(Animations.IN_OUT_SIN, 0.3f, Color.white.withAlpha(0).constraint)
    } childOf this

    val outlineEffect = OutlineEffect(Color.white.withAlpha(0).awt, 0.5f)

    init {
        settingsButton.hide(true)
        removeButton.hide(true)

        constrainSelf() effect outlineEffect

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
                val hitbox = element.calculateHitBox(element.position.scale)
                val posX = this@ElementComponent.getLeft() + mouseX - clickPos!!.first
                val posY = this@ElementComponent.getTop() + mouseY - clickPos!!.second

                element.position.rawX = posX + (element.position.rawX - hitbox.x1)
                element.position.rawY = posY + (element.position.rawY - hitbox.y1)
            }
        }

        onMouseEnter {
            outlineEffect::color.animate(Animations.IN_OUT_SIN, 0.3f, Color.white.awt)
            settingsButton.unhide()
            removeButton.unhide()
        }

        onMouseLeave {
            outlineEffect::color.animate(Animations.IN_OUT_SIN, 0.3f, Color.white.withAlpha(0).awt)
            settingsButton.hide()
            removeButton.hide()
        }
    }

    override fun draw(matrixStack: UMatrixStack) {
        beforeDrawCompat(matrixStack)

        constrainSelf()
        element.render(RenderOrigin.GUI)

        super.draw(matrixStack)
    }

    override fun onWindowResize() {
        super.onWindowResize()
        constrainSelf()
    }

    private fun constrainSelf(): ElementComponent {
        return constrain {
            val hitbox = element.calculateHitBox(element.position.scale)
            width = hitbox.width.pixels()
            height = hitbox.height.pixels()
            x = hitbox.x1.pixels()
            y = hitbox.y1.pixels()
        }
    }
}
