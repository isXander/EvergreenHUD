package dev.isxander.evergreenhud.compatibility.universal.impl

import dev.isxander.evergreenhud.compatibility.universal.impl.entity.AIEntity
import java.io.File

abstract class AIMinecraft {

    abstract val player: AIEntity
    abstract val dataDir: File
    abstract val fps: Int
    abstract val inGameHasFocus: Boolean

}