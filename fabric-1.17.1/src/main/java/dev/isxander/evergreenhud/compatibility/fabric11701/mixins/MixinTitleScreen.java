package dev.isxander.evergreenhud.compatibility.fabric11701.mixins;

import dev.isxander.evergreenhud.compatibility.fabric11701.Main;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {

    @Inject(method = "render", at = @At("TAIL"))
    public void init(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Main.matrices = matrices;
        Main.INSTANCE.setPostInitialized(true);
    }

}
