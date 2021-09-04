package dev.isxander.evergreenhud.api

import dev.isxander.evergreenhud.api.impl.*
import dev.isxander.evergreenhud.api.impl.render.UBufferBuilder
import dev.isxander.evergreenhud.api.impl.render.UGL
import dev.isxander.evergreenhud.utils.DI

val di = DI()

val mcVersion: MCVersion get() = di.get()
val logger: ULogManager get() = di.get()
val mc: UMinecraft get() = di.get()
val keybindManager: UKeybindManager get() = di.get()
val resolution: UResolution get() = di.get()
val gl: UGL get() = di.get()
val bufferBuilder: UBufferBuilder get() = di.get()
val fontRenderer: UFontRenderer get() = di.get()
val mouseHelper: UMouseHelper get() = di.get()
val profiler: UProfiler get() = di.get()
val loader: ULoader get() = di.get()
val world: UWorld get() = di.get()
val commandHandler: UCommandHandler get() = di.get()
val screenHandler: UScreenHandler get() = di.get()
val potions: UPotions get() = di.get()
val translation: UTranslation get() = di.get()