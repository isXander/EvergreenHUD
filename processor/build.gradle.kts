/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

plugins {
    `maven-publish`
}

apply(plugin = "org.jetbrains.kotlin.jvm")
apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

version = rootProject.version

val implementation by configurations

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:$kotlinVersion-1.0.+")
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
}


publishing {
    publications {
        register<MavenPublication>("evergreenhudProcessor") {
            groupId = "dev.isxander"
            artifactId = "evergreenhud-ap"

            artifact(tasks["jar"]) {
                classifier = "fabric-$minecraftVersion"
            }
        }
    }

    repositories {
        if (hasProperty("WOVERFLOW_REPO_PASS")) {
            logger.log(LogLevel.INFO, "Publishing to W-OVERFLOW")
            maven(url = "https://repo.woverflow.cc/releases") {
                credentials {
                    username = "wyvest"
                    password = property("WOVERFLOW_REPO_PASS") as? String
                }
            }
        }
    }
}
