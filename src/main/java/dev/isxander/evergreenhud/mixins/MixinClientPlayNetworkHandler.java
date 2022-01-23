/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.mixins;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.isxander.evergreenhud.EvergreenHUD;
import dev.isxander.evergreenhud.event.ClientReceivedChatMessage;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @WrapWithCondition(
            method = "onGameMessage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;addChatMessage(Lnet/minecraft/network/MessageType;Lnet/minecraft/text/Text;Ljava/util/UUID;)V")
    )
    public boolean gameMessageIf(InGameHud instance, MessageType type, Text message, UUID sender) {
        return !EvergreenHUD.INSTANCE.getEventBus().post(ClientReceivedChatMessage.class, new ClientReceivedChatMessage(type, message, sender)).getCanceled();
    }
}
