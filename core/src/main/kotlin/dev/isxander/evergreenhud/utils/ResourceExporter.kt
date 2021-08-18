package dev.isxander.evergreenhud.utils

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.ConfigSpec
import com.uchuhimo.konf.source.toml
import com.uchuhimo.konf.source.toml.toToml
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.EvergreenInfo
import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import dev.isxander.evergreenhud.compatibility.universal.MC
import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * Exports all resources from the JAR into a folder.
 * Used to allow users to select default resources for
 * profile icons etc and easily access resources from
 * Elementa.
 *
 * @author isXander
 */
fun exportResources() {
    // every 3 days export resources
    val metadataFile = File(EvergreenHUD.dataDir, "resources/metadata.toml")
    val metadata = Config { addSpec(ResourceSpec) }
        .from.toml.file(metadataFile, optional = true)

    if (MC.devEnv || !metadataFile.exists() || (metadata.getOrNull(ResourceSpec.version) ?: "1.0") != EvergreenInfo.VERSION_FULL.toString()) {
        LOGGER.info("Exporting resources...")

        val reflections = Reflections("evergreenhud.export", ResourcesScanner())
        EvergreenHUD.resourceDir.mkdirs()
        File(EvergreenHUD.dataDir, "resources/user").also { it.mkdirs() }

        val resourceMap = hashMapOf<File, String>()
        reflections.getResources { true }.forEach {
            resourceMap[File(EvergreenHUD.resourceDir, it.replaceFirst("evergreenhud/export/", ""))] = it
        }

        for (file in EvergreenHUD.resourceDir.walkTopDown()) {
            // delete old resources
            if (resourceMap[file] == null) {
                LOGGER.info("Deleting unknown resource. (Please use evergreenhud/resources/user for user resources) - ${file.path}")
                file.delete()
            }
        }

        for ((outputFile, resourceName) in resourceMap) {
            val resourceStream = EvergreenHUD::class.java.getResourceAsStream("/$resourceName")
            if (resourceStream == null) {
                LOGGER.err("Failed to export resource: ${outputFile.path}")
                continue
            }
            outputFile.parentFile.mkdirs()
            Files.copy(resourceStream, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }

        // FIXME: 18/08/2021 version saves under resource object
        metadata[ResourceSpec.version] = EvergreenInfo.VERSION_FULL.toString()
        metadataFile.parentFile.mkdirs()
        metadata.toToml.toFile(metadataFile)
    }
}

object ResourceSpec : ConfigSpec() {
    val version by optional<String>(EvergreenInfo.VERSION_FULL.toString())
}