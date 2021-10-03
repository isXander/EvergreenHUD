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

package dev.isxander.evergreenhud.api.forge10809.impl

import dev.isxander.evergreenhud.api.forge10809.mc
import dev.isxander.evergreenhud.api.impl.UMinecraftResource
import dev.isxander.evergreenhud.api.impl.UTextureManager
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.util.ResourceLocation
import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO

class TextureManagerImpl : UTextureManager() {
    override fun bindTexture(resource: UMinecraftResource) =
        mc.textureManager.bindTexture(ResourceLocation(location.domain, location.path))

    override fun getResource(resource: UMinecraftResource): InputStream =
        mc.resourceManager
            .getResource(ResourceLocation(resource.domain, resource.path))
            .inputStream

    override fun registerDynamicTextureLocation(name: String, image: BufferedImage): UMinecraftResource =
        mc.textureManager
            .getDynamicTextureLocation(name, DynamicTexture(image))
            .let { UMinecraftResource(it.resourceDomain, it.resourceDomain) }

    override fun deleteTexture(resource: UMinecraftResource) {
        mc.textureManager.deleteTexture(ResourceLocation(resource.domain, resource.path))
    }
}
