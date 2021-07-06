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

package co.uk.isxander.evergreenhud

import club.sk1er.mods.core.ModCore
import co.uk.isxander.evergreenhud.elements.ElementManager
import co.uk.isxander.xanderlib.XanderLib
import co.uk.isxander.xanderlib.utils.Constants.*
import lombok.Getter
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
val DATA_DIR: File = File(mc.mcDataDir, "config/evergreenhud")

@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_REVISION, clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]", guiFactory = "co.uk.isxander.evergreenhud.gui.EvergreenGuiFactory", modLanguageAdapter = "co.uk.isxander.evergreenhud.adapter.KotlinLanguageAdapter")
object EvergreenHUD {

    @Getter private lateinit var elementManager: ElementManager

    private val guiKeybind: KeyBinding = KeyBinding("Open EvergreenHUD GUI", Keyboard.KEY_HOME, "EvergreenHUD")

    @Mod.EventHandler
    fun onFMLInit(event: FMLInitializationEvent) {
        ModCore.getInstance().initialize(mc.mcDataDir)
        XanderLib.getInstance().initPhase()

        Element()

        val progress = ProgressManager.push("EvergreenHUD", 9)

        progress.step("Blacklist Check")
        try {

        } catch (e: NullPointerException) {

        }
    }

}