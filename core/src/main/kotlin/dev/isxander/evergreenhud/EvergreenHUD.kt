/*
 * Copyright (C) isXander [2019 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud

import club.sk1er.mods.core.ModCore
import dev.isxander.evergreenhud.elements.ElementManager
import co.uk.isxander.xanderlib.XanderLib
import co.uk.isxander.xanderlib.utils.Constants.*
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.ProgressManager
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.input.Keyboard
import java.io.File
import java.lang.NullPointerException

val LOGGER: Logger = LogManager.getLogger("EvergreenHUD")
val DATA_DIR: File = File(mc.mcDataDir, "evergreenhud")

@Mod(modid = MOD_ID, name = "EvergreenHUD (Core)", version = MOD_REVISION, clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]", guiFactory = "dev.isxander.evergreenhud.gui.EvergreenGuiFactory", modLanguageAdapter = "dev.isxander.evergreenhud.adapter.KotlinLanguageAdapter")
object EvergreenHUD {

    lateinit var elementManager: ElementManager
        private set

    private val guiKeybind: KeyBinding = KeyBinding("Open EvergreenHUD GUI", Keyboard.KEY_HOME, "EvergreenHUD")

    @Mod.EventHandler
    fun onFMLInit(event: FMLInitializationEvent) {
        ModCore.getInstance().initialize(mc.mcDataDir)
        XanderLib.getInstance().initPhase()

        val progress = ProgressManager.push("EvergreenHUD", 9)

        elementManager = ElementManager()
        for (entry in elementManager.getAvailableElements()) {
            val element = entry.value.newInstance()
            println(element.metadata.name)
            for (setting in element.settings) {
                println("  ${setting.getName()}")
            }
        }

        progress.step("Blacklist Check")
        try {

        } catch (e: NullPointerException) {

        }
    }

}