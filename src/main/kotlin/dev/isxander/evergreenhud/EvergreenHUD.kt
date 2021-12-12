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

import dev.isxander.evergreenhud.addons.AddonLoader
import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.config.profile.ProfileManager
import dev.isxander.evergreenhud.elements.impl.ElementImage
import dev.isxander.evergreenhud.event.EventBus
import dev.isxander.evergreenhud.event.ServerDamageEntityEventManager
import dev.isxander.evergreenhud.gui.screens.ElementDisplay
import dev.isxander.evergreenhud.repo.ReleaseChannel
import dev.isxander.evergreenhud.repo.RepoManager
import dev.isxander.evergreenhud.utils.*
import io.ejekta.kambrik.Kambrik
import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.loader.api.FabricLoader
import org.bundleproject.libversion.Version
import org.lwjgl.glfw.GLFW
import java.awt.Color
import java.io.File
import java.net.URI
import kotlin.random.Random
import kotlin.reflect.full.createInstance

object EvergreenHUD : ClientModInitializer {
    const val NAME = "__GRADLE_NAME__"
    const val ID = "__GRADLE_ID__"
    const val REVISION = "__GRADLE_REVISION__"
    const val VERSION_STR = "__GRADLE_VERSION__"
    val VERSION = Version.of(VERSION_STR)
    val RELEASE_CHANNEL: ReleaseChannel
        get() =
            if (VERSION.prerelease == null) ReleaseChannel.RELEASE
            else ReleaseChannel.BETA

    val dataDir: File = File(mc.runDirectory, "evergreenhud")
    val eventBus = EventBus()

    lateinit var profileManager: ProfileManager private set
    lateinit var elementManager: ElementManager private set
    lateinit var addonLoader: AddonLoader private set

    var postInitialized = false
        set(value) { if (!field)  field = value }

    /**
     * Initialises the whole mod
     *
     * @since 2.0
     * @author isXander
     */
    override fun onInitializeClient() {
        logger.info("Starting EvergreenHUD $VERSION_STR")

        val startTime = System.currentTimeMillis()

        dataDir.mkdirs()

        logger.info("Initialising element manager...")
        elementManager = ElementManager()

        logger.info("Discovering addons...")
        addonLoader = AddonLoader().apply { load() }

        logger.info("Loading configs...")
        profileManager = ProfileManager().apply { load() }
        elementManager.apply {
//            availableElements.forEach { (clazz, _) ->
//                addElement(clazz.createInstance().apply {
//                    position.scaledX = Random.nextFloat()
//                    position.scaledY = Random.nextFloat()
//                    position.origin = Position2D.Origin.values()[Random.nextInt(0, Position2D.Origin.values().size)]
//                })
//            }
//            elementConfig.save()

            mainConfig.load()
            elementConfig.load()
        }

        if (!FabricLoader.getInstance().isDevelopmentEnvironment) {
            if (elementManager.checkForUpdates || elementManager.checkForSafety) {
                logger.info("Getting information from API...")
                runAsync {
                    val response = runBlocking { RepoManager.getResponse() }

                    if (elementManager.checkForUpdates && response.latest[RELEASE_CHANNEL.id]!! < VERSION) {
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

        Kambrik.Command.addClientCommand("evergreenhud") {
            runs {
//                mc.setScreen(ElementDisplay())
            }
        }

        Kambrik.Input.registerKeyboardBinding(
            GLFW.GLFW_KEY_HOME,
            keyTranslation = "evergreenhud.key.opengui",
            keyCategory = "evergreenhud.keycategory"
        ) {
            onDown {
//                mc.setScreen(ElementDisplay())
            }
        }

        Kambrik.Input.registerKeyboardBinding(
            GLFW.GLFW_KEY_UNKNOWN,
            keyTranslation = "evergreenhud.key.toggle",
            keyCategory = "evergreenhud.keycategory"
        ) {
            elementManager.enabled = !elementManager.enabled
            Notifications.push("EvergreenHUD", "Toggled mod.")
        }

        logger.info("Invoking addon entrypoints...")
        addonLoader.invokeEntrypoints()

        logger.info("Registering events...")
        registerEvents()

        logger.info("Finished loading EvergreenHUD. Took ${System.currentTimeMillis() - startTime} ms.")
    }

    private fun registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register {
            eventBus.post { onClientTick() }
        }

        eventBus.subscribe(ServerDamageEntityEventManager())
    }
}
