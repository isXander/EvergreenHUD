/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.ui.components.ElementComponent
import dev.isxander.evergreenhud.ui.components.ProfileComponent
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.evergreenhud.utils.ofIdentifier
import dev.isxander.evergreenhud.utils.resource
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
import net.minecraft.util.math.Vec2f

class ElementDisplay(val parentScreen: Screen? = null) : WindowScreen(ElementaVersion.V1) {
    val inspector by Inspector(window) childOf window
    val elements = mutableListOf<ElementComponent>()

    val globalSnapPoints = listOf(
        Vec2f(0f, 0f),
        Vec2f(0f, 1f),
        Vec2f(0.5f, 0f),
        Vec2f(0.5f, 1f),
        Vec2f(1f, 0f),
        Vec2f(1f, 1f),
    )

    init {
        for (element in EvergreenHUD.elementManager) {
            val component by ElementComponent(element, this)

            component.settingsButton.onMouseClick {
                val configUI by ConfigUI(component.element)
                configUI childOf window
            }

            elements += component

            component childOf window
        }
    }

    val miniMenu by UIBlock(EvergreenPalette.Greyscale.Dark1.awt).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 25.percent()
        height = 25.percent()
    } effect OutlineEffect(Color.white.awt, 1f) childOf window

    val header by UIContainer().constrain {
        width = 100.percent()
        height = 50.percent()
    } childOf miniMenu

    val paddedHeader by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 95.percent()
        height = 98.percent()
    } childOf header

    val logo by UIImage.ofIdentifier(resource("ui/evergreenhud-circular.png")).constrain {
        y = CenterConstraint()
        width = ImageAspectConstraint()
        height = 75.percent()
    } childOf paddedHeader

    val rightHeader by UIContainer().constrain {
        x = 0.pixels(alignOpposite = true)
        width = ChildBasedSizeConstraint()
        height = 100.percent()
    } childOf paddedHeader

    val profileIcon by ProfileComponent().constrain {
        y = CenterConstraint()
        height = 40.percent()
    }.onMouseClick {
        val profilesUI by ProfilesUI()
        profilesUI childOf window
    } childOf rightHeader

    val optionsButton by UIImage.ofIdentifier(resource("ui/options.png")).constrain {
        x = SiblingConstraint(5f)
        y = CenterConstraint()
        width = ImageAspectConstraint()
        height = 40.percent()
    }.onMouseClick {
        val elementManagerSettings by ConfigUI(EvergreenHUD.elementManager)
        elementManagerSettings childOf window
    }  childOf rightHeader

    val mainContent by UIContainer().constrain {
        y = 50.percent()
        width = 100.percent()
        height = 50.percent()
    } childOf miniMenu

    val paddedMainContent by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 98.percent()
        height = 98.percent()
    } childOf mainContent

    val addElementOutline = OutlineEffect(Color.white.withAlpha(0).awt, 1f, drawInsideChildren = true)

    val addElementBg by UIBlock(EvergreenPalette.Greyscale.Dark2.awt).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = ChildBasedMaxSizeConstraint() + 10.percent()
        height = 50.percent()
    }.onMouseClick {
        val addElementUI by AddElementUI()
        addElementUI childOf window
    }.onMouseEnter {
        addElementOutline::color.animate(Animations.IN_OUT_SIN, 0.3f, Color.white.awt)
    }.onMouseLeave {
        addElementOutline::color.animate(Animations.IN_OUT_SIN, 0.3f, Color.white.withAlpha(0).awt)
    } effect addElementOutline childOf paddedMainContent

    val addElementText by UIText("Add Element", false).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = TextAspectConstraint()
        height = 50.percent()
    } childOf addElementBg

    override fun onScreenClose() {
        EvergreenHUD.elementManager.elementConfig.save()

        super.onScreenClose()
    }

    override fun close() {
        mc.setScreen(parentScreen)
    }
}
