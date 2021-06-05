/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/Evergreen-Client/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.type.SimpleTextElement;
import co.uk.isxander.evergreenhud.settings.impl.BooleanSetting;
import co.uk.isxander.evergreenhud.settings.impl.IntegerSetting;
import co.uk.isxander.evergreenhud.elements.ElementData;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.HashSet;
import java.util.Set;

public class ElementBlockAbove extends SimpleTextElement {

    private static final Set<Block> EXEMPT_BLOCKS = Sets.newHashSet(Blocks.air, Blocks.water, Blocks.lava, Blocks.ladder, Blocks.vine, Blocks.wall_sign, Blocks.wall_sign, Blocks.standing_sign, Blocks.standing_banner);
    private int blockDistance = 0;

    public BooleanSetting notify;
    public IntegerSetting notifyHeight;
    public IntegerSetting checkAmount;

    @Override
    public void initialise() {
        addSettings(notify = new BooleanSetting("Notify", "Sound", "Make a noise when the block gets too close.", false));
        addSettings(notifyHeight = new IntegerSetting("Notify Height", "Functionality", "How close the block needs to be before notifying.", 3, 1, 10, " blocks"));
        addSettings(checkAmount = new IntegerSetting("Check Amount", "Functionality", "How many blocks the element measures before stopping. (Turning this to a high value may cause lag.)", 10, 1, 30, " blocks"));
    }

    @Override
    public ElementData metadata() {
        return new ElementData("Block Above", "Tells you if there is a block above your head. Useful for games like bedwars.", "Advanced");
    }

    @Override
    protected String getValue() {
        return Integer.toString(blockDistance);
    }

    @Override
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (mc.theWorld == null) return;
        if (event.entity != mc.thePlayer) return;

        boolean above = false;
        for (int i = 1; i < checkAmount.get() + 1; i++) {
            BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1 + i, mc.thePlayer.posZ);
            if (blockPos.getY() > 255) break;

            IBlockState state;
            try {
                state = mc.theWorld.getBlockState(blockPos);
            } catch (NullPointerException e) {
                continue;
            }

            if (state == null) continue;
            Block b = state.getBlock();
            if (!EXEMPT_BLOCKS.contains(b)) {
                if (i <= notifyHeight.get() && (blockDistance > notifyHeight.get() || blockDistance == 0)) {
                    if (notify.get())
                        mc.thePlayer.playSound("random.orb", 0.1f, 0.5f);
                }
                blockDistance = i;
                above = true;
                break;
            }
        }
        if (!above)
            blockDistance = 0;
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Above";
    }
}

