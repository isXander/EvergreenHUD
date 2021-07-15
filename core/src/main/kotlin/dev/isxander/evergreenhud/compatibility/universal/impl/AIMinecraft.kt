package dev.isxander.evergreenhud.compatibility.universal.impl

import dev.isxander.evergreenhud.compatibility.universal.impl.entity.AIEntity
import java.io.File

abstract class AIMinecraft {

    abstract fun player(): AIEntity
    abstract fun dataDir(): File
    abstract fun fps(): Int
    abstract fun inGameHasFocus(): Boolean

}