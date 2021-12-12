/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.utils

import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.client.texture.TextureManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import java.awt.image.BufferedImage

fun MatrixStack.translate(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0) =
    translate(x.toDouble(), y.toDouble(), z.toDouble())

fun BufferedImage.toNativeImage(): NativeImage {
    val nativeImage = NativeImage(width, height, false)
    for (x in 0 until nativeImage.width) {
        for (y in 0 until nativeImage.height) {
            nativeImage.setColor(x, y, getRGB(x, y))
        }
    }

    return nativeImage
}
