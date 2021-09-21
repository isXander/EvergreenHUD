package dev.isxander.evergreenhud.addons

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.api.loader
import dev.isxander.evergreenhud.api.logger
import dev.isxander.evergreenhud.api.mcVersion
import dev.isxander.evergreenhud.utils.MCVersion
import dev.isxander.evergreenhud.utils.jsonParser
import java.io.File
import java.util.jar.JarFile

class AddonLoader {
    private val entrypoints = mutableListOf<() -> Unit>()

    fun load() {
        for (file in ADDONS_FOLDER.walkTopDown()) {
            if (file.extension.lowercase() != "jar") continue

            val jar = JarFile(file)
            val addonInfo = jsonParser.parse(jar.getInputStream(jar.getJarEntry("evergreenhud.json")))
            val elements = jar.getInputStream(jar.getJarEntry("evergreenhud_elements.json"))
            jar.close()

            val id = addonInfo.get<String>("id")
            val supportedVersions = getList(addonInfo.get("minecraft_version")) ?: MCVersion.values().map { it.toString() }

            if (!supportedVersions.contains(mcVersion.toString())) {
                logger.info("Skipping unsupported addon: $id")
                continue
            }

            val entrypoints = getList<String>(addonInfo.get("entrypoints")) ?: listOf()

            loader.addURL(file.toURI().toURL())

            EvergreenHUD.elementManager.addSource(elements)

            for (entrypoint in entrypoints) {
                try {
                    val split = entrypoint.split("::")
                    val className = split[0]
                    val methodName = split[1].substringBefore('(')


                    val method = Class.forName(className)
                        .getDeclaredMethod(methodName)

                    this.entrypoints.add { method.invoke(null) }
                } catch (e: Exception) {
                    AddonException("Failed to invoke entrypoint: $entrypoint in $id.", e).printStackTrace()
                }
            }
        }
    }

    fun invokeEntrypoints() = entrypoints.forEach {
        try {
            it()
        } catch (e: Exception) {
            AddonException("Failed to invoke addon's entrypoint!", e).printStackTrace()
        }
    }

    private inline fun <reified T> getList(potentialList: Any): List<T>? {
        if (potentialList is List<*>) return potentialList as List<T>
        if (potentialList is T) return listOf(potentialList as T)
        return null
    }

    companion object {
        val ADDONS_FOLDER = File(EvergreenHUD.dataDir, "addons")
    }
}
