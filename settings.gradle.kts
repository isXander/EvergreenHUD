pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()

        maven("https://maven.minecraftforge.net") { name = "Forge" }
        maven("https://jitpack.io") { name = "JitPack" }
        maven("https://repo.spongepowered.org/maven/") { name = "SpongePowered" }
    }

}

rootProject.name = "EvergreenHUD"

include("core")

listOf(
    "forge-1.8.9",
    "fabric-1.17.1"
).forEach { version ->
    include(":$version")
}

