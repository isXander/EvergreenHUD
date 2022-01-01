/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.elements.impl

import com.mojang.blaze3d.systems.RenderSystem
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.elements.type.BackgroundElement
import dev.isxander.evergreenhud.utils.*
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.settxi.impl.boolean
import dev.isxander.settxi.impl.file
import dev.isxander.settxi.impl.float
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Quaternion
import net.minecraft.util.math.Vec3f
import java.awt.Color
import java.awt.Dimension
import javax.imageio.ImageIO
import kotlin.math.min

@ElementMeta(id = "IMAGE", name = "Image", category = "Miscallaneous", description = "Display an image file.")
class ElementImage : BackgroundElement() {
    var file by file(EvergreenHUD.dataDir) {
        name = "File"
        category = "Image"
        description = "The file of the image."

        set {
            changed = true
            it
        }
    }

    var autoScale by boolean(true) {
        name = "Auto Scale"
        category = "Image"
        description = "Scale your image so it is the same size, no matter the resolution."
    }

    var rotation by float(0f) {
        range = 0f..360f
        name = "Rotation"
        category = "Image"
        description = "Rotate the image x amount of degrees."
    }

    var mirror by boolean(false) {
        name = "Mirror"
        category = "Image"
        description = "Makes your image look as if you viewed it through a mirror."

        set {
            changed = true
            it
        }
    }

    var changed = true
    var currentImage: Identifier? = null
    lateinit var imageDimension: Dimension
    var scaleMod = 1f
    var imgSize = 64
    val textureName = "evergreen-image-element-${hashCode()}"
    val unknownResource = resource("unknown.png")

    override val hitboxWidth: Float
        get() = imageDimension.width * scaleMod
    override val hitboxHeight: Float
        get() = imageDimension.height * scaleMod

    init {
        backgroundColor = Color(0, 0, 0, 0)
    }

    override fun render(matrices: MatrixStack, renderOrigin: RenderOrigin) {
        if (changed || currentImage == null) {
            if (currentImage != null)
                mc.textureManager.destroyTexture(currentImage!!)

            loadImage()

            changed = false
        }

        val hitbox = calculateHitBox(1f, position.scale)

        scaleMod =
            if (autoScale) imgSize / min(imageDimension.width, imageDimension.height).toFloat()
            else 1f

        matrices.push()
        matrices.push()
        val bgPivotX = hitbox.x1 + (hitbox.width / 2)
        val bgPivotY = hitbox.y1 + (hitbox.height / 2)
        matrices.translate(bgPivotX, bgPivotY)
        matrices.multiply(Quaternion(Vec3f(0f, 0f, 1f), rotation, true))
        matrices.translate(-bgPivotX, -bgPivotY)
        super.render(matrices, renderOrigin)
        matrices.pop()
        matrices.scale(position.scale, position.scale, 1f)
        RenderSystem.enableDepthTest()
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, currentImage!!)
        val width = imageDimension.width
        val height = imageDimension.height
        val renderWidth = width * scaleMod
        val renderHeight = height * scaleMod
        val renderX = position.rawX
        val renderY = position.rawY

        val imgPivotX = (renderX + (renderWidth / 2f)) / position.scale
        val imgPivotY = (renderY + (renderHeight / 2f)) / position.scale
        matrices.translate(imgPivotX, imgPivotY)
        matrices.multiply(Quaternion(Vec3f(0f, 0f, 1f), rotation, true))
        if (mirror) matrices.multiply(Quaternion(Vec3f(1f, 0f, 0f), 180f, true))
        matrices.translate(-imgPivotX, -imgPivotY)

        matrices.drawTexture(
            renderX / position.scale, renderY / position.scale,
            0f, 0f,
            renderWidth, renderHeight,
            imageDimension.width.toFloat(), imageDimension.height.toFloat(),
        )

        matrices.pop()
    }

    fun loadImage() {
        val input =
            if (file.isDirectory || !file.exists()) mc.resourceManager.getResource(unknownResource).inputStream
            else file.inputStream()

        val image = ImageIO.read(input)

        currentImage = mc.textureManager.registerDynamicTexture(textureName, NativeImageBackedTexture(image.toNativeImage()))
        imageDimension = Dimension(image.width, image.height)
    }
}
