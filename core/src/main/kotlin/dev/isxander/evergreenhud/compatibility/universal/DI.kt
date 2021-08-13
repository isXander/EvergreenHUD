/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
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
import dev.isxander.evergreenhud.compatibility.universal.impl.render.*

lateinit var MC_VERSION: MCVersion
lateinit var LOGGER: ULogger
lateinit var MC: UMinecraft
lateinit var KEYBIND_MANAGER: UKeybindManager
lateinit var RESOLUTION: UResolution
lateinit var GL: UGL
lateinit var BUFFER_BUILDER: UBufferBuilder
lateinit var FONT_RENDERER: UFontRenderer
lateinit var MOUSE_HELPER: UMouseHelper
lateinit var PROFILER: UProfiler
lateinit var LOADER: ULoader
lateinit var WORLD: UWorld
lateinit var COMMAND_HANDLER: UCommandHandler
lateinit var SCREEN_HANDLER: UScreenHandler
lateinit var POTIONS: UPotions
lateinit var TRANSLATION: UTranslation