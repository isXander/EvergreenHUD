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

package dev.isxander.evergreenhud.api.fabric11701.impl

import dev.isxander.evergreenhud.api.impl.UEntity
import dev.isxander.evergreenhud.api.impl.UMinecraft
import dev.isxander.evergreenhud.api.fabric11701.mc
import dev.isxander.evergreenhud.api.fabric11701.mixins.AccessorMinecraftClient
import dev.isxander.evergreenhud.api.impl.UMinecraftResource
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream
import javax.imageio.ImageIO
import javax.imageio.stream.ImageInputStream

class MinecraftImpl : UMinecraft() {
    override val player: UEntity get() = EntityImpl(mc.player)
    override val dataDir: File = mc.runDirectory
    override val fps: Int get() = AccessorMinecraftClient.getFps()
    override val inGameHasFocus: Boolean
        get() = mc.mouse.isCursorLocked
    override val devEnv: Boolean = FabricLoader.getInstance().isDevelopmentEnvironment
    override val inChatMenu: Boolean
        get() = mc.currentScreen is ChatScreen
    override val inDebugMenu: Boolean
        get() = mc.options.debugEnabled
}
