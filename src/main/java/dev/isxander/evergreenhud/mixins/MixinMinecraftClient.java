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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
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
}
