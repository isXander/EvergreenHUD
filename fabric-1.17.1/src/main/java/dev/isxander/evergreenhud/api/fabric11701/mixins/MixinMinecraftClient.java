/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.api.fabric11701.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.isxander.evergreenhud.EvergreenHUD;
import dev.isxander.evergreenhud.api.fabric11701.Main;
import dev.isxander.evergreenhud.event.RenderTickEvent;
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
        if (Main.INSTANCE.getPostInitialized()) {
            Main.matrices = new MatrixStack();
            RenderSystem.enableTexture();
            RenderSystem.enableCull();
            EvergreenHUD.INSTANCE.getEventBus().post(new RenderTickEvent(MinecraftClient.getInstance().getTickDelta()));
            Main.matrices = null;
        }
    }

}
