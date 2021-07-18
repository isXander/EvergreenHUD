package dev.isxander.evergreenhud.compatibility.fabric11701.mixins;

import dev.isxander.evergreenhud.EvergreenHUD;
import dev.isxander.evergreenhud.compatibility.fabric11701.Main;
import dev.isxander.evergreenhud.event.RenderHUDEvent;
import gg.essential.universal.UMatrixStack;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderMountHealth(Lnet/minecraft/client/util/math/MatrixStack;)V", shift = At.Shift.AFTER))
    public void render(MatrixStack matrices, float deltaTicks, CallbackInfo ci) {
        Main.matrices = matrices;
        EvergreenHUD.INSTANCE.getEVENT_BUS().post(new RenderHUDEvent(deltaTicks, new UMatrixStack()));
        // prevent use of MatrixStack outside of a render context
        Main.matrices = null;
    }

}
