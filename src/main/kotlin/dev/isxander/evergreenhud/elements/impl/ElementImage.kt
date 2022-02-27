/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.elements.type.BackgroundElement
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.boolean
import dev.isxander.settxi.impl.file
import dev.isxander.settxi.impl.float
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import javax.imageio.ImageIO


@ElementMeta(id = "evergreenhud:image", name = "Image", category = "Miscallaneous", description = "Display an image file.")
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
    private var currentImage: ResourceLocation? = null
    var imageDimension = Dimension(64, 64)
    var scaleMod = 1f
    var imgSize = 64
    val textureName = "evergreen-image-element-${hashCode()}"
    private val unknownImage = ResourceLocation("evergreenhud", "textures/unknown.png")

    override val hitboxWidth: Float
        get() = imageDimension.width * scaleMod
    override val hitboxHeight: Float
        get() = imageDimension.height * scaleMod

    init {
        backgroundColor = Color.black.withAlpha(100)
        paddingLeft = 0f
        paddingRight = 0f
        paddingTop = 0f
        paddingBottom = 0f
    }

    override fun render(renderOrigin: RenderOrigin) {
        if (changed || currentImage == null) {
            // Reload the texture
            if (currentImage != null) mc.textureManager.deleteTexture(currentImage)
            try {
                cacheResourceLocation()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            changed = false
        }

        GlStateManager.pushMatrix()
        val scale = position.scale

        scaleMod = if (autoScale) {
            imgSize / imageDimension.getWidth().toFloat().coerceAtMost(imageDimension.getHeight().toFloat())
        } else {
            1f
        }
        GlStateManager.pushMatrix()
        val hitbox = calculateHitBox(position.scale)
        val bgPivotX: Float = hitbox.x1 + hitbox.width / 2f
        val bgPivotY: Float = hitbox.x1 + hitbox.height / 2f
        GlStateManager.translate(bgPivotX, bgPivotY, 0f)
        GlStateManager.rotate(rotation, 0f, 0f, 1f)
        GlStateManager.translate(-bgPivotX, -bgPivotY, 0f)
        super.render(renderOrigin)
        GlStateManager.popMatrix()
        GlStateManager.scale(scale, scale, 1f)
        GlStateManager.enableDepth()
        GlStateManager.color(1f, 1f, 1f, 1f)
        mc.textureManager.bindTexture(currentImage)
        val width = imageDimension.getWidth()
        val height = imageDimension.getHeight()
        val renderWidth = width * scaleMod
        val renderHeight = height * scaleMod
        val renderX = position.rawX
        val renderY = position.rawY

        val imgPivotX = (renderX + renderWidth.toFloat() / 2f) / scale
        val imgPivotY = (renderY + renderHeight.toFloat() / 2f) / scale
        GlStateManager.translate(imgPivotX, imgPivotY, 0f)
        GlStateManager.rotate(rotation, 0f, 0f, 1f)
        GlStateManager.translate(-imgPivotX, -imgPivotY, 0f)

        drawModalRect(
            renderX / scale,
            renderY / scale,
            0,
            0,
            imageDimension.getWidth(),
            imageDimension.getHeight(),
            renderWidth,
            renderHeight,
            imageDimension.getWidth(),
            imageDimension.getHeight()
        )

        GlStateManager.popMatrix()
    }

    @Throws(IOException::class)
    private fun cacheResourceLocation() {
        val imgFile: File? = getImageFile()
        val `in`: InputStream
        if (imgFile == null) {
            `in` = mc.resourceManager.getResource(unknownImage).inputStream
        } else {
            `in` = FileInputStream(imgFile)
        }
        var img: BufferedImage = ImageIO.read(`in`)
        if (mirror) img = mirror(img)
        currentImage = mc.textureManager.getDynamicTextureLocation(textureName, DynamicTexture(img))
        imageDimension = Dimension(img.width, img.height)
    }

    private fun getImageFile(): File? {
        return if (file.name == "" || file.exists()) {
            null
        } else file
    }

    private fun mirror(image: BufferedImage): BufferedImage {
        val newImage = BufferedImage(image.width, image.height, image.type)
        for (y in 0 until newImage.height) {
            var x1 = 0
            var x2 = newImage.width - 1
            while (x1 < newImage.width) {
                newImage.setRGB(x1, y, image.getRGB(x2, y))
                x1++
                x2--
            }
        }
        return newImage
    }

    fun drawModalRect(
        x: Float,
        y: Float,
        u: Int,
        v: Int,
        uWidth: Double,
        vHeight: Double,
        width: Double,
        height: Double,
        tileWidth: Double,
        tileHeight: Double
    ) {
        val f = 1.0 / tileWidth
        val f1 = 1.0 / tileHeight
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX)
        worldrenderer.pos(x.toDouble(), y + height, 0.0).tex(u * f, (v + vHeight) * f1).endVertex()
        worldrenderer.pos(x + width, y + height, 0.0).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex()
        worldrenderer.pos(x + width, y.toDouble(), 0.0).tex((u + uWidth) * f, v * f1).endVertex()
        worldrenderer.pos(x.toDouble(), y.toDouble(), 0.0).tex(u * f, v * f1).endVertex()
        tessellator.draw()
    }
}
