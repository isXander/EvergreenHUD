package cc.polyfrost.evergreenhud.mixins;

import cc.polyfrost.evergreenhud.ClientDamageEntityEvent;
import cc.polyfrost.oneconfig.events.EventManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public class EntityPlayerMixin {
    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At("HEAD"))
    private void onPlayerAttack(Entity targetEntity, CallbackInfo ci) {
        EventManager.INSTANCE.post(new ClientDamageEntityEvent((EntityPlayer) (Object) this, targetEntity));
    }
}
