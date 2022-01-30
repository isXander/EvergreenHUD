/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelOutboundHandlerAdapter
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.util.function.Consumer


object ChannelPipelineManager {
    /* List of custom channel handlers to add next tick */
    private val handlers: MutableList<IChannelHandlerCustom>

    /* List of handler names to remove next tick */
    private val toRemoveHandlers: MutableList<String>

    init {
        handlers = ArrayList<IChannelHandlerCustom>()
        toRemoveHandlers = ArrayList()
        MinecraftForge.EVENT_BUS.register(this)
    }

    /**
     * Adds a Channel Handler to Minecraft's Nethandler
     * Also re-registers handler if present
     *
     * @param handler the actual handler
     */
    fun addHandler(handler: IChannelHandlerCustom) {
        handlers.add(handler)
    }

    /**
     * Removes channel handler from nethandler
     *
     * @param handler to remove
     */
    fun removeHandler(handler: IChannelHandlerCustom) {
        handlers.remove(handler)
        handler.name()?.let { toRemoveHandlers.add(it) }
    }

    /**
     * Registers / unregisters packet handlers
     */
    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (mc.netHandler == null) {
            return
        }
        val pipeline = mc.netHandler.networkManager.channel().pipeline()
        handlers.forEach(Consumer { handler: IChannelHandlerCustom ->
            if (pipeline[handler.name()] == null) {
                var meetsRequirements = true
                handler.requires()?.forEach { requirement ->
                    if (pipeline[requirement] == null) meetsRequirements = false
                }
                if (meetsRequirements) {
                    try {
                        if (handler.addBefore() != null) {
                            pipeline.addBefore(handler.addBefore(), handler.name(), handler.handler())
                        } else if (handler.addAfter() != null) {
                            pipeline.addAfter(handler.addAfter(), handler.name(), handler.handler())
                        } else {
                            if (handler.first()) pipeline.addFirst(
                                handler.name(),
                                handler.handler()
                            ) else pipeline.addLast(handler.name(), handler.handler())
                        }
                    } catch (e: NoSuchElementException) {
                        e.printStackTrace()
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                    }
                }
            }
        })
        for (name in toRemoveHandlers) {
            if (pipeline[name] != null) {
                try {
                    pipeline.remove(name)
                } catch (e: NoSuchElementException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
        }
        toRemoveHandlers.clear()
    }
}


class CustomChannelHandlerFactory private constructor(
    private var name: String?,
    private var handler: ChannelHandler?,
    private var addBefore: String?,
    private var addAfter: String?,
    private var first: Boolean,
    requirements: ArrayList<String?>
) {
    private var requirements: MutableList<String?>?

    init {
        this.requirements = requirements
    }

    fun setName(name: String?): CustomChannelHandlerFactory {
        this.name = name
        return this
    }

    fun setHandler(handler: ChannelHandler): CustomChannelHandlerFactory {
        requireNotNull(handler.javaClass.getAnnotation(Sharable::class.java)) { "ChannelHandler must be sharable." }
        this.handler = handler
        return this
    }

    fun setAddBefore(addBefore: String?): CustomChannelHandlerFactory {
        this.addBefore = addBefore
        return this
    }

    fun setAddAfter(addAfter: String?): CustomChannelHandlerFactory {
        this.addAfter = addAfter
        return this
    }

    fun setFirst(first: Boolean): CustomChannelHandlerFactory {
        this.first = first
        return this
    }

    fun setRequirements(vararg requirements: String?): CustomChannelHandlerFactory {
        this.requirements = arrayListOf(*requirements)
        return this
    }

    fun addRequirements(vararg requirements: String?): CustomChannelHandlerFactory {
        this.requirements!!.addAll(listOf(*requirements))
        return this
    }

    fun removeRequirements(vararg requirements: String?): CustomChannelHandlerFactory {
        this.requirements!!.removeAll(listOf(*requirements))
        return this
    }

    fun build(): IChannelHandlerCustom {
        if (validate()) {
            return object : IChannelHandlerCustom {
                override fun name(): String? {
                    return name
                }

                override fun handler(): ChannelHandler? {
                    return handler
                }

                override fun addBefore(): String? {
                    return addBefore
                }

                override fun addAfter(): String? {
                    return addAfter
                }

                override fun first(): Boolean {
                    return first
                }

                override fun requires(): Array<String?>? {
                    return requirements!!.toTypedArray()
                }
            }
        }
        throw IllegalStateException("Name or Channel Handler are null. These are needed to create a valid IChannelHandlerCustom")
    }

    fun validate(): Boolean {
        return !(name == null || handler == null || requirements == null)
    }

    companion object {
        fun newInstance(): CustomChannelHandlerFactory {
            return CustomChannelHandlerFactory(null, null, null, null, false, java.util.ArrayList())
        }
    }
}


@Sharable
open class SharableChannelInboundHandlerAdapter : ChannelInboundHandlerAdapter()

@Sharable
open class SharableChannelOutboundHandlerAdapter : ChannelOutboundHandlerAdapter()


interface IChannelHandlerCustom {
    /**
     * @return the name of the channel handler
     */
    fun name(): String?

    /**
     * @return the channel handler to add
     */
    fun handler(): ChannelHandler?

    /**
     * @return what handler this needs to be added before
     */
    fun addBefore(): String?

    /**
     * @return what handler this needs to be added after
     */
    fun addAfter(): String?

    /**
     * If addBefore or addAfter are not null, this becomes redundant.
     * If false, will be added last
     *
     * @return if handler should be added first
     */
    fun first(): Boolean

    /**
     * @return what handlers need to exist for this to be added
     */
    fun requires(): Array<String?>?
}
