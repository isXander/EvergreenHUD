package cc.polyfrost.evergreenhud.utils

import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.RenderEvent
import cc.polyfrost.oneconfig.events.event.Stage
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FrameTimeHelper : ReadOnlyProperty<Any?, MutableList<Double>> {
    private var lastTime = System.currentTimeMillis().toDouble()
    private val frameTimes = mutableListOf<Double>()

    init {
        EventManager.INSTANCE.register(this)
    }

    @Subscribe
    private fun onRenderTick(event: RenderEvent) {
        if (event.stage == Stage.END) {
            frameTimes += System.currentTimeMillis() - lastTime
            lastTime = System.currentTimeMillis().toDouble()
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>) = frameTimes
}