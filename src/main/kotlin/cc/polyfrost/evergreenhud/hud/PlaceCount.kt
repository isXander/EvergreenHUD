package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.evergreenhud.ClientPlaceBlockEvent
import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.Stage
import cc.polyfrost.oneconfig.events.event.TickEvent
import cc.polyfrost.oneconfig.hud.SingleTextHud
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import cc.polyfrost.oneconfig.utils.dsl.mc

class PlaceCount: Config(Mod("Block Place Count", ModType.HUD), "placecount.json") {
    @HUD(name = "Main")
    var hud = PlaceCountHud()

    init {
        initialize()
    }

    class PlaceCountHud : SingleTextHud("Blocks", false) {
        @Slider(
            name = "Interval",
            min = 500F,
            max = 3000F
        )
        var interval = 1000

        init {
            EventManager.INSTANCE.register(this)
        }

        private val blockCount = ArrayDeque<Long>()

        @Subscribe
        private fun onTick(event: TickEvent) {
            if (event.stage == Stage.START) {
                val currentTime = System.currentTimeMillis()
                if (!blockCount.isEmpty()) {
                    while ((currentTime - blockCount.first()) > interval) {
                        blockCount.removeFirst()
                        if (blockCount.isEmpty()) break
                    }
                }
            }
        }

        @Subscribe
        private fun onBlockPlace(event: ClientPlaceBlockEvent) {
            if (event.player == mc.thePlayer) {
                blockCount.addLast(System.currentTimeMillis())
            }
        }

        override fun getText(): String {
            return blockCount.size.toString()
        }

    }
}