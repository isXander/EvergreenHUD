/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.gui;

import com.evergreenclient.hudmod.EvergreenHUD;
import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.settings.Setting;
import com.evergreenclient.hudmod.settings.impl.BooleanSetting;
import com.evergreenclient.hudmod.settings.impl.DoubleSetting;
import com.evergreenclient.hudmod.settings.impl.IntegerSetting;
import com.evergreenclient.hudmod.utils.Alignment;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.evergreenclient.hudmod.utils.Alignment.*;

public class ElementGUI extends GuiScreen {

    private final Element element;
    private boolean dragging;
    private int xOff, yOff;

    private final Map<Integer, Setting> customButtons = new HashMap<>();

    public ElementGUI(Element element) {
        this.element = element;
    }

    @Override
    public void initGui() {
        addButtons();
    }

    private void addButtons() {
        this.buttonList.clear();

        this.buttonList.add(new GuiButtonExt( 0, width / 2 + 1,      height - 20, 90, 20, "Finished"));
        this.buttonList.add(new GuiButtonExt( 1, width / 2 - 1 - 90, height - 20, 90, 20, "Reset"));

        this.buttonList.add(new GuiButtonExt( 2, left(),  getRow(0), 120, 20, "Enabled: "  + (element.isEnabled()    ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.buttonList.add(new GuiSlider(    3, right(), getRow(0), 120, 20, "Scale: ",     "%", 20, 200, element.getPosition().scale * 100, false, true));
        this.buttonList.add(new GuiButtonExt( 4, left(),  getRow(1), 120, 20, "Brackets: " + (element.showBrackets() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.buttonList.add(new GuiButtonExt( 5, right(), getRow(1), 120, 20, "Shadow: "   + (element.renderShadow() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.buttonList.add(new GuiButtonExt( 6, left(),  getRow(2), 120, 20, "Title: "   + (element.showTitle()   ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.buttonList.add(new GuiSlider(    7, right(), getRow(2), 120, 20, "Text Red: ",   "", 0, 255, element.getTextColor().getRed(),   false, true));
        this.buttonList.add(new GuiSlider(    8, left(),  getRow(3), 120, 20, "Text Green: ", "", 0, 255, element.getTextColor().getGreen(), false, true));
        this.buttonList.add(new GuiSlider(    9, right(), getRow(3), 120, 20, "Text Blue: ",  "", 0, 255, element.getTextColor().getBlue(),  false, true));
        this.buttonList.add(new GuiSlider(   10, left(),  getRow(4), 120, 20, "Text Alpha: ", "", 0, 255, element.getTextColor().getBlue(),  false, true));
        this.buttonList.add(new GuiSlider(   11, right(), getRow(4), 120, 20, "Background Red: ",   "", 0, 255, element.getBgColor().getRed(),   false, true));
        this.buttonList.add(new GuiSlider(   12, left(),  getRow(5), 120, 20, "Background Green: ", "", 0, 255, element.getBgColor().getGreen(), false, true));
        this.buttonList.add(new GuiSlider(   13, right(), getRow(5), 120, 20, "Background Blue: ",  "", 0, 255, element.getBgColor().getBlue(),  false, true));
        this.buttonList.add(new GuiSlider(   14, left(),  getRow(6), 120, 20, "Background Alpha: ", "", 0, 255, element.getBgColor().getAlpha(), false, true));
        this.buttonList.add(new GuiButtonExt(15, right(), getRow(6), 120, 20, "Alignment: " + element.getAlignment().getName()));
        this.buttonList.add(new GuiButtonExt(16, left(),  getRow(7), 120, 20, "Inverted: " + (element.isInverted() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));

        int id = 17;
        int row = 7;
        for (Setting s : element.getCustomSettings()) {
            if (s instanceof BooleanSetting) {
                BooleanSetting setting = (BooleanSetting) s;
                this.buttonList.add(new GuiButtonExt(id, (id % 2 == 0 ? left() : right()), getRow(row), 120, 20, setting.getName() + ": " + (setting.get() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
            } else if (s instanceof IntegerSetting) {
                IntegerSetting setting = (IntegerSetting) s;
                this.buttonList.add(new GuiSlider(id, (id % 2 == 0 ? left() : right()), getRow(row), 120, 20, setting.getName() + ": ", setting.getSuffix(), setting.getMin(), setting.getMax(), setting.get(), false, true));
            } else if (s instanceof DoubleSetting) {
                DoubleSetting setting = (DoubleSetting) s;
                this.buttonList.add(new GuiSlider(id, (id % 2 == 0 ? left() : right()), getRow(row), 120, 20, setting.getName() + ": ", setting.getSuffix(), setting.getMin(), setting.getMax(), setting.get(), true, true));
            }
            customButtons.put(id, s);

            id++;
            if (id % 2 == 0)
                row++;
        }
    }

    private int getRow(int row) {
        return 40 + (row * 22);
    }

    private int left() {
        return width / 2 - 1 - 120;
    }

    private int right() {
        return width / 2 + 1;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        for (Element e : EvergreenHUD.getInstance().getElementManager().getElements())
            if (e.isEnabled()) e.render();
        GlStateManager.pushMatrix();
        float scale = 2;
        GlStateManager.scale(scale, scale, 0);
        drawCenteredString(mc.fontRendererObj, element.getMetadata().getName(), (int)(width / 2 / scale), (int)(5 / scale), -1);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        scale = 1f;
        GlStateManager.scale(scale, scale, 0);
        drawCenteredString(mc.fontRendererObj, element.getMetadata().getDescription(), (int)(width / 2 / scale), (int)(25 / scale), -1);
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                mc.displayGuiScreen(new MainGUI());
                break;
            case 1:
                element.resetSettings();
                addButtons();
                break;
            case 2:
                element.setEnabled(!element.isEnabled());
                button.displayString = "Enabled: " + (element.isEnabled() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            case 3:
                element.getPosition().scale = (float)((GuiSlider)(button)).getValue() / 100;
                break;
            case 4:
                element.setBrackets(!element.showBrackets());
                button.displayString = "Brackets: " + (element.showBrackets() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            case 5:
                element.setShadow(!element.renderShadow());
                button.displayString = "Shadow: " + (element.renderShadow() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            case 6:
                element.setTitle(!element.showTitle());
                button.displayString = "Title: " + (element.showTitle() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            case 7:
                element.setTextColor(new Color(((GuiSlider)(button)).getValueInt(), element.getTextColor().getGreen(), element.getTextColor().getBlue(), element.getTextColor().getAlpha()));
                break;
            case 8:
                element.setTextColor(new Color(element.getTextColor().getRed(), ((GuiSlider)(button)).getValueInt(), element.getTextColor().getBlue(), element.getTextColor().getAlpha()));
                break;
            case 9:
                element.setTextColor(new Color(element.getTextColor().getRed(), element.getTextColor().getGreen(), ((GuiSlider)(button)).getValueInt(), element.getTextColor().getAlpha()));
                break;
            case 10:
                element.setTextColor(new Color(element.getTextColor().getRed(), element.getTextColor().getGreen(), element.getTextColor().getBlue(), ((GuiSlider)(button)).getValueInt()));
                break;
            case 11:
                element.setBgColor(new Color(((GuiSlider)(button)).getValueInt(), element.getBgColor().getGreen(), element.getBgColor().getBlue(), element.getTextColor().getAlpha()));
                break;
            case 12:
                element.setBgColor(new Color(element.getBgColor().getRed(), ((GuiSlider)(button)).getValueInt(), element.getBgColor().getBlue(), element.getTextColor().getAlpha()));
                break;
            case 13:
                element.setBgColor(new Color(element.getBgColor().getRed(), element.getBgColor().getGreen(), ((GuiSlider)(button)).getValueInt(), element.getTextColor().getAlpha()));
                break;
            case 14:
                element.setBgColor(new Color(element.getBgColor().getRed(), element.getBgColor().getGreen(), element.getBgColor().getBlue(), ((GuiSlider)(button)).getValueInt()));
                break;
            case 15:
                Alignment alignment = element.getAlignment();
                if (alignment == LEFT) {
                    alignment = CENTER;
                    element.getPosition().x -= mc.fontRendererObj.getStringWidth(element.getDisplayString()) / 2;
                }
                else if (alignment == CENTER) {
                    alignment = RIGHT;
                    element.getPosition().x -= mc.fontRendererObj.getStringWidth(element.getDisplayString()) / 2;
                }
                else if (alignment == RIGHT) {
                    alignment = LEFT;
                    element.getPosition().x += mc.fontRendererObj.getStringWidth(element.getDisplayString());
                }
                element.setAlignment(alignment);
                button.displayString = "Alignment: " + element.getAlignment().getName();
                break;
            case 16:
                element.setInverted(!element.isInverted());
                button.displayString = "Inverted: " + (element.isInverted() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            default:
                Setting s = customButtons.get(button.id);
                if (s instanceof BooleanSetting) {
                    BooleanSetting setting = (BooleanSetting) s;
                    setting.set(!setting.get());
                    button.displayString = setting.get() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF";
                } else if (s instanceof IntegerSetting) {
                    IntegerSetting setting = (IntegerSetting) s;
                    setting.set(((GuiSlider)button).getValueInt());
                } else if (s instanceof DoubleSetting) {
                    DoubleSetting setting = (DoubleSetting) s;
                    setting.set(((GuiSlider)button).getValue());
                }
                break;
        }
    }

    @Override
    public void onGuiClosed() {
        element.getConfig().save();
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

        if (!dragging) {
            if (clickedMouseButton == 0) {
                if (element.getHitbox().isMouseOver(mouseX, mouseY)) {
                    dragging = true;
                    xOff = mouseX - element.getPosition().x;
                    yOff = mouseY - element.getPosition().y;
                }
            }
        }
        else {
            element.getPosition().x = mouseX - xOff;
            element.getPosition().y = mouseY - yOff;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
        xOff = yOff = 0;
    }
}
