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
import dev.isxander.evergreenhud.utils.drawBorderLines
import gg.essential.api.EssentialAPI
import net.minecraft.client.gui.GuiScreen

open class ElementDisplay(private val parent: GuiScreen?) : GuiScreen() {
    protected var renderElements = true

    protected var dragging: Element? = null
    protected var lastClicked: Element? = null
    protected var offX = 0f
    protected var offY = 0f

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        renderElements(mouseX, mouseY, partialTicks)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    open fun renderElements(mouseX: Int, mouseY: Int, delta: Float) {
        if (!renderElements) return

        for (element in EvergreenHUD.elementManager.currentElements) {

            element.render(RenderOrigin.GUI)

            val hitbox = element.calculateHitBox(element.position.scale)
            val width = 0.5f

            drawBorderLines(hitbox.x1 - width, hitbox.y1 - width, hitbox.x1 + hitbox.width, hitbox.y1 + hitbox.height, width, -1)
        }

        if (dragging != null) {
            val elementX = mouseX - offX
            val elementY = mouseY - offY

            dragging!!.position.rawX = elementX
            dragging!!.position.rawY = elementY
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        var clickedElement = false

        for (e in EvergreenHUD.elementManager.currentElements.reversed()) {
            // e.onMouseClicked()
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
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, button: Int) {
        dragging = null
        offX = 0f
        offY = 0f

        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun onGuiClosed() {
        super.onGuiClosed()
        EssentialAPI.getGuiUtil().openScreen(parent)
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }
}
