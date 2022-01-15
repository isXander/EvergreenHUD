/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.isxander.evergreenhud.EvergreenHUD;
import dev.isxander.evergreenhud.event.ClientDisconnectEvent;
import dev.isxander.evergreenhud.event.ClientJoinWorldEvent;
import dev.isxander.evergreenhud.event.RenderTickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
    @Shadow public abstract float getTickDelta();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;endWrite()V", shift = At.Shift.BEFORE))
    private void render(boolean tick, CallbackInfo ci) {
        if (EvergreenHUD.INSTANCE.getPostInitialized()) {
            RenderSystem.enableTexture();
            RenderSystem.enableCull();
            EvergreenHUD.INSTANCE.getEventBus().post(RenderTickEvent.class, new RenderTickEvent(new MatrixStack(), getTickDelta()));
        }
    }

    @Inject(method = "joinWorld", at = @At("RETURN"))
    private void onJoinWorld(ClientWorld world, CallbackInfo ci) {
        EvergreenHUD.INSTANCE.getEventBus().post(ClientJoinWorldEvent.class, new ClientJoinWorldEvent(world));
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("HEAD"))
    private void onDisconnect(Screen screen, CallbackInfo ci) {
        EvergreenHUD.INSTANCE.getEventBus().post(ClientDisconnectEvent.class, new ClientDisconnectEvent());
    }
}
