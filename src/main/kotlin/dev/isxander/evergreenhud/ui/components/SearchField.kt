/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui.components

import dev.isxander.evergreenhud.ui.EvergreenPalette
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.constraint
import dev.isxander.evergreenhud.utils.ofIdentifier
import dev.isxander.evergreenhud.utils.resource
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ImageAspectConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*

class SearchField : UIBlock(EvergreenPalette.Greyscale.Dark3.awt) {
    val content by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = (100 - 8).percent()
        height = (100 - 8).percent()
    } childOf this

    val icon by UIImage.ofIdentifier(resource("ui/search.png")).constrain {
        y = CenterConstraint()
        width = ImageAspectConstraint()
        height = 55.percent()
    } childOf content

    val text by UITextInput(placeholder = "Search...", shadow = false, cursorColor = Color(0xDCDCDC, false).awt).constrain {
        x = SiblingConstraint(2f)
        y = CenterConstraint() + 3.pixels()
        width = 95.percent()
        height = 100.percent()
        textScale = 0.5.pixels()
        color = Color(0xDCDCDC, false).constraint
    } childOf content

    init {
        text.lineHeight = 10f
        text.onMouseClick {
            text.grabWindowFocus()
        }
    }
}
