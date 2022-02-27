/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.mixins;

import dev.isxander.evergreenhud.EvergreenHUD;
import dev.isxander.evergreenhud.event.ClientDisconnectEvent;
import dev.isxander.evergreenhud.event.ClientJoinWorldEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
    private void callEvent(WorldClient worldClientIn, String loadingMessage, CallbackInfo ci) {
        if (worldClientIn != null) {
            EvergreenHUD.INSTANCE.getEventBus().post(ClientJoinWorldEvent.class, new ClientJoinWorldEvent(worldClientIn));
        } else {
            EvergreenHUD.INSTANCE.getEventBus().post(ClientDisconnectEvent.class, new ClientDisconnectEvent());
        }
    }
}
