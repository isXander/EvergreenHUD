/*
 * Copyright (C) isXander [2019 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package co.uk.isxander.evergreenhud.mixins;

import co.uk.isxander.evergreenhud.elements.impl.ElementPotionHUD;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryEffectRenderer.class)
public abstract class MixinInventoryEffectRenderer {

    @Shadow
    private boolean hasActivePotionEffects;

    @Inject(method = "updateActivePotionEffects", at = @At("HEAD"), cancellable = true)
    protected void injectUpdateActivePotionEffects(CallbackInfo ci) {
        for (ElementPotionHUD potionHUD : ElementPotionHUD.PotionHUDTracker.INSTANCE.instances) {
            if (potionHUD.overwriteIER.get()) {
                hasActivePotionEffects = true;
                ci.cancel();
                break;
            }
        }
    }

    @Inject(method = "drawActivePotionEffects", at = @At("HEAD"), cancellable = true)
    private void injectDrawActivePotionEffects(CallbackInfo ci) {
        boolean cancel = false;

        for (ElementPotionHUD potionHUD : ElementPotionHUD.PotionHUDTracker.INSTANCE.instances) {
            if (potionHUD.overwriteIER.get()) {
                cancel = true;

                potionHUD.render(1F, 3);
            }
        }

        if (cancel)
            ci.cancel();
    }

}
