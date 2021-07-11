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
import co.uk.isxander.evergreenhud.settings.impl.ArraySetting;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ElementItemCount extends SimpleTextElement {

    public ArraySetting item;

    private List<Item> items;

    @Override
    public void initialise() {
        items = new ArrayList<>();
        List<String> items = new ArrayList<>();
        Item.itemRegistry.forEach(item -> {
            items.add(I18n.format(item.getUnlocalizedName() + ".name"));
            this.items.add(item);
        });
        addSettings(item = new ArraySetting("Item", "Display", "The item to check the count of.", I18n.format(Items.arrow.getUnlocalizedName() + ".name"), items));
    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Item Count", "Display how many items you have.", "Simple");
    }

    @Override
    protected String getValue() {
        if (mc.thePlayer == null) return "0";

        int count = 0;
        for (ItemStack stack : mc.thePlayer.inventory.mainInventory) {
            if (stack == null) continue;
            if (stack.getItem().getRegistryName().equals(items.get(item.getIndex()).getRegistryName()))
                count += stack.stackSize;
        }
        return String.valueOf(count);
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Items";
    }
}
