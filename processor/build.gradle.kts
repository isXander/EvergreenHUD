/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

apply(plugin = "org.jetbrains.kotlin.jvm")
apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

val implementation by configurations

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.0-1.0.2")
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":annotations"))
    implementation("com.electronwill.night-config:core:3.6.5")
    implementation("com.electronwill.night-config:json:3.6.5")
}
