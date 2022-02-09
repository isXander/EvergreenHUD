/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

plugins {
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("fabric-loom") version "0.10.+"
    id("com.google.devtools.ksp") version "$kotlinVersion-1.0.2"
    id("net.kyori.blossom") version "1.3.+"
    id("org.ajoberstar.grgit") version "4.1.+"
    `java-library`
    java
}

group = "dev.isxander"

val revision: String? = grgit.head()?.abbreviatedId
version = "2.0.0-pre13"

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://maven.fabricmc.net")
    maven(url = "https://repo.sk1er.club/repository/maven-public")
    maven(url = "https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    maven(url = "https://maven.terraformersmc.com/releases")
}

fun DependencyHandlerScope.includeApi(dep: Any) {
    api(dep)
    include(dep)
}

fun DependencyHandlerScope.includeModApi(dep: Any) {
    modApi(dep)
    include(dep)
}

dependencies {
    includeApi(project(":utils"))
    ksp(project(":processor"))

    includeApi("io.ktor:ktor-client-core:$ktorVersion")
    includeApi("io.ktor:ktor-client-apache:$ktorVersion")
    includeApi("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    includeApi("io.ktor:ktor-serialization:$ktorVersion")
    includeApi("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")

    includeApi("org.bundleproject:libversion:0.0.3")
    includeApi("dev.isxander:settxi:2.1.0")

    minecraft("com.mojang:minecraft:1.18.1")
    mappings("net.fabricmc:yarn:1.18.1+build.+:v2")
    modImplementation("net.fabricmc:fabric-loader:0.12.+")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.46.1+1.18")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.7.1+kotlin.$kotlinVersion")

    modImplementation("io.ejekta:kambrik:3.1.0-1.18")
    includeModApi("gg.essential:elementa-1.18-fabric:425")

    modImplementation("com.terraformersmc:modmenu:3.0.1")

    includeApi("com.github.LlamaLad7:MixinExtras:0.0.3")
    annotationProcessor("com.github.LlamaLad7:MixinExtras:0.0.3")
}

blossom {
    val evergreenClass = "src/main/kotlin/dev/isxander/evergreenhud/EvergreenHUD.kt"

    replaceToken("__GRADLE_NAME__", modName, evergreenClass)
    replaceToken("__GRADLE_ID__", modId, evergreenClass)
    replaceToken("__GRADLE_VERSION__", project.version, evergreenClass)
    replaceToken("__GRADLE_REVISION__", revision ?: "unknown", evergreenClass)
}

tasks {
    processResources {
        inputs.property("mod_id", modId)
        inputs.property("mod_name", modName)
        inputs.property("mod_version", project.version)

        filesMatching(listOf("fabric.mod.json", "bundle.project.json")) {
            expand(
                "mod_id" to modId,
                "mod_name" to modName,
                "mod_version" to project.version
            )
        }
    }
}

allprojects {
    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io")
    }

    tasks {
        withType<JavaCompile> {
            options.release.set(17)
        }

        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
}
