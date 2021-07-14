package dev.isxander.evergreenhud.compatibility.universal.impl

import java.io.File

abstract class AIMinecraft {

    abstract fun dataDir(): File
    abstract fun fps(): Int
    abstract fun inGameHasFocus(): Boolean

}