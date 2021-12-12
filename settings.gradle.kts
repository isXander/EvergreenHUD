/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()

        maven(url = "https://jitpack.io")
        maven(url = "https://maven.fabricmc.net")
    }

}

rootProject.name = "EvergreenHUD"

include("processor")
include("annotations")
