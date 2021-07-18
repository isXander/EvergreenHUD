package dev.isxander.evergreenhud.gui.effects

import dev.isxander.evergreenhud.compatibility.universal.GL
import dev.isxander.evergreenhud.compatibility.universal.impl.render.AIGL11
import gg.essential.elementa.effects.Effect
import gg.essential.universal.UMatrixStack

class InvertedEffect : Effect() {

    override fun beforeDraw(matrixStack: UMatrixStack) {
        GL.blendFuncSeparate(AIGL11.GL_ONE_MINUS_DST_COLOR, AIGL11.GL_ONE_MINUS_SRC_COLOR, 1, 0)
    }

    override fun afterDraw(matrixStack: UMatrixStack) {
        GL.defaultBlendFunc()
    }

}