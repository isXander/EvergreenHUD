/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui.components

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.fromBase64
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.constraints.ImageAspectConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.effect
import gg.essential.elementa.effects.OutlineEffect
import java.util.concurrent.CompletableFuture
import javax.imageio.ImageIO

class ProfileComponent : UIImage(CompletableFuture.supplyAsync { ImageIO.read(fromBase64(EvergreenHUD.profileManager.currentProfile.icon)) }) {
    val profileOutline = OutlineEffect(Color(0.85f, 0.85f, 0.85f, 0f).awt, 0.5f)

    init {
        constrain {
            width = ImageAspectConstraint()
            height = ImageAspectConstraint()
        } effect profileOutline

        onMouseEnter {
            profileOutline::color.animate(Animations.OUT_CUBIC, 0.3f, Color(0.85f, 0.85f, 0.85f, 0.5f).awt)
        }
        onMouseLeave {
            profileOutline::color.animate(Animations.OUT_CUBIC, 0.3f, Color(0.85f, 0.85f, 0.85f, 0f).awt)
        }
    }
}
