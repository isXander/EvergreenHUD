/*
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
 */

package dev.isxander.evergreenhud.utils

enum class MCVersion(val display: String, val number: Int, val platform: Platform) {
    FORGE_1_8_9("Forge 1.8.9", 10809, Platform.Forge),
    FABRIC_1_17_1("Fabric 1.17.1", 11701, Platform.Fabric),
}

enum class Platform {
    Forge,
    Fabric,
}