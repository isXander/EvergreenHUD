/*
 *
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
 *
 */

package dev.isxander.evergreenhud.api.fabric11701.impl

import com.mojang.blaze3d.systems.RenderSystem
import dev.isxander.evergreenhud.api.fabric11701.mc
import dev.isxander.evergreenhud.api.impl.UMinecraftResource
import dev.isxander.evergreenhud.api.impl.UTextureManager
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.ByteBuffer
import javax.imageio.ImageIO

class TextureManagerImpl : UTextureManager() {
    override fun bindTexture(resource: UMinecraftResource) =
        RenderSystem.setShaderTexture(0, Identifier(resource.domain, resource.path))

    override fun getResource(resource: UMinecraftResource): InputStream =
        mc.resourceManager
            .getResource(Identifier(resource.domain, resource.path))
            .inputStream

    override fun registerDynamicTextureLocation(name: String, image: BufferedImage): UMinecraftResource {
        val nativeImage = NativeImage(image.width, image.height, false)
        for (x in 0 until nativeImage.width) {
            for (y in 0 until nativeImage.height) {
                nativeImage.setPixelColor(x, y, image.getRGB(x, y))
            }
        }

        return mc.textureManager
            .registerDynamicTexture(name, NativeImageBackedTexture(nativeImage))
            .let { UMinecraftResource(it.namespace, it.path) }
    }

    override fun deleteTexture(resource: UMinecraftResource) {
        mc.textureManager.destroyTexture(Identifier(resource.domain, resource.path))
    }
}
