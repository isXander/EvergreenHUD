/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud

import dev.isxander.evergreenhud.addons.AddonLoader
import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.config.profile.ProfileManager
import dev.isxander.evergreenhud.event.ClientTickEvent
import dev.isxander.evergreenhud.event.EventBus
import dev.isxander.evergreenhud.event.RenderHudEvent
import dev.isxander.evergreenhud.event.ServerDamageEntityEventManager
import dev.isxander.evergreenhud.ui.BlacklistedScreen
import dev.isxander.evergreenhud.ui.UpdateScreen
import dev.isxander.evergreenhud.ui.test.PositionTest
import dev.isxander.evergreenhud.packets.client.registerElementsPacket
import dev.isxander.evergreenhud.repo.ReleaseChannel
import dev.isxander.evergreenhud.repo.RepoManager
import dev.isxander.evergreenhud.ui.ElementDisplay
import dev.isxander.evergreenhud.utils.*
import dev.isxander.evergreenhud.utils.hypixel.locraw.LocrawManager
import io.ejekta.kambrik.Kambrik
import io.ejekta.kambrik.text.textLiteral
import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.SharedConstants
import org.bundleproject.libversion.Version
import org.lwjgl.glfw.GLFW
import java.io.File

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

    val dataDir = File(mc.runDirectory, "evergreenhud")
    val eventBus = EventBus()
    val locrawManager = LocrawManager()

    lateinit var profileManager: ProfileManager private set
    lateinit var elementManager: ElementManager private set
    lateinit var addonLoader: AddonLoader private set

    val isReplayModLoaded = FabricLoader.getInstance().isModLoaded("replaymod")

    var postInitialized = false
        private set

    /**
     * Initialises the whole mod
     *
     * @since 2.0
     * @author isXander
     */
    override fun onInitializeClient() {
        logger.info("Starting EvergreenHUD $VERSION_STR for ${SharedConstants.getGameVersion().name}")

        val startTime = System.currentTimeMillis()

        dataDir.mkdirs()

        logger.debug("Initialising element manager...")
        elementManager = ElementManager()

        logger.debug("Discovering addons...")
        addonLoader = AddonLoader()
        logger.debug("Adding addon element sources...")
        addonLoader.addSources(elementManager)
        logger.debug("Invoking pre-initialization addon entrypoints...")
        addonLoader.invokePreinitEntrypoints()

        logger.debug("Loading configs...")
        profileManager = ProfileManager().apply { load() }
        elementManager.apply {
            globalConfig.load()
            elementConfig.load()
        }

        logger.debug("Registering hooks...")

        Kambrik.Command.addClientCommand("evergreenhud") {
            runs {
                GuiHandler.displayGui(ElementDisplay())
            }

            if (FabricLoader.getInstance().isDevelopmentEnvironment) {
                "test" {
                    "position" runs {
                        GuiHandler.displayGui(PositionTest())
                    }
                }
            }
        }

        Kambrik.Input.registerKeyboardBinding(
            GLFW.GLFW_KEY_HOME,
            keyTranslation = "evergreenhud.key.opengui",
            keyCategory = "evergreenhud.keycategory"
        ) {
            onDown {
                mc.setScreen(ElementDisplay())
            }
        }

        Kambrik.Input.registerKeyboardBinding(
            GLFW.GLFW_KEY_UNKNOWN,
            keyTranslation = "evergreenhud.key.toggle",
            keyCategory = "evergreenhud.keycategory"
        ) {
            onDown {
                elementManager.enabled = !elementManager.enabled
                mc.inGameHud?.chatHud?.addMessage(evergreenHudPrefix + textLiteral("Toggled mod."))
            }
        }

        logger.debug("Registering events...")
        registerEvents()

        logger.debug("Registering packet listeners...")
        registerElementsPacket()

        logger.debug("Invoking addon entrypoints...")
        addonLoader.invokeInitEntrypoints()

        logger.info("Finished loading EvergreenHUD. Took ${System.currentTimeMillis() - startTime} ms.")
    }

    fun onPostInitialize() {
        if (!postInitialized) {
            if (!FabricLoader.getInstance().isDevelopmentEnvironment) {
                if (elementManager.checkForUpdates || elementManager.checkForSafety) {
                    logger.info("Getting information from API...")
                    runAsync {
                        val response = runBlocking { RepoManager.getResponse() }

                        val latest = response.latest[RELEASE_CHANNEL]!!
                        if (elementManager.checkForUpdates && latest < VERSION) {
                            logger.info("Found update.")
                            mc.setScreen(UpdateScreen(latest.toString(), mc.currentScreen))
                        }

                        if (elementManager.checkForSafety && REVISION in response.blacklisted) {
                            logger.warn("Mod version has been marked as dangerous.")
                            mc.setScreen(BlacklistedScreen(mc.currentScreen))
                        }
                    }
                }
            } else {
                logger.info("Skipping update and blacklisting check due to being in a development environment.")
            }
        }

        postInitialized = true
    }

    private fun registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register {
            eventBus.post(ClientTickEvent())
        }

        HudRenderCallback.EVENT.register { matrices, tickDelta ->
            eventBus.post(RenderHudEvent(matrices, tickDelta))
        }

        ServerDamageEntityEventManager(eventBus)
    }
}
