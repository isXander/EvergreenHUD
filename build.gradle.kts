/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

//file:noinspection UnnecessaryQualifiedReference
//file:noinspection GroovyAssignabilityCheck

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecraftforge.gradle.user.IReobfuscator
import net.minecraftforge.gradle.user.ReobfMappingType.SEARGE
import net.minecraftforge.gradle.user.TaskSingleReobf
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("cc.woverflow.loom") version "0.10.3"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.google.devtools.ksp") version "$kotlinVersion-1.0.2"
    id("net.kyori.blossom") version "1.3.+"
    id("org.ajoberstar.grgit") version "4.1.+"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    `java-library`
    java
    `maven-publish`
}

group = "dev.isxander"

val revision: String? = grgit.head()?.abbreviatedId
version = "2.0.0-alpha.4"

loom {
    launchConfigs {
        client {
            arg("--tweakClass", "cc.woverflow.onecore.tweaker.OneCoreTweaker")
            property("onecore.mixin", "mixins.${mod_id}.json")
        }
    }
    runConfigs {
        client {
            ideConfigGenerated = true
        }
    }
    forge {
        pack200Provider = new dev.architectury.pack200.java.Pack200Adapter()
        mixinConfig("mixins.${mod_id}.json")
        mixin.defaultRefmapName.set("mixins.${mod_id}.refmap.json")
    }
}

repositories {
    maven(url = "https://repo.woverflow.cc/")
}

val include: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

fun DependencyHandlerScope.includeApi(dep: Any) {
    api(dep)
    include(dep)
}

fun DependencyHandlerScope.compileMainAnnotationProcessor(dep: Any) {
    compileOnly(dep)
    annotationProcessor(dep)
}

dependencies {
    ksp(project(":processor"))

    includeApi("io.ktor:ktor-client-core:$ktorVersion")
    includeApi("io.ktor:ktor-client-apache:$ktorVersion")
    includeApi("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    includeApi("io.ktor:ktor-serialization:$ktorVersion")
    includeApi("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    includeApi("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")

    includeApi("org.bundleproject:libversion:0.0.3")
    includeApi("dev.isxander:settxi:2.1.0")
    
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    compileOnly 'gg.essential:essential-1.8.9-forge:1933'
    compileOnly 'cc.woverflow:onecore:1.3.3'
    include ('cc.woverflow:onecore-tweaker:1.3.0') {
        transitive = false
    }
    
    compileOnly ('org.spongepowered:mixin:0.8.5-SNAPSHOT')
    annotationProcessor("com.google.code.gson:gson:2.2.4")
    annotationProcessor("com.google.guava:guava:21.0")
    annotationProcessor("org.ow2.asm:asm-tree:6.2")
}

mixin {
    disableRefMapWarning = true
    defaultObfuscationEnv = searge
    add(sourceSets.main.get(), "mixins.evergreenhud.refmap.json")
}

blossom {
    val evergreenClass = "src/main/kotlin/dev/isxander/evergreenhud/EvergreenHUD.kt"

    replaceToken("__GRADLE_NAME__", modName, evergreenClass)
    replaceToken("__GRADLE_ID__", modId, evergreenClass)
    replaceToken("__GRADLE_VERSION__", project.version, evergreenClass)
    replaceToken("__GRADLE_REVISION__", revision ?: "unknown", evergreenClass)
}

sourceSets {
    main {
        ext["refmap"] = "mixins.evergreenhud.refmap.json"
        output.setResourcesDir(file("${buildDir}/classes/kotlin/main"))
    }
}

configure<NamedDomainObjectContainer<IReobfuscator>> {
    clear()
    create("shadowJar") {
        mappingType = SEARGE
        classpath = sourceSets.main.get().compileClasspath
    }
}

tasks {
    processResources {
        inputs.property("mod_id", modId)
        inputs.property("mod_name", modName)
        inputs.property("mod_version", project.version)

        filesMatching(listOf("mcmod.info", "bundle.project.json")) {
            expand(
                "mod_id" to modId,
                "mod_name" to modName,
                "mod_version" to project.version
            )
        }
    }
    named<Jar>("jar") {
        archiveBaseName.set("EvergreenHUD")
        manifest {
            attributes(
                mapOf(
                    "FMLCorePlugin" to "cc.woverflow.onecore.tweaker.OneCoreTweaker",
                    "FMLCorePluginContainsFMLMod" to true,
                    "ForceLoadAsMod" to true,
                    "MixinConfigs" to "mixins.evergreenhud.json",
                    "ModSide" to "CLIENT",
                    "TweakOrder" to "0"
                )
            )
        }
        enabled = false
    }
    named<ShadowJar>("shadowJar") {
        archiveFileName.set(jar.get().archiveFileName)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(include)

        exclude(
            "**/LICENSE.md",
            "**/LICENSE.txt",
            "**/LICENSE",
            "**/NOTICE",
            "**/NOTICE.txt",
            "pack.mcmeta",
            "dummyThing",
            "**/module-info.class",
            "META-INF/proguard/**",
            "META-INF/maven/**",
            "META-INF/versions/**",
            "META-INF/com.android.tools/**",
            "fabric.mod.json"
        )
        mergeServiceFiles()
    }
    named<TaskSingleReobf>("reobfJar") {
        enabled = false
    }
    named<TaskSingleReobf>("reobfShadowJar") {
        mustRunAfter(shadowJar)
    }
    register<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }
    artifacts {
        archives(this@tasks["sourcesJar"])
    }
}

allprojects {
    repositories {
        maven(url = "https://repo.woverflow.cc/")
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
            }
            kotlinDaemonJvmArguments.set(listOf("-Xmx2G", "-Dkotlin.enableCacheBuilding=true", "-Dkotlin.useParallelTasks=true", "-Dkotlin.enableFastIncremental=true"))
        }
    }
}
java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

publishing {
    publications {
        register<MavenPublication>("evergreenhud") {
            groupId = "dev.isxander"
            artifactId = "evergreenhud"

            // WYVEST: when changing to architectury, make sure to set these to remapJar and remapSourcesJar
            artifact(tasks.shadowJar) {
                classifier = "forge-1.8.9"
            }
            artifact(tasks["sourcesJar"]) {
                classifier = "forge-1.8.9-sources"
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
