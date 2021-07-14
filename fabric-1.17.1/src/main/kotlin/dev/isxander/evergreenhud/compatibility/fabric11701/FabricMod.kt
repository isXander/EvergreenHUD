package dev.isxander.evergreenhud.compatibility.fabric11701

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.fabric11701.keybind.KeybindManager
import dev.isxander.evergreenhud.compatibility.fabric11701.mixins.AccessorMinecraftClient
import dev.isxander.evergreenhud.compatibility.universal.*
import dev.isxander.evergreenhud.compatibility.universal.impl.*
import dev.isxander.evergreenhud.event.TickEvent
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.dsl.childOf
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File

object FabricMod : ModInitializer {

    val logger: Logger = LogManager.getLogger("EvergreenHUD")

    override fun onInitialize() {
        val mc = MinecraftClient.getInstance()

        MC_VERSION = MCVersion.FABRIC_1_17_1

        LOGGER = object : AILogger() {
            override fun info(msg: String) = logger.info(msg)
            override fun warn(msg: String) = logger.warn(msg)
            override fun err(msg: String) = logger.error(msg)
        }

        MC = object : AIMinecraft() {
            override fun dataDir(): File = mc.runDirectory
            override fun fps(): Int = AccessorMinecraftClient.getFps()
            override fun inGameHasFocus(): Boolean = mc.currentScreen != null
        }

        RESOLUTION = object : AIResolution() {
            override fun getDisplayWidth(): Int = mc.window.width
            override fun getDisplayHeight(): Int = mc.window.height

            override fun getScaledWidth(): Int = mc.window.scaledWidth
            override fun getScaledHeight(): Int = mc.window.scaledHeight

            override fun getScaleFactor(): Double = mc.window.scaleFactor
        }

        SCREEN_HANDLER = object : AIScreenHandler() {
            override fun displayComponent(component: UIComponent) {
                mc.setScreen(object : WindowScreen() {
                    init { component childOf window }
                })
            }
        }

        KeybindManager()

        registerEvents()

        EvergreenHUD.init()
    }

    private fun registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register {
            EvergreenHUD.EVENT_BUS.post(TickEvent())
        }
    }

}