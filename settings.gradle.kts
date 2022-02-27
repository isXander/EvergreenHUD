/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()

        maven(url = "https://repo.woverflow.cc/")
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "net.minecraftforge.gradle.forge" -> useModule("com.github.asbyth:ForgeGradle:${requested.version}")
                "org.spongepowered.mixin" -> useModule("com.github.Skytils:mixingradle:${requested.version}")
            }
        }
    }
}

rootProject.name = "EvergreenHUD"

include("processor")
