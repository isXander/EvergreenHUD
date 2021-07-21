/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/gpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.compatibility.fabric11701.impl

import dev.isxander.evergreenhud.compatibility.fabric11701.mc
import dev.isxander.evergreenhud.compatibility.universal.impl.AIResolution

class ResolutionImpl : AIResolution() {

    override val displayWidth: Int get() = mc.window.width
    override val displayHeight: Int get() = mc.window.height

    override val scaledWidth: Int get() = mc.window.scaledWidth
    override val scaledHeight: Int get() = mc.window.scaledHeight

    override val scaleFactor: Double get() = mc.window.scaleFactor

}