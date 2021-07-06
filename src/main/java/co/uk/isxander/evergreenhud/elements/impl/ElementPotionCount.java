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

package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.elements.type.SimpleTextElement;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ElementPotionCount extends SimpleTextElement {

    @Override
    protected ElementData metadata() {
        return new ElementData("Potion Count", "Similar to item count except all potions.", "Simple");
    }

    @Override
    protected String getValue() {
        if (mc.thePlayer == null) return "0";

        int count = 0;
        for (ItemStack stack : mc.thePlayer.inventory.mainInventory) {
            if (stack == null) continue;
            if (stack.getItem() instanceof ItemPotion)
                count += stack.stackSize;
        }
        return String.valueOf(count);
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Potions";
    }

}
