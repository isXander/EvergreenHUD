package dev.isxander.evergreenhud.gui.components

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.elements.Element
import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.gui.effects.InvertedEffect
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.universal.UMatrixStack
import java.awt.Color
import java.io.File

class ElementComponent(private val element: Element) : UIComponent() {

    init {
        constrain {
            val hitbox = element.calculateHitBox(1f, element.position.scale)
            width = hitbox.width.pixels()
            height = hitbox.height.pixels()
            x = element.position.rawX.pixels()
            y = element.position.rawY.pixels()
        } effect OutlineEffect(Color.white, width = 1f)

        val settingsButton = UIImage.ofFile(File(EvergreenHUD.RESOURCE_DIR, "settings.png")).constrain {
            width = 5.percent()
            height = 5.percent()
            x = 2.pixels()
            y = 2.pixels()
        } childOf this // effect InvertedEffect()
    }

    override fun draw(matrixStack: UMatrixStack) {
        element.render(matrixStack, 1f, RenderOrigin.GUI)
        super.draw(matrixStack)
    }

}