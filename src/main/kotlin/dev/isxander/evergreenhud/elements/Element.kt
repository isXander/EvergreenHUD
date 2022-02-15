/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.config.element.ElementSerializer
import dev.isxander.evergreenhud.event.Event
import dev.isxander.evergreenhud.event.EventListener
import dev.isxander.evergreenhud.ui.ElementDisplay
import dev.isxander.evergreenhud.utils.HitBox2D
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.evergreenhud.utils.position.ZonedPosition
import dev.isxander.settxi.Setting
import dev.isxander.settxi.impl.boolean
import dev.isxander.settxi.impl.float
import dev.isxander.settxi.serialization.ConfigProcessor
import kotlinx.serialization.Serializable
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats


@Serializable(ElementSerializer::class)
abstract class Element : ConfigProcessor {
    var isAdded = false
        private set

    override val settings = mutableListOf<Setting<*>>()
    val metadata = EvergreenHUD.elementManager.availableElements[this::class]!!
    var position = ZonedPosition.center()

    var scale by float(100f) {
        name = "Scale"
        description = "How large the element appears on the screen."
        category = "Render"
        range = 50f..500f
        shouldSave = false

        set {
            position.scale = it / 100f
            it
        }
        get { position.scale * 100f }
    }

    var showInChat by boolean(false) {
        name = "Show In Chat"
        category = "Visibility"
        description = "Render the element if you are in the chat. (Takes priority over show under gui)"
    }

    var showInDebug by boolean(false) {
        name = "Show In F3"
        category = "Visibility"
        description = "Render the element if you have the debug screen (F3 menu) open."
    }

    var showUnderGui by boolean(true) {
        name = "Show Under GUIs"
        category = "Visibility"
        description = "Render the element even when you have a gui open."
    }

    var showInReplayViewer by boolean(false) {
        name = "Show In Replay Viewer"
        category = "Visibility"
        description = "Render the element if you are in the replay viewer."

        depends { EvergreenHUD.isReplayModLoaded }
    }

    /* called when element is added */
    open fun onAdded() {
        isAdded = true
    }
    /* called when element is removed */
    open fun onRemoved() {
        isAdded = false
    }

    abstract fun render(renderOrigin: RenderOrigin)

    abstract fun calculateHitBox(scale: Float): HitBox2D
    protected open val hitboxWidth = 10f
    protected open val hitboxHeight = 10f

    open fun canRenderInHUD(): Boolean {
        val inChat = mc.currentScreen is GuiChat
        val inDebug = mc.gameSettings.showDebugInfo
        val inGui = mc.currentScreen != null && mc.currentScreen !is ElementDisplay && !inChat
        val inReplayViewer = EvergreenHUD.isReplayModLoaded && mc.theWorld != null && !mc.isSingleplayer && mc.currentServerData == null && !mc.isConnectedToRealms

        return (mc.inGameHasFocus && !inDebug)
                || (showInChat && inChat)
                || (showInDebug && inDebug && !(!showInChat && inChat))
                || (showUnderGui && inGui)
                || (showInReplayViewer && inReplayViewer)
    }

    fun resetSettings(save: Boolean = false) {
        position = ZonedPosition.scaledPositioning(0.25f, 0.25f)

        for (s in settings) s.reset()
        if (save) EvergreenHUD.elementManager.elementConfig.save()
    }

    protected inline fun <reified T : Event> event(noinline predicate: (T) -> Boolean = { true }, noinline executor: (T) -> Unit): EventListener<T, Unit?> {
        return eventBus.register({ isAdded && predicate(it) }, executor)
    }

    protected inline fun <reified T : Event, R : Any?> eventReturnable(defaultCache: R? = null, noinline predicate: (T) -> Boolean = { true }, noinline executor: (T) -> R?): EventListener<T, R?> {
        return eventBus.registerReturnable(defaultCache, { isAdded && predicate(it) }, executor)
    }

    companion object {
        protected val eventBus by EvergreenHUD::eventBus
    }

    fun drawTexturedModalRectF(x: Float, y: Float, textureX: Int, textureY: Int, width: Float, height: Float) {
        val f = 0.00390625f
        val f1 = 0.00390625f
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX)
        worldrenderer.pos(x.toDouble(), (y + height).toDouble(), 0.0)
            .tex((textureX * f).toDouble(), ((textureY + height) * f1).toDouble()).endVertex()
        worldrenderer.pos((x + width).toDouble(), (y + height).toDouble(), 0.0)
            .tex(((textureX + width) * f).toDouble(), ((textureY + height) * f1).toDouble()).endVertex()
        worldrenderer.pos((x + width).toDouble(), y.toDouble(), 0.0)
            .tex(((textureX + width) * f).toDouble(), (textureY * f1).toDouble()).endVertex()
        worldrenderer.pos((x + 0).toDouble(), (y + 0).toDouble(), 0.0)
            .tex((textureX * f).toDouble(), (textureY * f1).toDouble()).endVertex()
        tessellator.draw()
    }
}

