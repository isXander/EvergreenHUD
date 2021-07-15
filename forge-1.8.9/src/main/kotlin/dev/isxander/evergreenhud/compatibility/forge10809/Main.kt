package dev.isxander.evergreenhud.compatibility.forge10809

import club.chachy.event.forge.on
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.EvergreenInfo
import dev.isxander.evergreenhud.compatibility.forge10809.keybind.KeybindManager
import dev.isxander.evergreenhud.compatibility.universal.*
import dev.isxander.evergreenhud.compatibility.universal.impl.*
import dev.isxander.evergreenhud.event.RenderHUDEvent
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.dsl.childOf
import gg.essential.universal.UMinecraft
import gg.essential.universal.UScreen
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.Tessellator
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
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.Method

@Mod(modid = EvergreenInfo.MOD_ID, name = "EvergreenHUD", version = EvergreenInfo.MOD_REVISION, clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]", modLanguageAdapter = "dev.isxander.evergreenhud.compatibility.forge10809.KotlinLanguageAdapter")
object ForgeMod {

    val logger: Logger = LogManager.getLogger("EvergreenHUD")
    val mc: Minecraft = Minecraft.getMinecraft()

    private var scaledRes: ScaledResolution = ScaledResolution(mc)

    @Mod.EventHandler
    fun onFMLInit(event: FMLInitializationEvent) {
        MC_VERSION = MCVersion.FORGE_1_8_9

        LOGGER = object : AILogger() {
            override fun info(msg: String) = logger.info(msg)
            override fun warn(msg: String) = logger.warn(msg)
            override fun err(msg: String) = logger.error(msg)
        }

        MC = object : AIMinecraft() {
            override fun dataDir(): File = mc.mcDataDir
            override fun fps(): Int = Minecraft.getDebugFPS()
            override fun inGameHasFocus(): Boolean = mc.inGameHasFocus
        }

        RESOLUTION = object : AIResolution() {
            override fun getDisplayWidth(): Int = mc.displayWidth
            override fun getDisplayHeight(): Int = mc.displayHeight

            override fun getScaledWidth(): Int = scaledRes.scaledWidth
            override fun getScaledHeight(): Int = scaledRes.scaledHeight

            override fun getScaleFactor(): Double = scaledRes.scaleFactor.toDouble()
        }

        KEYBIND_MANAGER = KeybindManager()

        SCREEN_HANDLER = object : AIScreenHandler() {
            override fun displayComponent(component: UIComponent) {
                mc.displayGuiScreen(object : WindowScreen() {
                    init { component childOf window }
                })
            }
        }

        registerEvents()

        EvergreenHUD.init()
    }

    private fun registerEvents() {
        on<TickEvent.ClientTickEvent>()
            .filter { it.phase == TickEvent.Phase.END }
            .subscribe { EvergreenHUD.EVENT_BUS.post(dev.isxander.evergreenhud.event.TickEvent()) }

        on<RenderGameOverlayEvent.Post>()
            .filter { it.type == RenderGameOverlayEvent.ElementType.ALL }
            .subscribe {
                scaledRes = it.resolution
                EvergreenHUD.EVENT_BUS.post(RenderHUDEvent(it.partialTicks))
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