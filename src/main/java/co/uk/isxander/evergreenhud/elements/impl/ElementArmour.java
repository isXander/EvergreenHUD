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

import co.uk.isxander.evergreenhud.elements.RenderOrigin;
import co.uk.isxander.evergreenhud.elements.type.TextElement;
import co.uk.isxander.evergreenhud.gui.screens.impl.GuiElementConfig;
import co.uk.isxander.evergreenhud.settings.impl.ArraySetting;
import co.uk.isxander.evergreenhud.settings.impl.BooleanSetting;
import co.uk.isxander.evergreenhud.settings.impl.IntegerSetting;
import co.uk.isxander.xanderlib.utils.GuiUtils;
import co.uk.isxander.xanderlib.utils.HitBox2D;
import co.uk.isxander.evergreenhud.settings.Setting;
import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.xanderlib.utils.Resolution;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElementArmour extends TextElement {

    public BooleanSetting helmet, chestplate, leggings, boots, item, showCount;
    public IntegerSetting spacing;
    public ArraySetting listType, textDisplay;

    private int width = 0, height = 0;

    @Override
    public void initialise() {
        addSettings(helmet = new BooleanSetting("Show Helmet", "Armour", true));
        addSettings(chestplate = new BooleanSetting("Show Chestplate", "Armour", true));
        addSettings(leggings = new BooleanSetting("Show Leggings", "Armour", true));
        addSettings(boots = new BooleanSetting("Show Boots", "Armour", true));
        addSettings(item = new BooleanSetting("Show Item", "Armour", true));
        addSettings(showCount = new BooleanSetting("Show Count", "Armour", true));
        addSettings(spacing = new IntegerSetting("Spacing", "Display", 5, 0, 10, ""));
        addSettings(listType = new ArraySetting("List Type", "Display", "Which way the list should expand if an item is added.", "Down", new String[]{"Down", "Up"}));
        addSettings(textDisplay = new ArraySetting("Text", "Display", "What information should be displayed next to the item.", "Durability", new String[]{"Durability", "Name", "None"}));
    }

    @Override
    public ElementData metadata() {
        return new ElementData("ArmourHUD", "Displays what you are wearing and holding.", "Advanced");
    }

    protected int getHeight() {
        return height;
    }

    @Override
    public GuiElementConfig getElementConfigGui(GuiScreen parent) {
        return new GuiElementConfig(this, parent) {

            @Override
            protected void actionPerformed(GuiButton button) {
                super.actionPerformed(button);
                Setting s = customButtons.get(button.id);
                if (s != null && s.equals(listType)) {
                    ScaledResolution res = Resolution.get();
                    if (listType.get().equalsIgnoreCase("up"))
                        getPosition().setRawY(getPosition().getRawY(res) + getHeight(), res);
                    else
                        getPosition().setRawY(getPosition().getRawY(res) - getHeight(), res);
                }
            }

        };
    }

    @Override
    public void render(float partialTicks, RenderOrigin origin) {
        ScaledResolution res = Resolution.get();

        super.render(partialTicks, origin);
        if (mc.thePlayer == null)
            return;

        ItemStack item = mc.thePlayer.inventory.getCurrentItem();
        ItemStack helmet = mc.thePlayer.inventory.armorItemInSlot(3);
        ItemStack chest = mc.thePlayer.inventory.armorItemInSlot(2);
        ItemStack legs = mc.thePlayer.inventory.armorItemInSlot(1);
        ItemStack boots = mc.thePlayer.inventory.armorItemInSlot(0);

        List<ItemStack> renderedStacks = new ArrayList<>();
        if (helmet != null && this.helmet.get()) renderedStacks.add(helmet);
        if (chest != null && this.chestplate.get()) renderedStacks.add(chest);
        if (legs != null && this.leggings.get()) renderedStacks.add(legs);
        if (boots != null && this.boots.get()) renderedStacks.add(boots);
        if (item != null && this.item.get()) renderedStacks.add(item);
        boolean up = listType.get().equalsIgnoreCase("up");
        if (up) {
            Collections.reverse(renderedStacks);
        }

        RenderItem itemRenderer = mc.getRenderItem();

        GlStateManager.pushMatrix();
        GlStateManager.scale(getPosition().getScale(), getPosition().getScale(), 1);

        int offset = 0;
        int index = 0;
        for (ItemStack stack : renderedStacks) {
            int x = (int) (getPosition().getRawX(res) / getPosition().getScale());
            int y = (int) (getPosition().getRawY(res) / getPosition().getScale());

            String text = "";
            if (this.textDisplay.get().equalsIgnoreCase("durability") && stack.isItemStackDamageable())
                text = Integer.toString(stack.getMaxDamage() - stack.getItemDamage());
            else if (this.textDisplay.get().equalsIgnoreCase("name"))
                text = stack.getDisplayName();
            float textX = 0, textY = 0;
            switch (this.getAlignmentSetting().get()) {
                case RIGHT:
                    textX = x - 2 - mc.fontRendererObj.getStringWidth(text);
                    textY = y + 5 - (mc.fontRendererObj.FONT_HEIGHT / 2f);

                    int newWidth = mc.fontRendererObj.getStringWidth(text) + 4 + 15;
                    if (newWidth > width)
                        width = newWidth;
                    break;
                case LEFT:
                    textX = x + 15 + 2;
                    textY = y + 5 - (mc.fontRendererObj.FONT_HEIGHT / 2f);

                    newWidth = mc.fontRendererObj.getStringWidth(text) + 4 + 15;
                    if (newWidth > width)
                        width = newWidth;
                    break;
                case CENTER:
                    textX = x + 8 - (mc.fontRendererObj.getStringWidth(text) / 2f);
                    textY = y + 12;

                    newWidth = mc.fontRendererObj.getStringWidth(text) + 4 + 15 + 2;
                    if (newWidth > width)
                        width = newWidth;
                    break;
            }
            textY += offset + 4;
            if (getAlignmentSetting().get() == Alignment.LEFT) {
                textX -= width / 2f;
            } else if (getAlignmentSetting().get() == Alignment.CENTER) {
                textX -= width;
            }
            GuiUtils.drawString(mc.fontRendererObj, text, textX, textY, getTextModeSetting().get() == TextMode.SHADOW, getTextModeSetting().get() == TextMode.BORDER, getChromaSetting().get(), false, getTextColor().getRGB());

            if (getAlignmentSetting().get() == Alignment.LEFT) {
                x -= width / 2f;
            } else if (getAlignmentSetting().get() == Alignment.CENTER) {
                x -= width;
            }

            //GlStateManager.color(1f, 1f, 1f, 1f);
            //GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();
            itemRenderer.zLevel = 200f;
            itemRenderer.renderItemAndEffectIntoGUI(stack, x, y + offset);
            String count = Integer.toString(stack.stackSize);
            if (!stack.isStackable() || !showCount.get() || stack.getItem() instanceof ItemArmor || (up ? index != 0 : index != 4))
                count = "";
            itemRenderer.renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, y + offset, count);
            RenderHelper.disableStandardItemLighting();

            if (listType.get().equalsIgnoreCase("down"))
                offset += 10 + spacing.get() + (this.getAlignmentSetting().get() == Alignment.CENTER ? mc.fontRendererObj.FONT_HEIGHT + 2 : 0);
            else
                offset -= 10 + spacing.get() + (this.getAlignmentSetting().get() == Alignment.CENTER ? mc.fontRendererObj.FONT_HEIGHT + 2 : 0);

            index++;
        }
        height = Math.abs(4 + offset + 2);

        //GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.popMatrix();
    }

    @Override
    public HitBox2D calculateHitBox(float gl, float sizeScale) {
        ScaledResolution res = Resolution.get();
        float x = getPosition().getRawX(res);
        float y = getPosition().getRawY(res);
        float extraWidth = getPaddingWidthSetting().get() * sizeScale;
        float extraHeight = getPaddingHeightSetting().get() * sizeScale;

        float hitX, hitY, hitW, hitH;
        hitX = hitY = hitW = hitH = 0;
        switch (getAlignmentSetting().get()) {
            case RIGHT:
                hitX = x - width;
                hitY = y;
                hitW = width;
                hitH = height;
                break;
            case CENTER:
                hitX = x - (width / 2f) - width + 20;
                hitY = y;
                hitW = width - 24;
                hitH = height - 4;
                break;
            case LEFT:
                hitX = x;
                hitY = y;
                hitW = width;
                hitH = height;
                break;
        }
        if (listType.get().equalsIgnoreCase("up"))
            hitY -= height;

        return new HitBox2D(hitX / gl - extraWidth, hitY / gl - extraHeight, hitW * sizeScale + (extraWidth * 2), hitH * sizeScale + (extraHeight * 2));
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "";
    }

}
