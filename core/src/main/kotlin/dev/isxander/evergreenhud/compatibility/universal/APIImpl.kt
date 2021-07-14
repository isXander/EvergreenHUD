package dev.isxander.evergreenhud.compatibility.universal

import dev.isxander.evergreenhud.compatibility.universal.impl.*
import dev.isxander.evergreenhud.compatibility.universal.impl.keybind.AIKeybindManager

lateinit var MC_VERSION: MCVersion
lateinit var LOGGER: AILogger
lateinit var MC: AIMinecraft
lateinit var KEYBIND_MANAGER: AIKeybindManager
lateinit var RESOLUTION: AIResolution
lateinit var SCREEN_HANDLER: AIScreenHandler