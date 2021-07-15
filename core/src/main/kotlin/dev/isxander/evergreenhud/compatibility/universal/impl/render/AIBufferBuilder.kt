package dev.isxander.evergreenhud.compatibility.universal.impl.render

abstract class AIBufferBuilder {

    abstract fun vertex(x: Double, y: Double, z: Double): AIBufferBuilder
    abstract fun color(r: Float, g: Float, b: Float, a: Float): AIBufferBuilder
    abstract fun tex(u: Double, v: Double): AIBufferBuilder

    abstract fun next(): AIBufferBuilder
    abstract fun end(): AIBufferBuilder

    abstract fun begin(mode: DrawMode, format: VertexFormats): AIBufferBuilder

    abstract fun draw()

}