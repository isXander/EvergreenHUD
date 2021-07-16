package dev.isxander.evergreenhud.compatibility.fabric11701

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.fabric11701.keybind.KeybindManager
import dev.isxander.evergreenhud.compatibility.fabric11701.mixins.AccessorMinecraftClient
import dev.isxander.evergreenhud.compatibility.fabric11701.provider.AIEntityProvider
import dev.isxander.evergreenhud.compatibility.universal.*
import dev.isxander.evergreenhud.compatibility.universal.impl.*
import dev.isxander.evergreenhud.compatibility.universal.impl.entity.AIEntity
import dev.isxander.evergreenhud.compatibility.universal.impl.render.AIBufferBuilder
import dev.isxander.evergreenhud.compatibility.universal.impl.render.AIGL11
import dev.isxander.evergreenhud.compatibility.universal.impl.render.DrawMode.*
import dev.isxander.evergreenhud.compatibility.universal.impl.render.VertexFormats
import dev.isxander.evergreenhud.event.ClientTickEvent
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat.DrawMode
import net.minecraft.client.util.math.MatrixStack
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.opengl.GL11
import java.io.File


object Main : ClientModInitializer {

    val logger: Logger = LogManager.getLogger("EvergreenHUD")
    lateinit var matrices: MatrixStack
    var postInitialized = false

