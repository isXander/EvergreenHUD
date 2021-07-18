package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.compatibility.universal.MC
import dev.isxander.evergreenhud.elements.ElementMeta
import dev.isxander.evergreenhud.elements.abstractimp.SimpleTextElement

@ElementMeta(id = "FPS", name = "FPS Display", category = "Simple", description = "Display how many times your screen is updating every second.")
class ElementFps : SimpleTextElement() {
    override fun calculateValue(): String = MC.fps.toString()
    override var title: String = "FPS"
}