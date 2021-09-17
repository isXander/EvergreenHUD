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

package dev.isxander.evergreenhud.api.fabric11701

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.api.di
import dev.isxander.evergreenhud.api.impl.*
import dev.isxander.evergreenhud.api.impl.render.UBufferBuilder
import dev.isxander.evergreenhud.api.impl.render.UGL
import dev.isxander.evergreenhud.api.fabric11701.events.ServerDamageEntityEventManager
import dev.isxander.evergreenhud.api.fabric11701.impl.*
import dev.isxander.evergreenhud.api.fabric11701.impl.KeybindManagerImpl
import dev.isxander.evergreenhud.event.ClientTickEvent
import dev.isxander.evergreenhud.utils.MCVersion
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack

val mc: MinecraftClient = MinecraftClient.getInstance()

object Main : ClientModInitializer {
    lateinit var matrices: MatrixStack
    var postInitialized = false

    override fun onInitializeClient() {
        di.bind(MCVersion.FABRIC_1_17_1)

        di.bind<ULogManager>(LogManagerImpl())
        di.bind<UProfiler>(ProfilerImpl())
        di.bind<UMinecraft>(MinecraftImpl())
        di.bind<UResolution>(ResolutionImpl())
        di.bind<UKeybindManager>(KeybindManagerImpl())
        di.bind<UBufferBuilder>(BufferBuilderImpl())
        di.bind<UGL>(GLImpl())
        di.bind<UFontRenderer>(FontRenderImpl())
        di.bind<UMouseHelper>(MouseHelperImpl())
        di.bind<UCommandHandler>(CommandHandlerImpl())
        di.bind<UScreenHandler>(ScreenHandlerImpl())
        di.bind<UTranslation>(TranslationImpl())
        di.bind<UPotions>(PotionsImpl())
        di.bind<UWorld>(WorldImpl())

        registerEvents()

        EvergreenHUD.init()
    }

    private fun registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register {
            EvergreenHUD.eventBus.post(ClientTickEvent())
        }

        EvergreenHUD.eventBus.register(ServerDamageEntityEventManager())
    }
}
