/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.gui.screens

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.elements.Element
import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.gui.screens.components.SidebarComponent
import dev.isxander.evergreenhud.utils.*
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.elementa.dsl.*
import gg.essential.universal.UMatrixStack

open class ElementConfigurationMenu : WindowScreen(ElementaVersion.V1) {

    /* UI/UX. */
    val sidebar = SidebarComponent() childOf window

    /* Element editing. */
    private var renderElements = true

    private var dragging: Element? = null
    private var lastClicked: Element? = null
    private var offX = 0f
    private var offY = 0f

    init {
        Inspector(window) childOf window
    }

    override fun onDrawScreen(matrices: UMatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderElements(matrices, mouseX, mouseY, delta)
        super.onDrawScreen(matrices, mouseX, mouseY, delta)
    }

    open fun renderElements(matrices: UMatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        if (!renderElements) return

        val scaledMouseX = client!!.mouse.scaledX
        val scaledMouseY = client!!.mouse.scaledY

        for (element in EvergreenHUD.elementManager.currentElements) {

            element.render(matrices.toMC(), RenderOrigin.GUI)

            val hitbox = element.calculateHitBox(element.position.scale)
            val width = 0.5f

            matrices.toMC().drawBorderLines(hitbox.x1 - width, hitbox.y1 - width, hitbox.x1 + hitbox.width, hitbox.y1 + hitbox.height, width, -1)
        }

        if (dragging != null) {
            val elementX = scaledMouseX - offX
            val elementY = scaledMouseY - offY

            dragging!!.position.rawX = elementX.toFloat()
            dragging!!.position.rawY = elementY.toFloat()
        }
    }

    override fun onMouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        var clickedElement = false

        for (e in EvergreenHUD.elementManager.currentElements.reversed()) {
            if (e.calculateHitBox(e.position.scale).overlaps(mouseX.toFloat(), mouseY.toFloat())) {
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

        if (clickedElement) return
        dragging = null
        lastClicked = null
        super.onMouseClicked(mouseX, mouseY, button)
    }

    override fun onMouseReleased(mouseX: Double, mouseY: Double, button: Int) {
        dragging = null
        offX = 0f
        offY = 0f
        super.onMouseReleased(mouseX, mouseY, button)
    }
}
