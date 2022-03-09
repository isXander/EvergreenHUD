/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

pluginManagement {
    repositories {
        maven(url = "https://repo.woverflow.cc/")
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "gg.essential.loom") {
                useModule("gg.essential:architectury-loom:${requested.version}")
            }      
    }
}

rootProject.name = "EvergreenHUD"
include(":processor")
