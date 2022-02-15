/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui.components

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.ui.EvergreenPalette
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.constraints.TextAspectConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect

class ElementDescriptionComponent(val element: ElementMeta) : UIBlock(EvergreenPalette.Greyscale.Dark3.awt) {
    val padded by UIContainer().constrain {
        x = 2.percent()
        y = CenterConstraint()
        width = 96.percent()
        height = ChildBasedSizeConstraint()
    } childOf this

    val titleText by UIText(element.name, false).constrain {
        width = TextAspectConstraint()
        height = 40.percent() boundTo this@ElementDescriptionComponent
    } childOf padded

    val outline = OutlineEffect(Color.white.withAlpha(0).awt, 1f, drawInsideChildren = true)

    init {
        if (element.description != "") {
            val descriptionText by UIText(element.description, false).constrain {
                y = SiblingConstraint(3f)
                width = TextAspectConstraint()
                height = 20.percent() boundTo this@ElementDescriptionComponent
            } childOf padded
        }

        onMouseClick {
            EvergreenHUD.elementManager.addElement(EvergreenHUD.elementManager.getNewElementInstance(element.id)!!)
        }

        this effect outline
        onMouseEnter {
            outline::color.animate(Animations.IN_OUT_SIN, 0.1f, Color.white.awt)
        }
        onMouseLeave {
            outline::color.animate(Animations.IN_OUT_SIN, 0.1f, Color.white.withAlpha(0).awt)
        }
    }


}