    override fun onInitializeClient() {
        val mc = MinecraftClient.getInstance()

        MC_VERSION = MCVersion.FABRIC_1_17_1

        LOGGER = object : AILogger() {
            override fun info(msg: String) = logger.info(msg)
            override fun warn(msg: String) = logger.warn(msg)
            override fun err(msg: String) = logger.error(msg)
        }

        MC = object : AIMinecraft() {
            override fun player(): AIEntity = AIEntityProvider.getPlayer()
            override fun dataDir(): File = mc.runDirectory
            override fun fps(): Int = AccessorMinecraftClient.getFps()
            override fun inGameHasFocus(): Boolean = mc.currentScreen != null
        }

        RESOLUTION = object : AIResolution() {
            override fun getDisplayWidth(): Int = mc.window.width
            override fun getDisplayHeight(): Int = mc.window.height

            override fun getScaledWidth(): Int = mc.window.scaledWidth
            override fun getScaledHeight(): Int = mc.window.scaledHeight

            override fun getScaleFactor(): Double = mc.window.scaleFactor
        }

        KEYBIND_MANAGER = KeybindManager()

        SCREEN_HANDLER = object : AIScreenHandler() {
            override fun displayComponent(component: UIComponent) {
                LOGGER.info("afiah")
                object: WindowScreen() {}
//                mc.setScreen(object : WindowScreen() {
//                    init { component childOf window }
//                })
            }
        }

        BUFFER_BUILDER = object : AIBufferBuilder() {
            private val tes = Tessellator.getInstance()
            private val buf: BufferBuilder = tes.buffer

            override fun vertex(x: Double, y: Double, z: Double): AIBufferBuilder {
                buf.vertex(x, y, z)
                return this
            }

            override fun color(r: Float, g: Float, b: Float, a: Float): AIBufferBuilder {
                buf.color(r, g, b, a)
                return this
            }

            override fun tex(u: Double, v: Double): AIBufferBuilder {
                buf.texture(u.toFloat(), v.toFloat())
                return this
            }

            override fun next(): AIBufferBuilder {
                buf.next()
                return this
            }
            override fun end(): AIBufferBuilder {
                buf.end()
                return this
            }

            override fun begin(mode: dev.isxander.evergreenhud.compatibility.universal.impl.render.DrawMode, format: VertexFormats): AIBufferBuilder {
                val parsedFormat = when (format) {
                    VertexFormats.POSITION -> net.minecraft.client.render.VertexFormats.POSITION
                    VertexFormats.POSITION_COLOR -> net.minecraft.client.render.VertexFormats.POSITION_COLOR
                    VertexFormats.POSITION_TEXTURE -> net.minecraft.client.render.VertexFormats.POSITION_TEXTURE
                    VertexFormats.POSITION_COLOR_TEXTURE -> net.minecraft.client.render.VertexFormats.POSITION_COLOR_TEXTURE
                }
                val parsedMode = when (mode) {
                    LINES -> DrawMode.LINES
                    LINE_STRIP -> DrawMode.LINE_STRIP
                    TRIANGLES -> DrawMode.TRIANGLES
                    TRIANGLE_STRIP -> DrawMode.TRIANGLE_STRIP
                    TRIANGLE_FAN -> DrawMode.TRIANGLE_FAN
                    QUADS -> DrawMode.QUADS
                }

                buf.begin(parsedMode, parsedFormat)

                return this
            }

            override fun draw() = BufferRenderer.draw(buf)
        }

        GL = object : AIGL11() {
            override fun push() = matrices.push()
            override fun pop() = matrices.pop()

            override fun scale(x: Float, y: Float, z: Float) = matrices.scale(x, y, z)
            override fun translate(x: Double, y: Double, z: Double) = matrices.translate(x, y, z)
            override fun color(r: Float, g: Float, b: Float, a: Float) = RenderSystem.clearColor(r, g, b, a)
            override fun rotate(angle: Float, x: Float, y: Float, z: Float) = GL11.glRotatef(angle, x, y, z)

            override fun bindTexture(texture: Int) = RenderSystem.bindTexture(texture)

            override fun scissor(x: Int, y: Int, width: Int, height: Int) = GlStateManager._scissorBox(x, y, width, height)
            override fun enableScissor() = GlStateManager._enableScissorTest()
            override fun disableScissor() = GlStateManager._disableScissorTest()

            override fun lineWidth(width: Float) = RenderSystem.lineWidth(width)

            override fun enable(target: Int) = GL11.glEnable(target)
            override fun disable(target: Int) = GL11.glDisable(target)

            override fun begin(mode: Int) = GL11.glBegin(mode)
            override fun end() = GL11.glEnd()

            override fun enableBlend() = RenderSystem.enableBlend()
            override fun disableBlend() = RenderSystem.disableBlend()

            override fun enableTexture() = RenderSystem.enableTexture()
            override fun disableTexture() = RenderSystem.disableTexture()

            override fun enableAlpha() = enable(GL_ALPHA_TEST)
            override fun disableAlpha() = disable(GL_ALPHA_TEST)

            override fun enableDepth() = RenderSystem.enableDepthTest()
            override fun disableDepth() = RenderSystem.disableDepthTest()

            override fun blendFuncSeparate(srcFactorRGB: Int, dstFactorRGB: Int, srcFactorAlpha: Int, dstFactorAlpha: Int) =
                RenderSystem.blendFuncSeparate(srcFactorRGB, dstFactorRGB, srcFactorAlpha, dstFactorAlpha)
            override fun blendFunc(srcFactor: Int, dstFactor: Int) = RenderSystem.blendFunc(srcFactor, dstFactor)

            override fun rect(x: Float, y: Float, width: Float, height: Float, color: Int) {
                val x2 = x + width
                val y2 = y + height

                val f = (color shr 24 and 255).toFloat() / 255.0f
                val g = (color shr 16 and 255).toFloat() / 255.0f
                val h = (color shr 8 and 255).toFloat() / 255.0f
                val k = (color and 255).toFloat() / 255.0f
                enableBlend()
                disableTexture()
                defaultBlendFunc()
                RenderSystem.setShader { GameRenderer.getPositionColorShader() }
                BUFFER_BUILDER
                    .begin(QUADS, VertexFormats.POSITION_COLOR)
                    .vertex(x.toDouble(), y2.toDouble(), 0.0).color(g, h, k, f).next()
                    .vertex(x2.toDouble(), y2.toDouble(), 0.0).color(g, h, k, f).next()
                    .vertex(x2.toDouble(), y.toDouble(), 0.0).color(g, h, k, f).next()
                    .vertex(x.toDouble(), y.toDouble(), 0.0).color(g, h, k, f).next()
                    .end().draw()
                enableTexture()
                disableBlend()
            }

            override fun modalRect(x: Float, y: Float, u: Float, v: Float, uWidth: Float, vHeight: Float, width: Float, height: Float, tileWidth: Float, tileHeight: Float) {
                val f = 1.0 / tileWidth
                val f1 = 1.0 / tileHeight

                enableBlend()
                disableTexture()
                defaultBlendFunc()
                RenderSystem.setShader { GameRenderer.getPositionColorShader() }
                BUFFER_BUILDER
                    .begin(QUADS, VertexFormats.POSITION_TEXTURE)
                    .vertex(x.toDouble(), y.toDouble() + height, 0.0).tex(u * f, (v + vHeight) * f1).next()
                    .vertex(x.toDouble() + width, y.toDouble() + height, 0.0).tex((u + uWidth) * f, (v + vHeight) * f1).next()
                    .vertex(x.toDouble() + width, y.toDouble(), 0.0).tex((u + uWidth) * f, v * f1).next()
                    .vertex(x.toDouble(), y.toDouble(), 0.0).tex(u * f, v * f1).next()
                    .end().draw()
                enableTexture()
                disableBlend()
            }

            override fun roundedRect(x: Float, y: Float, width: Float, height: Float, color: Int, angle: Float) {
                TODO("Not yet implemented")
            }

            override fun circle(x: Float, y: Float, radius: Float, color: Int) {
                TODO("Not yet implemented")
            }
        }

        FONT_RENDERER = object : AIFontRenderer() {
            override fun draw(text: String, _x: Float, y: Float, color: Int, shadow: Boolean, centered: Boolean): AIFontRenderer {
                var x = _x
                if (centered) x -= width(text) / 2

                if (shadow) mc.textRenderer.drawWithShadow(matrices, text, x, y, color)
                else mc.textRenderer.draw(matrices, text, x, y, color)

                return this
            }

            override val fontHeight: Int
                get() = mc.textRenderer.fontHeight

            override fun width(text: String): Int = mc.textRenderer.getWidth(text)
        }

        MOUSE_HELPER = object : AIMouseHelper() {
            override fun getMouseX(): Float = mc.mouse.x.toFloat()
            override fun getMouseY(): Float = mc.mouse.y.toFloat()
            override fun wasLeftMouseDown(): Boolean = mc.mouse.wasLeftButtonClicked()
            override fun wasRightMouseDown(): Boolean = mc.mouse.wasRightButtonClicked()
        }

        registerEvents()

        EvergreenHUD.init()
    }

    private fun registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register {
            EvergreenHUD.EVENT_BUS.post(ClientTickEvent())
        }

    }

}