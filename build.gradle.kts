/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options
import com.modrinth.minotaur.TaskModrinthUpload
import com.modrinth.minotaur.dependencies.ModDependency
import com.modrinth.minotaur.dependencies.DependencyType

plugins {
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("fabric-loom") version "0.11.+"
    id("io.github.juuxel.loom-quiltflower") version "1.6.+"
    id("com.google.devtools.ksp") version "$kotlinVersion-1.0.+"
    id("net.kyori.blossom") version "1.3.+"
    id("org.ajoberstar.grgit") version "5.0.+"
    id("com.matthewprenger.cursegradle") version "1.4.+"
    id("com.modrinth.minotaur") version "2.+"
    `java-library`
    java
    `maven-publish`
}

group = "dev.isxander"

val revision: String? = grgit.head()?.abbreviatedId
version = "2.0.0-alpha.4"

repositories {
    maven(url = "https://repo.woverflow.cc")
}

fun DependencyHandlerScope.includeApi(dep: Any) {
    api(dep)
    include(dep)
}

fun DependencyHandlerScope.includeModApi(dep: String, action: Action<ExternalModuleDependency> = Action<ExternalModuleDependency> {}) {
    include(modApi(dep, action))
}

val includeTransitive: Configuration by configurations.creating

fun DependencyHandlerScope.includeTransitive(
    dependencies: Set<ResolvedDependency>,
    fabricLanguageKotlinDependency: ResolvedDependency,
    checkedDependencies: MutableSet<ResolvedDependency> = HashSet()
) {
    dependencies.forEach {
        if (checkedDependencies.contains(it) || (it.moduleGroup == "org.jetbrains.kotlin" && it.moduleName.startsWith("kotlin-stdlib")))
            return@forEach

        if (fabricLanguageKotlinDependency.children.any { kotlinDep -> kotlinDep.name == it.name }) {
            println("Skipping -> ${it.name} (already in fabric-language-kotlin)")
        } else {
            include(it.name)
            println("Including -> ${it.name}")
        }
        checkedDependencies += it

        includeTransitive(it.children, fabricLanguageKotlinDependency, checkedDependencies)
    }
}

fun DependencyHandlerScope.handleIncludes(project: Project, configuration: Configuration) {
    includeTransitive(
        configuration.resolvedConfiguration.firstLevelModuleDependencies,
        project.configurations.getByName("modImplementation").resolvedConfiguration.firstLevelModuleDependencies
            .first { it.moduleGroup == "net.fabricmc" && it.moduleName == "fabric-language-kotlin" }
    )
}

dependencies {
    ksp(project(":processor"))

    includeApi("io.ktor:ktor-client-core:$ktorVersion")
    includeApi("io.ktor:ktor-client-apache:$ktorVersion")
    includeApi("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    includeApi("io.ktor:ktor-serialization:$ktorVersion")
    includeApi("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")

    includeApi("org.bundleproject:libversion:0.0.3")
    includeApi("dev.isxander:settxi:2.1.0")

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$minecraftVersion+build.+:v2")
    modImplementation("net.fabricmc:fabric-loader:0.13.+")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.47.8+1.18.2")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.7.1+kotlin.$kotlinVersion")

    includeTransitive(modApi("gg.essential:elementa-1.18-fabric:+") {
        exclude(module = "annotations")
        exclude(module = "kotlin-reflect")
        exclude(module = "fabric-loader")
    })

    modImplementation("com.terraformersmc:modmenu:3.0.+")

    includeApi("com.github.LlamaLad7:MixinExtras:0.0.+")
    annotationProcessor("com.github.LlamaLad7:MixinExtras:0.0.+")

    handleIncludes(project, includeTransitive)
}

blossom {
    val evergreenClass = "src/main/kotlin/dev/isxander/evergreenhud/EvergreenHUD.kt"

    replaceToken("__GRADLE_NAME__", modName, evergreenClass)
    replaceToken("__GRADLE_ID__", modId, evergreenClass)
    replaceToken("__GRADLE_VERSION__", project.version, evergreenClass)
    replaceToken("__GRADLE_REVISION__", revision ?: "unknown", evergreenClass)
}

tasks {
    remapJar {
        archiveVersion.set("${project.version}-$minecraftVersion" + (revision?.let { "-$it" } ?: ""))
    }
    remapSourcesJar {
        archiveClassifier.set("sources")
    }

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

    register("setupEvergreenHUD") {
        dependsOn("genSourcesWithQuiltflower")
    }

    register("releaseEvergreenHUD") {
        dependsOn("clean", "build")
        dependsOn("modrinth")
        dependsOn("curseforge")
        dependsOn("publish")
    }

    named("curseforge") {
        onlyIf { hasProperty("curseforge.token") }
    }
}

allprojects {
    repositories {
        maven(url = "https://repo.woverflow.cc")
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

modrinth {
    token.set(findProperty("modrinth.token").toString())
    projectId.set("1yIQcc2b")
    versionNumber.set(project.version.toString())
    versionType.set("alpha")
    uploadFile.set(tasks.remapJar.get())
    gameVersions.set(listOf(minecraftVersion))
    loaders.set(listOf("fabric"))
    dependencies.set(listOf(
        ModDependency("P7dR8mSH", DependencyType.REQUIRED), // fapi
        ModDependency("Ha28R6CL", DependencyType.REQUIRED), // flk
        ModDependency("mOgUt4GM", DependencyType.OPTIONAL), // mod menu
    ))
}

curseforge {
    apiKey = property("curseforge.token")
    project(closureOf<CurseProject> {
        mainArtifact(tasks.remapJar.get())

        id = "460419"
        releaseType = "alpha"
        addGameVersion(minecraftVersion)

        relations(closureOf<CurseRelation> {
            requiredDependency("fabric-api")
            requiredDependency("fabric-language-kotlin")
            optionalDependency("modmenu")
        })
    })

    options(closureOf<Options> {
        forgeGradleIntegration = false
    })
}

publishing {
    publications {
        register<MavenPublication>("evergreenhud") {
            groupId = "dev.isxander"
            artifactId = "evergreenhud"

            artifact(tasks.remapJar) {
                classifier = "fabric-$minecraftVersion"
            }
            artifact(tasks.remapSourcesJar) {
                classifier = "fabric-$minecraftVersion-sources"
            }
        }
    }

    repositories {
        if (hasProperty("woverflow.token")) {
            logger.log(LogLevel.INFO, "Publishing to W-OVERFLOW")
            maven(url = "https://repo.woverflow.cc/releases") {
                credentials {
                    username = "xander"
                    password = property("woverflow.token") as? String
                }
            }
        }
    }
}
