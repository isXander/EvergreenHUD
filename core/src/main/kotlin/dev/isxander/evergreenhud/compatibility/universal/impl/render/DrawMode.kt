package dev.isxander.evergreenhud.compatibility.universal.impl.render

enum class DrawMode(val glNum: Int) {
    LINES(AIGL11.GL_LINES),
    LINE_STRIP(AIGL11.GL_LINE_STRIP),

    TRIANGLES(AIGL11.GL_TRIANGLES),
    TRIANGLE_STRIP(AIGL11.GL_TRIANGLE_STRIP),
    TRIANGLE_FAN(AIGL11.GL_TRIANGLE_FAN),

    QUADS(AIGL11.GL_QUADS)
}