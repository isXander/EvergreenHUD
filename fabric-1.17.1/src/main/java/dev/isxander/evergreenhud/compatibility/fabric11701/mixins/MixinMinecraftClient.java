package dev.isxander.evergreenhud.compatibility.fabric11701.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.isxander.evergreenhud.EvergreenHUD;
import dev.isxander.evergreenhud.compatibility.fabric11701.Main;
import dev.isxander.evergreenhud.event.RenderTickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
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
            EvergreenHUD.INSTANCE.getEVENT_BUS().post(new RenderTickEvent(MinecraftClient.getInstance().getTickDelta()));
            Main.matrices = null;
        }
    }

}
