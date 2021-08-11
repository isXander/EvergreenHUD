/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud

import com.typesafe.config.ConfigFactory
import dev.isxander.evergreenhud.compatibility.universal.*
import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.compatibility.universal.impl.keybind.CustomKeybind
import dev.isxander.evergreenhud.compatibility.universal.impl.keybind.Keyboard
import dev.isxander.evergreenhud.config.profile.ProfileManager
import dev.isxander.evergreenhud.gui.MainGui
import dev.isxander.evergreenhud.repo.RepoManager
import dev.isxander.evergreenhud.utils.*
import gg.essential.universal.UDesktop
import me.kbrewster.eventbus.EventBus
import me.kbrewster.eventbus.eventbus
import me.kbrewster.eventbus.invokers.ReflectionInvoker
import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import java.awt.Color
import java.io.File
import java.net.URI
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object EvergreenHUD {

    val dataDir: File = File(MC.dataDir, "evergreenhud")
    val resourceDir: File = File(dataDir, "resources/default")
    val eventBus: EventBus = eventbus {
        invoker { ReflectionInvoker() }
        threadSaftey { true }
        exceptionHandler {
            LOGGER.err("Error occurred in method while posting event")
            it.printStackTrace()
        }
    }

    lateinit var profileManager: ProfileManager private set
    lateinit var elementManager: ElementManager private set

    /**
     * Initialises the whole mod
     *
     * @since 2.0
     * @author isXander
     */
    fun init() {
        LOGGER.info("Starting EvergreenHUD ${EvergreenInfo.VERSION_FULL} (${MC_VERSION.display})")

        dataDir.mkdirs()

        exportResources()
        profileManager = ProfileManager().also { it.load() }
        elementManager = ElementManager().also {
            it.mainConfig.load()
            it.elementConfig.load()
        }

        if (!MC.devEnv) {
            if (elementManager.checkForUpdates || elementManager.checkForSafety) {
                LOGGER.info("Getting information from API...")
                Multithreading.runAsync {
                    val response = RepoManager.getResponse()

                    if (elementManager.checkForUpdates && response.outdated) {
                        LOGGER.info("Found update. Pushing notification to user.")
                        Notifications.push("EvergreenHUD", "EvergreenHUD is out of date. Click here to download the new update.") {
                            UDesktop.browse(URI.create("https://www.isxander.dev/mods/evergreenhud"))
                        }
                    }

                    if (elementManager.checkForSafety && response.blacklisted) {
                        LOGGER.info("Mod version has been marked as dangerous. Pushing notification to user.")
                        Notifications.push("EvergreenHUD", "This version has been remotely marked as dangerous. Please update here.", textColor = Color.red) {
                            UDesktop.browse(URI.create("https://www.isxander.dev/mods/evergreenhud"))
                        }
                    }
                }
            }
        } else {
            LOGGER.info("Skipping update and blacklisting check due to being in a development environment.")
        }

        eventBus.register(this)

        KEYBIND_MANAGER.registerKeybind(CustomKeybind(Keyboard.KEY_HOME, "Open EvergreenHUD GUI", "EvergreenHUD") { SCREEN_HANDLER.displayComponent(MainGui()) })
        KEYBIND_MANAGER.registerKeybind(CustomKeybind(Keyboard.KEY_NONE, "Toggle EvergreenHUD", "EvergreenHUD") {
            elementManager.enabled = !elementManager.enabled
            Notifications.push("EvergreenHUD", "Toggled mod.")
        })
    }

    /**
     * Exports all resources from the JAR into a folder.
     * Used to allow users to select default resources for
     * profile icons etc
     *
     * @author isXander
     */
    private fun exportResources() {
        LOGGER.info("Exporting resources...")

        // every 3 days export resources
        val metadataFile = File(dataDir, "resources/metadata.conf")
        if (MC.devEnv || !metadataFile.exists() || ConfigFactory.parseFile(metadataFile).root().getOrDefault("version", "1.0") != EvergreenInfo.VERSION_FULL) {
            val reflections = Reflections("evergreenhud.export", ResourcesScanner())
            resourceDir.mkdirs()
            File(dataDir, "resources/user").also { it.mkdirs() }

            val resourceMap = hashMapOf<File, String>()
            reflections.getResources { true }.forEach {
                resourceMap[File(resourceDir, it.replaceFirst("evergreenhud/export/", ""))] = it
            }

            for (file in resourceDir.walkTopDown()) {
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
                Files.copy(resourceStream, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }

            val metadata = ConfigFactory.empty()
                .withValue("updated", EvergreenInfo.VERSION_FULL.asConfig())

            metadataFile.parentFile.mkdirs()
            Files.write(
                metadataFile.toPath(),
                metadata.resolve().root().render(niceConfigRender).lines(),
                StandardCharsets.UTF_8
            )
        }
    }
}