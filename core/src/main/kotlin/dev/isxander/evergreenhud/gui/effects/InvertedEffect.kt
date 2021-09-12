/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.gui.effects

import dev.isxander.evergreenhud.api.gl
import dev.isxander.evergreenhud.api.impl.render.UGL
import gg.essential.elementa.effects.Effect
import gg.essential.universal.UMatrixStack

class InvertedEffect : Effect() {

    override fun beforeDraw(matrixStack: UMatrixStack) {
        gl.blendFuncSeparate(UGL.GL_ONE_MINUS_DST_COLOR, UGL.GL_ONE_MINUS_SRC_COLOR, 1, 0)
    }

    override fun beforeChildrenDraw(matrixStack: UMatrixStack) {
        gl.defaultBlendFunc()
    }

}