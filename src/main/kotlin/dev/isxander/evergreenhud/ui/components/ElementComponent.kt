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
import dev.isxander.evergreenhud.ui.ElementDisplay
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.constraint
import dev.isxander.evergreenhud.utils.ofIdentifier
import dev.isxander.evergreenhud.utils.resource
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.constraints.ImageAspectConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.utils.Vector2f
import gg.essential.universal.UMatrixStack
import gg.essential.universal.UResolution
import kotlin.math.abs

class ElementComponent(val element: Element, val display: ElementDisplay) : UIComponent() {
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

    val verticalSnapBlock by UIBlock(Color.red.awt) childOf display.window
    val horizontalSnapBlock by UIBlock(Color.red.awt) childOf display.window


    init {
        settingsButton.hide(true)
        removeButton.hide(true)

        verticalSnapBlock.hide()
        horizontalSnapBlock.hide()

        constrainSelf() effect outlineEffect

        onMouseClick {
            clickPos = if (it.relativeX < 0 || it.relativeY < 0 || it.relativeX > getWidth() || it.relativeY > getHeight()) {
                null
            } else {
                it.relativeX to it.relativeY
            }

            verticalSnapBlock.unhide()
            horizontalSnapBlock.unhide()
        }
        onMouseRelease {
            clickPos = null

            verticalSnapBlock.hide()
            horizontalSnapBlock.hide()
        }

        onMouseDrag { mouseX, mouseY, button ->
            if (clickPos == null) return@onMouseDrag

            if (button == 0) {
                val hitbox = element.calculateHitBox(element.position.scale)
                val posX = this@ElementComponent.getLeft() + mouseX - clickPos!!.first
                val posY = this@ElementComponent.getTop() + mouseY - clickPos!!.second

                val targetPosX = posX + (element.position.rawX - hitbox.x1)
                val targetPosY = posY + (element.position.rawY - hitbox.y1)

                element.position.rawX = targetPosX
                element.position.rawY = targetPosY

                if (!EvergreenHUD.elementManager.elementSnapping)
                    return@onMouseDrag

                val movedHitbox = element.calculateHitBox(element.position.scale)

                val scaledSnapPoints = element.snapPoints.map {
                    Vector2f(movedHitbox.x1 + movedHitbox.width * it.x, movedHitbox.y1 + movedHitbox.height * it.y)
                }

                val otherScaledSnapPoints = display.elements
                    .filter { it !== this@ElementComponent }
                    .map {
                        val otherHitbox = it.element.calculateHitBox(element.position.scale)
                        it.element.snapPoints.map { point ->
                            Vector2f(otherHitbox.x1 + otherHitbox.width * point.x, otherHitbox.y1 + otherHitbox.height * point.y)
                        }
                    }.flatten() + display.globalSnapPoints.map {
                    Vector2f(UResolution.scaledWidth * it.x, UResolution.scaledHeight * it.y)
                }

                var verticalSnap: Pair<Vector2f, Vector2f>? = null
                var horizontalSnap: Pair<Vector2f, Vector2f>? = null
                val snapThreshold = 5f
                for (snapPoint in scaledSnapPoints) {
                    for (otherSnapPoint in otherScaledSnapPoints) {
                        // horizontal line, vertical snap
                        if (verticalSnap == null) {
                            if (abs(snapPoint.y - otherSnapPoint.y) <= snapThreshold) {
                                verticalSnap = snapPoint to otherSnapPoint
                            }
                        }

                        // vertical line, horizontal snap
                        if (horizontalSnap == null) {
                            if (abs(snapPoint.x - otherSnapPoint.x) <= snapThreshold) {
                                horizontalSnap = snapPoint to otherSnapPoint
                            }
                        }
                    }
                }

                if (verticalSnap != null) {
                    val origin = verticalSnap.first.y - movedHitbox.y1 - (element.position.rawY - movedHitbox.y1)
                    element.position.rawY = verticalSnap.second.y - origin

                    verticalSnapBlock.constrain {
                        x = kotlin.math.min(verticalSnap.first.x, verticalSnap.second.x).pixels()
                        y = verticalSnap.second.y.pixels() - 0.5.pixels()
                        width = abs(verticalSnap.second.x - verticalSnap.first.x).pixels()
                        height = 1.pixels()
                    }
                } else {
                    verticalSnapBlock.constrain {
                        width = 0.pixels()
                        height = 0.pixels()
                    }
                }
                if (horizontalSnap != null) {
                    val origin = horizontalSnap.first.x - movedHitbox.x1 - (element.position.rawX - movedHitbox.x1)
                    element.position.rawX = horizontalSnap.second.x - origin

                    horizontalSnapBlock.constrain {
                        x = horizontalSnap.second.x.pixels() - 0.5.pixels()
                        y = kotlin.math.min(horizontalSnap.first.y, horizontalSnap.second.y).pixels()
                        width = 1.pixels()
                        height = abs(horizontalSnap.second.y - horizontalSnap.first.y).pixels()
                    }
                } else {
                    horizontalSnapBlock.constrain {
                        width = 0.pixels()
                        height = 0.pixels()
                    }
                }
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
