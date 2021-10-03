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

package dev.isxander.evergreenhud.api.forge10809

import club.chachy.event.on
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.api.forge10809.events.ServerDamageEntityEventManager
import dev.isxander.evergreenhud.api.forge10809.impl.*
import dev.isxander.evergreenhud.api.*
import dev.isxander.evergreenhud.api.impl.*
import dev.isxander.evergreenhud.api.impl.render.UBufferBuilder
import dev.isxander.evergreenhud.api.impl.render.UGL
import dev.isxander.evergreenhud.event.ClientDamageEntity
import dev.isxander.evergreenhud.event.RenderHUDEvent
import dev.isxander.evergreenhud.event.RenderTickEvent
import dev.isxander.evergreenhud.utils.MCVersion
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

@Mod(modid = EvergreenHUD.ID, name = "EvergreenHUD", version = EvergreenHUD.REVISION, clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]", modLanguageAdapter = "dev.isxander.evergreenhud.api.forge10809.KotlinLanguageAdapter")
object Main {
    private var scaledRes: ScaledResolution = ScaledResolution(mc)

    @Mod.EventHandler
    fun onFMLInit(event: FMLInitializationEvent) {
        di.bind(MCVersion.FORGE_1_8_9)

        di.bind<ULogManager>(LogManagerImpl())
        di.bind<UProfiler>(ProfilerImpl())
        di.bind<UMinecraft>(MinecraftImpl())
        di.bind<UResolution>(ResolutionImpl())
        di.bind<UKeybindManager>(KeybindManagerImpl())
        di.bind<UBufferBuilder>(BufferBuilderImpl())
        di.bind<UGL>(GLImpl())
        di.bind<UFontRenderer>(FontRendererImpl())
        di.bind<UMouseHelper>(MouseHelperImpl())
        di.bind<UCommandHandler>(CommandHandlerImpl())
        di.bind<UScreenHandler>(ScreenHandlerImpl())
        di.bind<UTextureManager>(TextureManagerImpl())
        di.bind<UTranslation>(TranslationImpl())
        di.bind<UPotions>(PotionsImpl())
        di.bind<UWorld>(WorldImpl())

        registerEvents()

        EvergreenHUD.init()
    }

    private fun registerEvents() {
        MinecraftForge.EVENT_BUS.register(ServerDamageEntityEventManager())

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

@Suppress("UNUSED")
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
