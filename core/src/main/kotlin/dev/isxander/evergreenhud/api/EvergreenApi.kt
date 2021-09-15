/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.api

import dev.isxander.evergreenhud.api.impl.*
import dev.isxander.evergreenhud.api.impl.render.UBufferBuilder
import dev.isxander.evergreenhud.api.impl.render.UGL
import dev.isxander.evergreenhud.utils.DI
import dev.isxander.evergreenhud.utils.MCVersion

val di = DI()

val mcVersion: MCVersion by lazy { di.get() }
val logger: ULogManager by lazy { di.get() }
val mc: UMinecraft by lazy { di.get() }
val keybindManager: UKeybindManager by lazy { di.get() }
val resolution: UResolution by lazy { di.get() }
val gl: UGL by lazy { di.get() }
val bufferBuilder: UBufferBuilder by lazy { di.get() }
val fontRenderer: UFontRenderer by lazy { di.get() }
val mouseHelper: UMouseHelper by lazy { di.get() }
val profiler: UProfiler by lazy { di.get() }
val loader: ULoader by lazy { di.get() }
val world: UWorld by lazy { di.get() }
val commandHandler: UCommandHandler by lazy { di.get() }
val screenHandler: UScreenHandler by lazy { di.get() }
val potions: UPotions by lazy { di.get() }
val translation: UTranslation by lazy { di.get() }
