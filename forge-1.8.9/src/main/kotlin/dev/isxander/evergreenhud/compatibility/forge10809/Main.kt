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

package dev.isxander.evergreenhud.compatibility.forge10809

import club.chachy.event.on
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.EvergreenInfo
import dev.isxander.evergreenhud.compatibility.forge10809.events.ServerDamageEntityEventManager
import dev.isxander.evergreenhud.compatibility.forge10809.impl.*
import dev.isxander.evergreenhud.compatibility.universal.*
import dev.isxander.evergreenhud.event.ClientDamageEntity
import dev.isxander.evergreenhud.event.RenderHUDEvent
import dev.isxander.evergreenhud.event.RenderTickEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.fml.common.FMLModContainer
import net.minecraftforge.fml.common.ILanguageAdapter
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.ModContainer
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import java.lang.reflect.Field
import java.lang.reflect.Method

val mc: Minecraft = Minecraft.getMinecraft()

@Mod(modid = EvergreenInfo.ID, name = "EvergreenHUD", version = EvergreenInfo.REVISION, clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]", modLanguageAdapter = "dev.isxander.evergreenhud.compatibility.forge10809.KotlinLanguageAdapter")
object ForgeMod {

    private var scaledRes: ScaledResolution = ScaledResolution(mc)

    @Mod.EventHandler
    fun onFMLInit(event: FMLInitializationEvent) {
        MC_VERSION = MCVersion.FORGE_1_8_9

        LOGGER = LoggerImpl()
        MC = MinecraftImpl()
        RESOLUTION = ResolutionImpl()
        MOUSE_HELPER = MouseHelperImpl()
        BUFFER_BUILDER = BufferBuilderImpl()
        FONT_RENDERER = FontRendererImpl()
        GL = GLImpl()
        LOADER = LoaderImpl()
        PROFILER = ProfilerImpl()
        KEYBIND_MANAGER = KeybindManagerImpl()
        COMMAND_HANDLER = CommandHandlerImpl()
        SCREEN_HANDLER = ScreenHandlerImpl()
        TRANSLATION = TranslationImpl()
        POTIONS = PotionsImpl()

        registerEvents()

        EvergreenHUD.init()
    }

    private fun registerEvents() {
        MinecraftForge.EVENT_BUS.register(ServerDamageEntityEventManager)

        on<TickEvent.ClientTickEvent>()
            .filter { it.phase == TickEvent.Phase.END }
            .subscribe { EvergreenHUD.eventBus.post(dev.isxander.evergreenhud.event.ClientTickEvent()) }

        on<TickEvent.RenderTickEvent>()
            .subscribe {
                if (it.phase == TickEvent.Phase.START) return@subscribe

                EvergreenHUD.eventBus.post(RenderTickEvent(it.renderTickTime))
            }

        on<RenderGameOverlayEvent.Post>()
            .filter { it.type == RenderGameOverlayEvent.ElementType.ALL }
            .subscribe {
                scaledRes = it.resolution
                EvergreenHUD.eventBus.post(RenderHUDEvent(it.partialTicks))
            }

        on<AttackEntityEvent>()
            .subscribe { EvergreenHUD.eventBus.post(ClientDamageEntity(EntityImpl(it.entityPlayer), EntityImpl(it.target))) }
    }

}

class KotlinLanguageAdapter : ILanguageAdapter {

    override fun supportsStatics() = false

    override fun setProxy(target: Field, proxyTarget: Class<*>, proxy: Any) {
        target.set(proxyTarget.getDeclaredField("INSTANCE").get(null), proxy)
    }

    override fun getNewInstance(
        container: FMLModContainer,
        objectClass: Class<*>,
        classLoader: ClassLoader,
        factoryMarkedAnnotation: Method?
    ): Any {
        return objectClass.fields.find { it.name == "INSTANCE" }?.get(null) ?: objectClass.getDeclaredConstructor().newInstance()
    }

    override fun setInternalProxies(mod: ModContainer?, side: Side?, loader: ClassLoader?) {}
}