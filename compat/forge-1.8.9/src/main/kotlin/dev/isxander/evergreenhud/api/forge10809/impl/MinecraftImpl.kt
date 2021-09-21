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

package dev.isxander.evergreenhud.api.forge10809.impl

import dev.isxander.evergreenhud.api.forge10809.mc
import dev.isxander.evergreenhud.api.impl.UEntity
import dev.isxander.evergreenhud.api.impl.UMinecraft
import net.minecraft.client.Minecraft
import net.minecraft.launchwrapper.Launch
import java.io.File

class MinecraftImpl : UMinecraft() {
    override val player: UEntity get() = EntityImpl(mc.thePlayer)
    override val dataDir: File = mc.mcDataDir
    override val fps: Int get() = Minecraft.getDebugFPS()
    override val inGameHasFocus: Boolean get() = mc.currentScreen != null
    override val devEnv by lazy {
        val o = Launch.blackboard["fml.deobfuscatedEnvironment"]
        o != null && o as Boolean
    }
}