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

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.annotations.ElementMeta
import dev.isxander.evergreenhud.api.gl
import dev.isxander.evergreenhud.api.impl.UMinecraftResource
import dev.isxander.evergreenhud.api.textureManager
import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.elements.type.BackgroundElement
import dev.isxander.evergreenhud.settings.file
import dev.isxander.evergreenhud.utils.resetGlColor
import dev.isxander.evergreenhud.utils.resource
import dev.isxander.settxi.impl.boolean
import dev.isxander.settxi.impl.float
import java.awt.Color
import java.awt.Dimension
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.math.min

@ElementMeta(id = "IMAGE", name = "Image", category = "Miscallaneous", description = "Display an image file.")
class ElementImage : BackgroundElement() {
    var file by file(
        default = EvergreenHUD.dataDir,
        name = "File",
        category = "Image",
        description = "The file of the image."
    ) {
        set {
            changed = true
            it
        }
    }

    var autoScale by boolean(
        default = true,
        name = "Auto Scale",
        category = "Image",
        description = "Scale your image so it is the same size, no matter the resolution."
    )

    var rotation by float(
        default = 0f,
        min = 0f, max = 360f,
        name = "Rotation",
        category = "Image",
        description = "Rotate the image x amount of degrees."
    )

    var mirror by boolean(
        default = false,
        name = "Mirror",
        category = "Image",
        description = "Makes your image look as if you viewed it through a mirror."
    ) {
        set {
            changed = true
            it
        }
    }

    var changed = true
    var currentImage: UMinecraftResource? = null
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

    override fun render(deltaTicks: Float, renderOrigin: RenderOrigin) {
        if (changed || currentImage == null) {
            if (currentImage != null)
                textureManager.deleteTexture(currentImage!!)

            loadImage()

            changed = false
        }

        val hitbox = calculateHitBox(1f, position.scale)

        scaleMod =
            if (autoScale) imgSize / min(imageDimension.width, imageDimension.height).toFloat()
            else 1f

        gl.push()
        gl.push()
        val bgPivotX = hitbox.x + (hitbox.width / 2)
        val bgPivotY = hitbox.y + (hitbox.height / 2)
        gl.translate(bgPivotX, bgPivotY)
        gl.rotate(rotation, 0f, 0f, 1f)
        gl.translate(-bgPivotX, -bgPivotY)
        super.render(deltaTicks, renderOrigin)
        gl.pop()
        gl.scale(position.scale, position.scale)
        gl.enableDepth()
        resetGlColor()
        textureManager.bindTexture(currentImage!!)
        val width = imageDimension.width
        val height = imageDimension.height
        val renderWidth = width * scaleMod
        val renderHeight = height * scaleMod
        val renderX = position.rawX
        val renderY = position.rawY

        val imgPivotX = (renderX + (renderWidth / 2f)) / position.scale
        val imgPivotY = (renderY + (renderHeight / 2f)) / position.scale
        gl.translate(imgPivotX, imgPivotY)
        gl.rotate(rotation, 0f, 0f, 1f)
        if (mirror) gl.rotate(180f, 1f, 0f, 0f)
        gl.translate(-imgPivotX, -imgPivotY)

        gl.modalRect(
            renderX / position.scale, renderY / position.scale,
            0f, 0f,
            imageDimension.width.toFloat(), imageDimension.height.toFloat(),
            renderWidth, renderHeight,
            imageDimension.width.toFloat(), imageDimension.height.toFloat()
        )

        gl.pop()
    }

    fun loadImage() {
        val input =
            if (file.isDirectory || !file.exists()) unknownResource.inputStream
            else file.inputStream()

        val image = ImageIO.read(input)

        currentImage = textureManager.registerDynamicTextureLocation(textureName, image)
        imageDimension = Dimension(image.width, image.height)
    }
}
