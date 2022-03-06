/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui

import dev.isxander.evergreenhud.ui.components.CategoryLabel
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.provideDelegate

class ProfilesUI : MainUI() {
    val buttonList by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = ChildBasedSizeConstraint()
        height = 25.percent()
    } childOf lowerHeader

    val addProfileButton by CategoryLabel("Create Profile", false).constrain {
        x = SiblingConstraint(20f)
        y = CenterConstraint()
        width = TextAspectConstraint()
        height = 100.percent()
    }.onMouseClick {

    } childOf buttonList


    val scrollContainer by UIContainer().constrain {
        width = 100.percent()
        height = FillConstraint()
    } childOf paddedMainContent

    val settingScroller by ScrollComponent(pixelsPerScroll = 25f).constrain {
        width = 100.percent()
        height = 100.percent()
    } childOf scrollContainer

    val settingsScrollbarContainer by UIBlock(EvergreenPalette.Greyscale.Dark3.awt).constrain {
        x = 98.percent()
        width = 2.percent()
    } childOf scrollContainer

    val settingScrollbar by UIBlock(EvergreenPalette.Greyscale.Gray1.awt).constrain {
        width = 100.percent()
    } childOf settingsScrollbarContainer

    init {
        settingScroller.setVerticalScrollBarComponent(settingScrollbar)
    }


}
