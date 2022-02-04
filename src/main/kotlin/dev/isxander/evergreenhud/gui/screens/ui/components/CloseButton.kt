/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.gui.screens.ui.components

import dev.isxander.evergreenhud.gui.screens.ui.EvergreenPalette
import dev.isxander.evergreenhud.utils.constraint
import dev.isxander.evergreenhud.utils.ofIdentifier
import dev.isxander.evergreenhud.utils.resource
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.*

class CloseButton : UIRoundedRectangle(1f) {
    init {
        constrain {
            color = EvergreenPalette.Greyscale.Dark3.constraint
            radius = 6.pixels()
        }
    }

    val icon by UIImage.ofIdentifier(resource("ui/close.png")).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 50.percent()
        height = 50.percent()
    } childOf this
}
