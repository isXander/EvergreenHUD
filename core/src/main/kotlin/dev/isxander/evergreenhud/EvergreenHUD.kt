/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud

import com.github.zafarkhaja.semver.Version
import dev.deamsy.eventbus.api.EventBus
import dev.deamsy.eventbus.impl.asm.ASMEventBus
import dev.isxander.evergreenhud.addons.AddonLoader
import dev.isxander.evergreenhud.api.*
import dev.isxander.evergreenhud.api.impl.registerCommand
import dev.isxander.evergreenhud.api.impl.registerKeybind
import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.utils.Input
import dev.isxander.evergreenhud.config.profile.ProfileManager
import dev.isxander.evergreenhud.gui.MainGui
import dev.isxander.evergreenhud.repo.ReleaseChannel
import dev.isxander.evergreenhud.repo.RepoManager
import dev.isxander.evergreenhud.utils.*
import gg.essential.universal.UDesktop
import java.awt.Color
import java.io.File
import java.net.URI

object EvergreenHUD {
    const val NAME = "__GRADLE_NAME__"
    const val ID = "__GRADLE_ID__"
    const val REVISION = "__GRADLE_REVISION__"
    const val VERSION_STR = "__GRADLE_VERSION__"
    val VERSION = Version.valueOf(VERSION_STR)
    val RELEASE_CHANNEL: ReleaseChannel
        get() =
            if (VERSION.preReleaseVersion == null) ReleaseChannel.RELEASE
            else ReleaseChannel.BETA

    val dataDir: File = File(mc.dataDir, "evergreenhud")
    val resourceDir: File = File(dataDir, "resources/default")
    val eventBus: EventBus = ASMEventBus()

    lateinit var profileManager: ProfileManager private set
    lateinit var elementManager: ElementManager private set
    lateinit var addonLoader: AddonLoader private set

    /**
     * Initialises the whole mod
     *
     * @since 2.0
     * @author isXander
     */
    fun init() {
        logger.info("Starting EvergreenHUD $VERSION_STR (${mcVersion.display})")

        val startTime = System.currentTimeMillis()

        dataDir.mkdirs()

        logger.info("Initialising element manager...")
        elementManager = ElementManager()

        logger.info("Discovering addons...")
        addonLoader = AddonLoader().apply { load() }

        logger.info("Loading configs...")
        profileManager = ProfileManager().apply { load() }
        elementManager.apply {
            mainConfig.load()
            elementConfig.load()
        }

        if (!mc.devEnv) {
            if (elementManager.checkForUpdates || elementManager.checkForSafety) {
                logger.info("Getting information from API...")
                runAsync {
                    val response = RepoManager.getResponse()

                    if (elementManager.checkForUpdates && response.latest < VERSION) {
                        logger.info("Found update. Pushing notification to user.")
                        Notifications.push("EvergreenHUD", "EvergreenHUD is out of date. Click here to download the new update.") {
                            UDesktop.browse(URI.create("https://www.isxander.dev/mods/evergreenhud"))
                        }
                    }

                    if (elementManager.checkForSafety && REVISION in response.blacklisted) {
                        logger.info("Mod version has been marked as dangerous. Pushing notification to user.")
                        Notifications.push("EvergreenHUD", "This version has been remotely marked as dangerous. Please update here.", textColor = Color.red) {
                            UDesktop.browse(URI.create("https://www.isxander.dev/mods/evergreenhud"))
                        }
                    }
                }
            }
        } else {
            logger.info("Skipping update and blacklisting check due to being in a development environment.")
        }

        logger.info("Registering hooks...")
        registerCommand {
            invoke = "evergreenhud"

            execute {
                screenHandler.displayComponentNextTick(MainGui())
            }
        }

        registerKeybind {
            key = Input.KEY_HOME
            name = "Open EvergreenHUD GUI"
            category = "EvergreenHUD"

            onDown {
                screenHandler.displayComponent(MainGui())
            }
        }
        registerKeybind {
            key = Input.KEY_NONE
            name = "Toggle EvergreenHUD"
            category = "EvergreenHUD"

            onDown {
                elementManager.enabled = !elementManager.enabled
                Notifications.push("EvergreenHUD", "Toggled mod.")
            }
        }

        logger.info("Invoking addon entrypoints...")
        addonLoader.invokeEntrypoints()

        logger.info("Finished loading EvergreenHUD. Took ${System.currentTimeMillis() - startTime} ms.")
    }
}
