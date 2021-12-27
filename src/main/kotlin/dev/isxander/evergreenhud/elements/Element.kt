/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.elements

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.config.element.ElementSerializer
import dev.isxander.evergreenhud.event.EventListener
import dev.isxander.evergreenhud.gui.components.Positionable
import dev.isxander.evergreenhud.gui.screens.ElementDisplay
import dev.isxander.settxi.Setting
import dev.isxander.settxi.impl.*
import dev.isxander.evergreenhud.utils.*
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.position.Position2D
import dev.isxander.evergreenhud.utils.position.scaledPosition
import dev.isxander.settxi.serialization.ConfigProcessor
import kotlinx.serialization.Serializable
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.util.math.MatrixStack

@Serializable(ElementSerializer::class)
abstract class Element : EventListener, ConfigProcessor, Positionable<Float> {
    var isAdded = false
        private set

    override val settings: MutableList<Setting<*>> = mutableListOf()
    val metadata: ElementMeta = EvergreenHUD.elementManager.availableElements[this::class]!!
    var position: Position2D =
        scaledPosition {
            x = 0.5f
            y = 0.5f
        }

    override var x by position::rawX
    override var y by position::rawY

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

        depends { FabricLoader.getInstance().isModLoaded("replaymod") }
    }

    /* called when element is added */
    open fun onAdded() {
        eventBus.register(this)
        isAdded = true
    }
    /* called when element is removed */
    open fun onRemoved() {
        eventBus.unregister(this)
        utilities.unregisterAllForObject(this)
        isAdded = false
    }

    abstract fun render(matrices: MatrixStack, renderOrigin: RenderOrigin)

    abstract fun calculateHitBox(glScale: Float, drawScale: Float): HitBox2D
    protected open val hitboxWidth = 10f
    protected open val hitboxHeight = 10f

    open fun canRenderInHUD(): Boolean {
        val inChat = mc.currentScreen is ChatScreen
        val inDebug = mc.options.debugEnabled
        val inGui = mc.currentScreen != null && mc.currentScreen !is ElementDisplay && !inChat
        val inReplayViewer = FabricLoader.getInstance().isModLoaded("replaymod") && mc.world != null && !mc.isInSingleplayer && mc.currentServerEntry == null && !mc.isConnectedToRealms

        return (mc.mouse.isCursorLocked && !inDebug)
                || (showInChat && inChat)
                || (showInDebug && inDebug && !(!showInChat && inChat))
                || (showUnderGui && inGui)
                || (showInReplayViewer && inReplayViewer)
    }

    fun resetSettings(save: Boolean = false) {
        position = Position2D.scaledPositioning(0.5f, 0.5f, 1f)

        for (s in settings) s.reset()
        if (save) EvergreenHUD.elementManager.elementConfig.save()
    }

    companion object {
        protected val utilities = ElementUtilitySharer()
        protected val eventBus by EvergreenHUD::eventBus
    }
}

