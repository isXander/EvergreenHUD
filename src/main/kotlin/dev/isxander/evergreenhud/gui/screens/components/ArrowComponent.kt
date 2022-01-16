/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.gui.screens.components

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.dsl.*

/**
 * Easiest method of keeping the paths for the texture constant.
 *
 * @author Deftu
 * @since 2.0
 */
class ArrowComponent : UIComponent() {
    init {
        UIImage.ofResource("/assets/evergreenhud/textures/arrow.png").constrain {
            width = RelativeConstraint()
            height = RelativeConstraint()
        } childOf this
    }
}
