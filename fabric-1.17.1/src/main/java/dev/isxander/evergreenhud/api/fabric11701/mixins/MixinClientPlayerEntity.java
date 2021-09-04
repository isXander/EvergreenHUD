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

import dev.isxander.evergreenhud.EvergreenHUD;
import dev.isxander.evergreenhud.api.fabric11701.impl.EntityImpl;
import dev.isxander.evergreenhud.event.ClientDamageEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity extends MixinPlayerEntity {

    @Override
    public void attack(Entity entity, CallbackInfo ci) {
        EvergreenHUD.INSTANCE.getEventBus().post(new ClientDamageEntity(new EntityImpl((ClientPlayerEntity) (Object) this), new EntityImpl(entity)));
    }
}
