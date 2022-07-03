package cc.polyfrost.evergreenhud.mixins;

import cc.polyfrost.evergreenhud.ClientPlaceBlockEvent;
import cc.polyfrost.oneconfig.events.EventManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ForgeHooks.class, remap = false)
public class ForgeHooksMixin {
    @Inject(method = "onPlaceItemIntoWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;addStat(Lnet/minecraft/stats/StatBase;I)V", shift = At.Shift.AFTER, remap = true))
    private static void onPlaceBlock(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir) {
        EventManager.INSTANCE.post(new ClientPlaceBlockEvent(player, world));
    }
}