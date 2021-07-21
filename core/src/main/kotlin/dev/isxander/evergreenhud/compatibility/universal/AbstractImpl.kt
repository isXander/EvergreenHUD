/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/gpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

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