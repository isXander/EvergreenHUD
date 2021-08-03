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

package dev.isxander.evergreenhud.compatibility.fabric11701.impl

import dev.isxander.evergreenhud.compatibility.fabric11701.mc
import dev.isxander.evergreenhud.compatibility.fabric11701.mixins.AccessorMinecraftClient
import dev.isxander.evergreenhud.compatibility.universal.impl.UMinecraft
import dev.isxander.evergreenhud.compatibility.universal.impl.UEntity
import net.fabricmc.loader.api.FabricLoader
import java.io.File

class MinecraftImpl : UMinecraft() {

    override val player: UEntity get() = EntityImpl(mc.player)
    override val dataDir: File = mc.runDirectory
    override val fps: Int get() = AccessorMinecraftClient.getFps()
    override val inGameHasFocus: Boolean get() = mc.currentScreen != null
    override val devEnv: Boolean = FabricLoader.getInstance().isDevelopmentEnvironment

}