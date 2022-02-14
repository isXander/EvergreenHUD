/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.ui.components.CloseButton
import dev.isxander.evergreenhud.ui.components.ElementComponent
import dev.isxander.evergreenhud.ui.components.ProfileComponent
import dev.isxander.evergreenhud.ui.components.SearchField
import dev.isxander.evergreenhud.utils.*
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.*
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.effects.ScissorEffect
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.Formatting

abstract class MainUI : UIBlock(EvergreenPalette.Greyscale.Dark2.constraint) {
    init {
        constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = 75.percent()
            height = 75.percent()
        }
    }

    val header by UIBlock(EvergreenPalette.Greyscale.Dark1.constraint).constrain {
        width = 100.percent()
        height = 20.percent()
    } childOf this

    val paddedHeader by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = (100 - 2.5).percent()
        height = (100 - 20).percent()
    } childOf header

    val upperHeader by UIContainer().constrain {
        width = 100.percent()
        height = 50.percent()
    } childOf paddedHeader

    val leftHeader by UIContainer().constrain {
        y = CenterConstraint()
        width = ChildBasedMaxSizeConstraint()
        height = 100.percent()
    } childOf upperHeader

    val icon by UIImage.ofIdentifier(resource("ui/evergreenhud-circular.png")).constrain {
        width = ImageAspectConstraint()
        height = 100.percent()
    } childOf leftHeader

    val evergreenHudText by UIText(Formatting.BOLD + "EvergreenHUD", false).constrain {
        width = 15.percent() boundTo upperHeader
        height = TextAspectConstraint()
        x = SiblingConstraint(8f)
        y = CenterConstraint()
    } childOf leftHeader

    val searchField by SearchField().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 25.percent()
        height = 50.percent()
    } childOf upperHeader

    val profileIcon by ProfileComponent().constrain {
        x = SiblingConstraint(15f)
        y = CenterConstraint()
        height = 55.percent()
    } childOf upperHeader

    val optionsButton by UIImage.ofIdentifier(resource("ui/options.png")).constrain {
        x = SiblingConstraint(5f)
        y = CenterConstraint()
        width = ImageAspectConstraint()
        height = 55.percent()
    }.onMouseClick {
        this@MainUI.hide()
        val elementManagerSettings by ConfigUI(EvergreenHUD.elementManager)
        elementManagerSettings childOf this@MainUI.parent
    } childOf upperHeader

    val rightHeader by UIContainer().constrain {
        x = 0.pixels(alignOpposite = true)
        width = ChildBasedMaxSizeConstraint()
        height = 100.percent()
    } childOf upperHeader

    val closeButtonOutline = OutlineEffect(Color.white.withAlpha(0).awt, 1f, drawInsideChildren = true)
    val closeButton by CloseButton().constrain {
        y = CenterConstraint()
        width = 2.5.percent() boundTo paddedHeader
        height = AspectConstraint()
    }.onMouseClick {
        this@MainUI.hide(true)
    }.onMouseEnter {
        closeButtonOutline::color.animate(Animations.IN_OUT_SIN, 0.3f, Color.white.awt)
    }.onMouseLeave {
        closeButtonOutline::color.animate(Animations.IN_OUT_SIN, 0.3f, Color.white.withAlpha(0).awt)
    } effect closeButtonOutline childOf rightHeader

    val lowerHeader by UIContainer().constrain {
        y = 50.percent()
        width = 100.percent()
        height = 50.percent()
    } childOf paddedHeader

    val mainContent by UIBlock(EvergreenPalette.Greyscale.Dark2.constraint).constrain {
        y = 20.percent()
        width = 100.percent()
        height = 80.percent()
    } childOf this

    val paddedMainContent by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = (100 - 5).percent()
        height = (100 - 4).percent()
    } effect ScissorEffect() childOf mainContent
}
