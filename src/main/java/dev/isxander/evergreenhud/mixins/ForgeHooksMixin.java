/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.mixins;

import dev.isxander.evergreenhud.EvergreenHUD;
import dev.isxander.evergreenhud.event.ClientBreakBlockEvent;
import dev.isxander.evergreenhud.event.ClientPlaceBlockEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ForgeHooks.class, remap = false)
public class ForgeHooksMixin {
    @Inject(method = "onPlaceItemIntoWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;addStat(Lnet/minecraft/stats/StatBase;I)V", shift = At.Shift.AFTER, remap = true))
    private static void onPlaceBlock(ItemStack blocksnapshot, EntityPlayer updateFlag, World oldBlock, BlockPos newBlock, EnumFacing snap, float newMeta, float newSize, float newNBT, CallbackInfoReturnable<Boolean> cir) {
        EvergreenHUD.INSTANCE.getEventBus().post(ClientPlaceBlockEvent.class, new ClientPlaceBlockEvent(updateFlag, oldBlock));
    }

    @Inject(method = "onBlockBreakEvent", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/world/BlockEvent$BreakEvent;getExpToDrop()I", shift = At.Shift.AFTER))
    private static void onBreakBlock(World itemstack, WorldSettings.GameType packet, EntityPlayerMP pkt, BlockPos tileentity, CallbackInfoReturnable<Integer> cir) {
        EvergreenHUD.INSTANCE.getEventBus().post(ClientBreakBlockEvent.class, new ClientBreakBlockEvent(tileentity));
    }
}
