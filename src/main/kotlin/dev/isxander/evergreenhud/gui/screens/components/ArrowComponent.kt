/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.gui.screens.components

import gg.essential.elementa.components.UIImage
import gg.essential.elementa.dsl.effect
import java.util.concurrent.CompletableFuture
import javax.imageio.ImageIO

class ArrowComponent : UIImage(
    CompletableFuture.supplyAsync {
        ImageIO.read(this::class.java.getResourceAsStream("/assets/evergreenhud/textures/arrow.png"))
    }
)
