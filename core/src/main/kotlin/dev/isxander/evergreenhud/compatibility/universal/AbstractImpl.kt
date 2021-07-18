package dev.isxander.evergreenhud.compatibility.universal

import dev.isxander.evergreenhud.compatibility.universal.impl.*
import dev.isxander.evergreenhud.compatibility.universal.impl.keybind.AIKeybindManager
import dev.isxander.evergreenhud.compatibility.universal.impl.render.AIGL11
import dev.isxander.evergreenhud.compatibility.universal.impl.render.AIBufferBuilder

lateinit var MC_VERSION: MCVersion
lateinit var LOGGER: AILogger
lateinit var MC: AIMinecraft
lateinit var KEYBIND_MANAGER: AIKeybindManager
lateinit var RESOLUTION: AIResolution
lateinit var SCREEN_HANDLER: AIScreenHandler
lateinit var GL: AIGL11
lateinit var BUFFER_BUILDER: AIBufferBuilder
lateinit var FONT_RENDERER: AIFontRenderer
lateinit var MOUSE_HELPER: AIMouseHelper
lateinit var PROFILER: AIProfiler
lateinit var LOADER: AILoader