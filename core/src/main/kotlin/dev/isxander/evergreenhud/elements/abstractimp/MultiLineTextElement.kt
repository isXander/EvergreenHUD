package dev.isxander.evergreenhud.elements.abstractimp

import dev.isxander.evergreenhud.settings.impl.IntSetting

abstract class MultiLineTextElement : TextElement() {

    @IntSetting(name = "Vertical Spacing", category = ["Text"], "How far apart each line of text is.", min = 0, max = 5)
    var verticalSpacing = 2



}