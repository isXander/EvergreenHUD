/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import gg.essential.elementa.components.UIImage
import gg.essential.elementa.constraints.ColorConstraint
import gg.essential.elementa.dsl.toConstraint
import net.minecraft.util.Identifier
import java.io.ByteArrayInputStream
import java.util.*
import java.util.concurrent.CompletableFuture
import javax.imageio.ImageIO

fun UIImage.Companion.ofIdentifier(identifier: Identifier): UIImage {
    return UIImage(CompletableFuture.supplyAsync {
        ImageIO.read(mc.resourceManager.getResource(identifier).inputStream)
    })
}

fun UIImage.Companion.ofBase64(base64: String): UIImage {
    return UIImage(CompletableFuture.supplyAsync {
        ImageIO.read(fromBase64(base64))
    })
}

val Color.constraint: ColorConstraint
    get() = java.awt.Color(this.rgba).toConstraint()
