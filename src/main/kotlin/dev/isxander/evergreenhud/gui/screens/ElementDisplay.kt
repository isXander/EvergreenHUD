/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.gui.screens

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.elements.Element
import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.gui.components.PrecisionTexturedButtonWidget
import dev.isxander.evergreenhud.utils.*
import io.ejekta.kambrik.text.textLiteral
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack

open class ElementDisplay(private val parent: Screen? = mc.currentScreen) : Screen(textLiteral("EvergreenHUD")) {
    protected var renderElements = true

    protected var dragging: Element? = null
    protected var lastClicked: Element? = null
    protected var offX = 0f
    protected var offY = 0f

    override fun init() {
        for (element in EvergreenHUD.elementManager) {
            addDrawableChild(
                PrecisionTexturedButtonWidget(
                    { element.position.rawX },
                    { element.position.rawY },
                    18f, 18f,
                    0f, 0f,
                    resource("settings.png"),
                    384f, 384f,
                ) {
                    logger.info("what is up guys")
                    true
                }
            )
        }
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        renderElements(matrices, mouseX, mouseY, delta)
        super.render(matrices, mouseX, mouseY, delta)
    }

    open fun renderElements(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        if (!renderElements) return

        val scaledMouseX = client!!.mouse.scaledX
        val scaledMouseY = client!!.mouse.scaledY

        for (element in EvergreenHUD.elementManager.currentElements) {

            element.render(matrices, RenderOrigin.GUI)

            val hitbox = element.calculateHitBox(1f, element.position.scale)
            val width = 0.5f

            matrices.drawBorderLines(hitbox.x1 - width, hitbox.y1 - width, hitbox.x1 + hitbox.width, hitbox.y1 + hitbox.height, width, -1)
        }

        if (dragging != null) {
            val elementX = scaledMouseX - offX
            val elementY = scaledMouseY - offY

            dragging!!.position.rawX = elementX.toFloat()
            dragging!!.position.rawY = elementY.toFloat()
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        var clickedElement = false

        for (e in EvergreenHUD.elementManager.currentElements.reversed()) {
            // e.onMouseClicked()
            if (e.calculateHitBox(1f, e.position.scale).overlaps(mouseX.toFloat(), mouseY.toFloat())) {
                lastClicked = e
                dragging = e

                offX = mouseX.toFloat() - e.position.rawX
                offY = mouseY.toFloat() - e.position.rawY

                clickedElement = true
                break
            }
        }
        if (lastClicked != null && EvergreenHUD.elementManager.currentElements.contains(lastClicked)) {
            EvergreenHUD.elementManager.currentElements.remove(lastClicked!!)
            EvergreenHUD.elementManager.currentElements.add(lastClicked!!)
        }

        if (clickedElement) return true

        dragging = null
        lastClicked = null
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        dragging = null
        offX = 0f
        offY = 0f

        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun onClose() {
        client?.setScreen(parent)
    }
}
