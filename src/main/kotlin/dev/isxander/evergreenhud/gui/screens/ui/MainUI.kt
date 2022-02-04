/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.gui.screens.ui

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.gui.screens.ui.components.CloseButton
import dev.isxander.evergreenhud.gui.screens.ui.components.SearchField
import dev.isxander.evergreenhud.utils.*
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.Formatting

abstract class MainUI(val parent: Screen? = null) : WindowScreen(ElementaVersion.V1) {
    init {
        UIBlock(EvergreenPalette.Greyscale.Dark2.constraint).constrain {
            width = 100.percent()
            height = 100.percent()
        } childOf window

        Inspector(window) childOf window
    }

    val header by UIBlock(EvergreenPalette.Greyscale.Dark1.constraint).constrain {
        width = 100.percent()
        height = 20.percent()
    } childOf window

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

    val profileOutline = OutlineEffect(Color(0.85f, 0.85f, 0.85f, 0f).awt, 0.5f)
    val profileIcon by UIImage.ofBase64(EvergreenHUD.profileManager.currentProfile.icon).constrain {
        x = SiblingConstraint(15f)
        y = CenterConstraint()
        width = ImageAspectConstraint()
        height = 55.percent()
    }.onMouseEnter {
        profileOutline::color.animate(Animations.OUT_CUBIC, 0.3f, Color(0.85f, 0.85f, 0.85f, 0.5f).awt)
    }.onMouseLeave {
        profileOutline::color.animate(Animations.OUT_CUBIC, 0.3f, Color(0.85f, 0.85f, 0.85f, 0f).awt)
    } effect profileOutline childOf upperHeader

    val optionsButton by UIImage.ofIdentifier(resource("ui/options.png")).constrain {
        x = SiblingConstraint(5f)
        y = CenterConstraint()
        width = ImageAspectConstraint()
        height = 55.percent()
    }.onMouseClick {

    } childOf upperHeader

    val rightHeader by UIContainer().constrain {
        x = 0.pixels(alignOpposite = true)
        width = ChildBasedMaxSizeConstraint()
        height = 100.percent()
    } childOf upperHeader

    val closeButton by CloseButton().constrain {
        y = CenterConstraint()
        width = 2.5.percent() boundTo paddedHeader
        height = AspectConstraint()
    }.onMouseClick {
        mc.setScreen(this@MainUI.parent)
    } childOf rightHeader

    val lowerHeader by UIContainer().constrain {
        y = 50.percent()
        width = 100.percent()
        height = 50.percent()
    } childOf paddedHeader



    val mainContent by UIBlock(EvergreenPalette.Greyscale.Dark2.constraint).constrain {
        y = 20.percent()
        width = 100.percent()
        height = 80.percent()
    } childOf window
}
