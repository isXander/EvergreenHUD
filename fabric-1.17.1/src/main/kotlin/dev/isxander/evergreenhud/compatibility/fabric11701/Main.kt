package dev.isxander.evergreenhud.compatibility.fabric11701

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.fabric11701.impl.*
import dev.isxander.evergreenhud.compatibility.fabric11701.keybind.KeybindManager
import dev.isxander.evergreenhud.compatibility.universal.*
import dev.isxander.evergreenhud.event.ClientTickEvent
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

val mc: MinecraftClient = MinecraftClient.getInstance()

object Main : ClientModInitializer {

    lateinit var matrices: MatrixStack
    var postInitialized = false

    override fun onInitializeClient() {
        MC_VERSION = MCVersion.FABRIC_1_17_1

        LOGGER = LoggerImpl()
        PROFILER = ProfilerImpl()
        MC = MinecraftImpl()
        RESOLUTION = ResolutionImpl()
        KEYBIND_MANAGER = KeybindManager()
        SCREEN_HANDLER = ScreenHandlerImpl()
        BUFFER_BUILDER = BufferBuilderImpl()
        GL = GLImpl()
        FONT_RENDERER = FontRenderImpl()
        MOUSE_HELPER = MouseHelperImpl()

        registerEvents()

        EvergreenHUD.init()
    }

    private fun registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register {
            EvergreenHUD.EVENT_BUS.post(ClientTickEvent())
        }

    }

}