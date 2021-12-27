/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.isxander.evergreenhud.EvergreenHUD;
import dev.isxander.evergreenhud.event.EventListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;endWrite()V", shift = At.Shift.BEFORE))
    private void render(boolean tick, CallbackInfo ci) {
        if (EvergreenHUD.INSTANCE.getPostInitialized()) {
            RenderSystem.enableTexture();
            RenderSystem.enableCull();
            EvergreenHUD.INSTANCE.getEventBus().postConsumer((listener) -> listener.onRenderTick(new MatrixStack(), MinecraftClient.getInstance().getTickDelta()));
        }
    }

    @Inject(method = "joinWorld", at = @At("RETURN"))
    private void onJoinWorld(ClientWorld world, CallbackInfo ci) {
        EvergreenHUD.INSTANCE.getEventBus().postConsumer((listener) -> listener.onClientJoinWorld(world));
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("HEAD"))
    private void onDisconnect(Screen screen, CallbackInfo ci) {
        EvergreenHUD.INSTANCE.getEventBus().postConsumer(EventListener::onClientDisconnect);
    }
}
