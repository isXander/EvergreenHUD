package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.evergreenhud.ClientDamageEntityEvent
import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Text
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent
import cc.polyfrost.oneconfig.events.event.Stage
import cc.polyfrost.oneconfig.events.event.TickEvent
import cc.polyfrost.oneconfig.hud.SingleTextHud
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import cc.polyfrost.oneconfig.utils.dsl.mc
import net.minecraft.network.play.server.S19PacketEntityStatus


class Combo: Config(Mod("Combo", ModType.HUD), "evergreenhud/combo.json", false) {
    @HUD(name = "Main")
    var hud = ComboHud()

    init {
        initialize()
    }

    class ComboHud: SingleTextHud("Combo", true, 0, 70) {
        @Slider(
            name = "Discard Time",
            min = 0F,
            max = 10F
        )
        var discardTime = 3

        @Text(
            name = "No Hit Message"
        )
        var noHitMessage = "0"

        @Transient private var sentAttackTime = 0L
        @Transient private var lastHitTime = 0L
        @Transient private var lastAttackId = 0
        @Transient private var currentCombo = 0
        @Transient private var sentAttack = 0

        init {
            EventManager.INSTANCE.register(this)
        }

        @Subscribe
        private fun onTick(event: TickEvent) {
            if (event.stage == Stage.START && System.currentTimeMillis() - lastHitTime >= discardTime * 1000L) {
                currentCombo = 0
            }
        }

        @Subscribe
        private fun onAttackEntity(event: ClientDamageEntityEvent) {
            if (event.attacker != mc.thePlayer) {
                return
            }
            sentAttack = event.target.entityId
            sentAttackTime = System.currentTimeMillis()
        }

        @Subscribe
        private fun onPacket(event: ReceivePacketEvent) {
            if (event.packet is S19PacketEntityStatus) {
                val packet = event.packet as S19PacketEntityStatus
                if (packet.opCode.toInt() != 2) {
                    return
                }
                val target = packet.getEntity(mc.theWorld) ?: return
                if (sentAttack != -1 && target.entityId == sentAttack) {
                    sentAttack = -1
                    if (System.currentTimeMillis() - sentAttackTime > 2000L) {
                        sentAttackTime = 0L
                        currentCombo = 0
                        return
                    }
                    if (lastAttackId == target.entityId) {
                        currentCombo++
                    } else {
                        currentCombo = 1
                    }
                    lastHitTime = System.currentTimeMillis()
                    lastAttackId = target.entityId
                } else if (target.entityId == mc.thePlayer.entityId) {
                    currentCombo = 0
                }
            }
        }

        override fun getText(example: Boolean): String {
            if (currentCombo == 0) return noHitMessage
            return currentCombo.toString()
        }
    }
}