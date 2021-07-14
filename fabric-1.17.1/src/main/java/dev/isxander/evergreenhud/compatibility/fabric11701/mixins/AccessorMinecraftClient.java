package dev.isxander.evergreenhud.compatibility.fabric11701.mixins;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface AccessorMinecraftClient {

    @Accessor("currentFps")
    static int getFps() {
        throw new AssertionError();
    }

}
