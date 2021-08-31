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

import com.asarkar.semver.SemVer
import dev.isxander.evergreenhud.compatibility.universal.*
import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.compatibility.universal.impl.registerCommand
import dev.isxander.evergreenhud.compatibility.universal.impl.registerKeybind
import dev.isxander.evergreenhud.utils.Keyboard
import dev.isxander.evergreenhud.config.profile.ProfileManager
import dev.isxander.evergreenhud.elements.impl.ElementFps
import dev.isxander.evergreenhud.gui.MainGui
import dev.isxander.evergreenhud.repo.ReleaseChannel
import dev.isxander.evergreenhud.repo.RepoManager
import dev.isxander.evergreenhud.utils.*
import gg.essential.universal.UDesktop
import me.kbrewster.eventbus.EventBus
import me.kbrewster.eventbus.eventbus
import me.kbrewster.eventbus.invokers.ReflectionInvoker
import java.awt.Color
import java.io.File
import java.net.URI

object EvergreenHUD {
    const val NAME = "__GRADLE_NAME__"
    const val ID = "__GRADLE_ID__"
    const val REVISION = "__GRADLE_REVISION__"
    const val VERSION_STR = "__GRADLE_VERSION__"
    val VERSION = SemVer.parse(VERSION_STR)
    val RELEASE_CHANNEL: ReleaseChannel
        get() =
            if (VERSION.hasPreReleaseVersion()) ReleaseChannel.RELEASE
            else ReleaseChannel.BETA

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
        LOGGER.info("Starting EvergreenHUD $VERSION_STR (${MC_VERSION.display})")

        dataDir.mkdirs()

        exportResources()
        profileManager = ProfileManager().apply { load() }
        elementManager = ElementManager().apply {
            mainConfig.load()
            elementConfig.load()
            addElement(ElementFps().preload().apply {
                position = rawPosition {
                    x = 20f
                    y = 20f
                }
            })
            elementConfig.save()
        }

        if (!MC.devEnv) {
            if (elementManager.checkForUpdates || elementManager.checkForSafety) {
                LOGGER.info("Getting information from API...")
                runAsync {
                    val response = RepoManager.getResponse()

                    if (elementManager.checkForUpdates && response.latest < VERSION) {
                        LOGGER.info("Found update. Pushing notification to user.")
                        Notifications.push("EvergreenHUD", "EvergreenHUD is out of date. Click here to download the new update.") {
                            UDesktop.browse(URI.create("https://www.isxander.dev/mods/evergreenhud"))
                        }
                    }

                    if (elementManager.checkForSafety && REVISION in response.blacklisted) {
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

        registerCommand {
            invoke = "evergreenhud"

            execute {
                SCREEN_HANDLER.displayComponentNextTick(MainGui())
            }
        }

        registerKeybind {
            key = Keyboard.KEY_HOME
            name = "Open EvergreenHUD GUI"
            category = "EvergreenHUD"

            execute {
                SCREEN_HANDLER.displayComponent(MainGui())
            }
        }
        registerKeybind {
            key = Keyboard.KEY_NONE
            name = "Toggle EvergreenHUD"
            category = "EvergreenHUD"

            execute {
                elementManager.enabled = !elementManager.enabled
                Notifications.push("EvergreenHUD", "Toggled mod.")
            }
        }
    }


}