/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.impl;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.gui.GuiElementConfig;
import com.evergreenclient.hudmod.gui.elements.GuiSliderExt;
import com.evergreenclient.hudmod.settings.Setting;
import com.evergreenclient.hudmod.settings.impl.*;
import com.evergreenclient.hudmod.utils.Alignment;
import com.evergreenclient.hudmod.utils.element.ElementData;
import com.evergreenclient.hudmod.utils.gui.Hitbox;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElementArmour extends Element {

    public BooleanSetting helmet, chestplate, leggings, boots, item, showCount;
    public IntegerSetting spacing;
    public ArraySetting listType, textDisplay;

    private int width = 0, height = 0;

    @Override
    public void initialise() {
        addSettings(helmet = new BooleanSetting("Show Helmet", true));
        addSettings(chestplate = new BooleanSetting("Show Chestplate", true));
        addSettings(leggings = new BooleanSetting("Show Leggings", true));
        addSettings(boots = new BooleanSetting("Show Boots", true));
        addSettings(item = new BooleanSetting("Show Item", true));
        addSettings(showCount = new BooleanSetting("Show Count", true));
        addSettings(spacing = new IntegerSetting("Spacing", 5, 0, 10, ""));
        addSettings(listType = new ArraySetting("List Type", "Down", "Down", "Up"));
        addSettings(textDisplay = new ArraySetting("Text", "Durability", "Durability", "Name", "None"));
    }

    @Override
    public ElementData metadata() {
        return new ElementData("ArmourHUD", "Displays what you are wearing and holding.");
    }

    protected int getHeight() {
        return height;
    }

    @Override
    public GuiElementConfig getElementConfigGui() {
        return new GuiElementConfig(this) {

            @Override
            protected void addButtons() {
                this.buttonList.clear();

                this.buttonList.add(new GuiButtonExt( 0, width / 2 + 1,      height - 20, 90, 20, "Finished"));
                this.buttonList.add(new GuiButtonExt( 1, width / 2 - 1 - 90, height - 20, 90, 20, "Reset"));

                this.buttonList.add(new GuiButtonExt( 2, left(),  getRow(0), 120, 20, "Enabled: "  + (element.isEnabled()    ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
                this.buttonList.add(new GuiSliderExt( 3, right(), getRow(0), 120, 20, "Scale: ",     "%", 20, 200, element.getPosition().getScale() * 100f, false, true, this));
                this.buttonList.add(new GuiButtonExt( 5, left(), getRow(1), 120, 20, "Shadow: "   + (element.renderShadow() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
                this.buttonList.add(new GuiSliderExt( 7, right(), getRow(1), 120, 20, "Text Red: ",   "", 0, 255, element.getTextColor().getRed(),   false, true, this));
                this.buttonList.add(new GuiSliderExt( 8, left(),  getRow(2), 120, 20, "Text Green: ", "", 0, 255, element.getTextColor().getGreen(), false, true, this));
                this.buttonList.add(new GuiSliderExt( 9, right(), getRow(2), 120, 20, "Text Blue: ",  "", 0, 255, element.getTextColor().getBlue(),  false, true, this));
                this.buttonList.add(new GuiButtonExt(14, left(),  getRow(3), 120, 20, "Alignment: " + element.getAlignment().getName()));
                this.buttonList.add(new GuiButtonExt(16, right(),  getRow(3), 120, 20, "Chroma: "    + (element.useChroma()  ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));

                int id = 18;
                int row = 4;
                for (Setting s : element.getCustomSettings()) {
                    if (s instanceof BooleanSetting) {
                        BooleanSetting setting = (BooleanSetting) s;
                        this.buttonList.add(new GuiButtonExt(id, (id % 2 == 0 ? left() : right()), getRow(row), 120, 20, setting.getName() + ": " + (setting.get() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
                    } else if (s instanceof IntegerSetting) {
                        IntegerSetting setting = (IntegerSetting) s;
                        this.buttonList.add(new GuiSliderExt(id, (id % 2 == 0 ? left() : right()), getRow(row), 120, 20, setting.getName() + ": ", setting.getSuffix(), setting.getMin(), setting.getMax(), setting.get(), false, true, this));
                    } else if (s instanceof DoubleSetting) {
                        DoubleSetting setting = (DoubleSetting) s;
                        this.buttonList.add(new GuiSliderExt(id, (id % 2 == 0 ? left() : right()), getRow(row), 120, 20, setting.getName() + ": ", setting.getSuffix(), setting.getMin(), setting.getMax(), setting.get(), true, true, this));
                    } else if (s instanceof ArraySetting) {
                        ArraySetting setting = (ArraySetting) s;
                        this.buttonList.add(new GuiButtonExt(id, (id % 2 == 0 ? left() : right()), getRow(row), 120, 20, setting.getName() + ": " + setting.get()));
                    } else if (s instanceof StringSetting) {
                        StringSetting setting = (StringSetting) s;
                        GuiTextField textInput = new GuiTextField(id, mc.fontRendererObj, (id % 2 == 0 ? left() : right()) + 1, getRow(row) + 1, 120 - 2, 20 - 2);
                        if (!setting.get().equals(setting.getName()))
                            textInput.setText(setting.get());
                        else
                            textInput.setText(setting.getName());
                        textInput.setEnableBackgroundDrawing(true);
                        textInput.setMaxStringLength(120);
                        textInput.setVisible(true);
                        textInput.setEnableBackgroundDrawing(true);
                        textInput.setCanLoseFocus(true);
                        textInput.setFocused(false);
                        textFieldList.add(textInput);
                    }
                    customButtons.put(id, s);

                    id++;
                    if (id % 2 == 0)
                        row++;
                }
            }

            @Override
            protected void actionPerformed(GuiButton button) {
                super.actionPerformed(button);
                Setting s = customButtons.get(button.id);
                if (s != null && s.equals(listType)) {
                    ScaledResolution res = new ScaledResolution(mc);
                    if (listType.get().equalsIgnoreCase("up"))
                        getPosition().setRawY(getPosition().getRawY(res) + getHeight(), res);
                    else
                        getPosition().setRawY(getPosition().getRawY(res) - getHeight(), res);
                }
            }

        };
    }

    @Override
    public void render() {
        ScaledResolution res = new ScaledResolution(mc);

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
        if (listType.get().equalsIgnoreCase("up")) {
            Collections.reverse(renderedStacks);
        }

        RenderItem itemRenderer = mc.getRenderItem();

        GlStateManager.pushMatrix();
        GlStateManager.scale(getPosition().getScale(), getPosition().getScale(), 1);

        int offset = 0;
        for (ItemStack stack : renderedStacks) {
            int x = (int) (getPosition().getRawX(res) / getPosition().getScale());
            int y = (int) (getPosition().getRawY(res) / getPosition().getScale());

            String text = "";
            if (this.textDisplay.get().equalsIgnoreCase("durability") && stack.isItemStackDamageable())
                text = Integer.toString(stack.getMaxDamage() - stack.getItemDamage());
            else if (this.textDisplay.get().equalsIgnoreCase("name"))
                text = stack.getDisplayName();
            float textX = 0, textY = 0;
            switch (this.getAlignment()) {
                case LEFT:
                    textX = x - 2 - mc.fontRendererObj.getStringWidth(text);
                    textY = y + 5 - (mc.fontRendererObj.FONT_HEIGHT / 2f);

                    int newWidth = mc.fontRendererObj.getStringWidth(text) + 4 + 15;
                    if (newWidth > width)
                        width = newWidth;
                    break;
                case RIGHT:
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
            if (getAlignment() == Alignment.LEFT) {
                textX -= width / 2f;
            } else if (getAlignment() == Alignment.CENTER) {
                textX -= width;
            }
            if (this.useChroma()) {
                drawChromaString(text, textX, textY, renderShadow(), false);
            } else {
                mc.fontRendererObj.drawString(text, textX, textY, this.getTextColor().getRGB(), renderShadow());
            }
            if (getAlignment() == Alignment.LEFT) {
                x -= width / 2f;
            } else if (getAlignment() == Alignment.CENTER) {
                x -= width;
            }

            //GlStateManager.color(1f, 1f, 1f, 1f);
            //GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();
            itemRenderer.zLevel = 200f;
            itemRenderer.renderItemAndEffectIntoGUI(stack, x, y + offset);
            String count = Integer.toString(stack.stackSize);
            if (!stack.isStackable() || !showCount.get()) count = "";
            itemRenderer.renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, y + offset, count);
            RenderHelper.disableStandardItemLighting();

            if (listType.get().equalsIgnoreCase("down"))
                offset += 10 + spacing.get() + (this.getAlignment() == Alignment.CENTER ? mc.fontRendererObj.FONT_HEIGHT + 2 : 0);
            else
                offset -= 10 + spacing.get() + (this.getAlignment() == Alignment.CENTER ? mc.fontRendererObj.FONT_HEIGHT + 2 : 0);
        }
        height = Math.abs(4 + offset + 2);

        //GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.popMatrix();
    }

    @Override
    public Hitbox getHitbox() {
        int x = getPosition().getRawX(new ScaledResolution(mc));
        int y = getPosition().getRawY(new ScaledResolution(mc));

        int hitX, hitY, hitW, hitH;
        hitX = hitY = hitW = hitH = 0;
        switch (getAlignment()) {
            case LEFT:
                hitX = x - width - 4;
                hitY = y - 2;
                hitW = width + 8;
                hitH = height + 4;
                break;
            case CENTER:
                hitX = x - (width / 2) - width + 12;
                hitY = y - 4;
                hitW = width - 8;
                hitH = height - 2;
                break;
            case RIGHT:
                hitX = x - 4;
                hitY = y - 2;
                hitW = width + 8;
                hitH = height + 4;
                break;
        }
        if (listType.get().equalsIgnoreCase("up"))
            hitY -= height;

        return new Hitbox(hitX, hitY, hitW, hitH);
    }

    @Override
    public String getDisplayString() {
        return "";
    }

    @Override
    protected String getValue() {
        return "";
    }

    @Override
    public String getDisplayTitle() {
        return "";
    }

}
