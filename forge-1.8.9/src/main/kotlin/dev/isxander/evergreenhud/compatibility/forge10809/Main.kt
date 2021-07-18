package dev.isxander.evergreenhud.compatibility.forge10809

import club.chachy.event.forge.on
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.EvergreenInfo
import dev.isxander.evergreenhud.compatibility.forge10809.impl.*
import dev.isxander.evergreenhud.compatibility.forge10809.keybind.KeybindManager
import dev.isxander.evergreenhud.compatibility.universal.*
import dev.isxander.evergreenhud.compatibility.universal.impl.*
import dev.isxander.evergreenhud.event.RenderHUDEvent
import dev.isxander.evergreenhud.event.RenderTickEvent
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.dsl.childOf
import gg.essential.universal.UMatrixStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.FMLModContainer
import net.minecraftforge.fml.common.ILanguageAdapter
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.ModContainer
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.lang.reflect.Field
import java.lang.reflect.Method

val mc: Minecraft = Minecraft.getMinecraft()

@Mod(modid = EvergreenInfo.MOD_ID, name = "EvergreenHUD", version = EvergreenInfo.MOD_REVISION, clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]", modLanguageAdapter = "dev.isxander.evergreenhud.compatibility.forge10809.KotlinLanguageAdapter")
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
        SCREEN_HANDLER = ScreenHandlerImpl()
        KEYBIND_MANAGER = KeybindManager()

        registerEvents()

        EvergreenHUD.init()
    }

    private fun registerEvents() {
        on<TickEvent.ClientTickEvent>()
            .filter { it.phase == TickEvent.Phase.END }
            .subscribe { EvergreenHUD.EVENT_BUS.post(dev.isxander.evergreenhud.event.ClientTickEvent()) }

        on<TickEvent.RenderTickEvent>()
            .subscribe {
                if (it.phase == TickEvent.Phase.START) return@subscribe

                EvergreenHUD.EVENT_BUS.post(RenderTickEvent(it.renderTickTime, UMatrixStack.Compat.get()))
            }

        on<RenderGameOverlayEvent.Post>()
            .filter { it.type == RenderGameOverlayEvent.ElementType.ALL }
            .subscribe {
                scaledRes = it.resolution
                EvergreenHUD.EVENT_BUS.post(RenderHUDEvent(it.partialTicks, UMatrixStack.Compat.get()))
            }
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
        return objectClass.fields.find { it.name == "INSTANCE" }?.get(null) ?: objectClass.newInstance()
    }

    override fun setInternalProxies(mod: ModContainer?, side: Side?, loader: ClassLoader?) {}
}