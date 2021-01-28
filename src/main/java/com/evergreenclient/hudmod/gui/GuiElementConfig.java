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
import com.evergreenclient.hudmod.gui.elements.GuiScreenExt;
import com.evergreenclient.hudmod.gui.elements.GuiSliderExt;
import com.evergreenclient.hudmod.settings.Setting;
import com.evergreenclient.hudmod.settings.impl.*;
import com.evergreenclient.hudmod.utils.Alignment;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.evergreenclient.hudmod.utils.Alignment.*;

public class GuiElementConfig extends GuiScreenExt {

    protected final Element element;
    protected Element dragging = null;
    protected int xOff, yOff;

    protected final Map<Integer, Setting> customButtons = new HashMap<>();
    protected final List<GuiTextField> textFieldList = new ArrayList<>();

    public GuiElementConfig(Element element) {
        this.element = element;
    }

    @Override
    public void initGui() {
        addButtons();
    }

    protected void addButtons() {
        this.buttonList.clear();

        this.buttonList.add(new GuiButtonExt( 0, width / 2 + 1,      height - 20, 90, 20, "Finished"));
        this.buttonList.add(new GuiButtonExt( 1, width / 2 - 1 - 90, height - 20, 90, 20, "Reset"));

        this.buttonList.add(new GuiButtonExt( 2, left(),  getRow(0), 120, 20, "Enabled: "  + (element.isEnabled()    ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.buttonList.add(new GuiSliderExt( 3, right(), getRow(0), 120, 20, "Scale: ",     "%", 20, 200, element.getPosition().getScale() * 100f, false, true, this));
        this.buttonList.add(new GuiButtonExt( 4, left(),  getRow(1), 120, 20, "Brackets: " + (element.showBrackets() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.buttonList.add(new GuiButtonExt( 5, right(), getRow(1), 120, 20, "Shadow: "   + (element.renderShadow() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.buttonList.add(new GuiButtonExt( 6, left(),  getRow(2), 120, 20, "Title: "   + (element.showTitle()   ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        if (!element.canShowTitle()) {
            this.buttonList.get(6).enabled = false;
            this.buttonList.get(6).displayString = "Title: " + EnumChatFormatting.RED + "OFF";
            this.element.setTitle(false);
        }
        this.buttonList.add(new GuiSliderExt( 7, right(), getRow(2), 120, 20, "Text Red: ",   "", 0, 255, element.getTextColor().getRed(),   false, true, this));
        this.buttonList.add(new GuiSliderExt( 8, left(),  getRow(3), 120, 20, "Text Green: ", "", 0, 255, element.getTextColor().getGreen(), false, true, this));
        this.buttonList.add(new GuiSliderExt( 9, right(), getRow(3), 120, 20, "Text Blue: ",  "", 0, 255, element.getTextColor().getBlue(),  false, true, this));
        this.buttonList.add(new GuiSliderExt(10, left(),  getRow(4), 120, 20, "Background Red: ",   "", 0, 255, element.getBgColor().getRed(),   false, true, this));
        this.buttonList.add(new GuiSliderExt(11, right(), getRow(4), 120, 20, "Background Green: ", "", 0, 255, element.getBgColor().getGreen(), false, true, this));
        this.buttonList.add(new GuiSliderExt(12, left(),  getRow(5), 120, 20, "Background Blue: ",  "", 0, 255, element.getBgColor().getBlue(),  false, true, this));
        this.buttonList.add(new GuiSliderExt(13, right(), getRow(5), 120, 20, "Background Alpha: ", "", 0, 255, element.getBgColor().getAlpha(), false, true, this));
        this.buttonList.add(new GuiButtonExt(14, left(),  getRow(6), 120, 20, "Alignment: " + element.getAlignment().getName()));
        this.buttonList.add(new GuiButtonExt(15, right(), getRow(6), 120, 20, "Inverted: "  + (element.isInverted() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.buttonList.add(new GuiButtonExt(16, left(),  getRow(7), 120, 20, "Chroma: "    + (element.useChroma()  ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));

        int id = 17;
        int row = 7;
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
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        for (Element e : EvergreenHUD.getInstance().getElementManager().getElements())
            if (e.isEnabled()) e.render();
        GlStateManager.pushMatrix();
        float scale = 2;
        GlStateManager.scale(scale, scale, 0);
        drawCenteredString(mc.fontRendererObj, element.getMetadata().getName(), (int)(width / 2 / scale), (int)(5 / scale), -1);
        GlStateManager.popMatrix();
        drawCenteredString(mc.fontRendererObj, element.getMetadata().getDescription(), width / 2, 25, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (GuiTextField textField : textFieldList) {
            textField.drawTextBox();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                mc.displayGuiScreen(new GuiMain());
                break;
            case 1:
                element.resetSettings();
                addButtons();
                break;
            case 2:
                element.setEnabled(!element.isEnabled());
                button.displayString = "Enabled: " + (element.isEnabled() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
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
            case 14:
                Alignment alignment = element.getAlignment();
                ScaledResolution res = new ScaledResolution(mc);
                if (alignment == LEFT) {
                    alignment = CENTER;
                    element.getPosition().setRawX(element.getPosition().getRawX(res) - mc.fontRendererObj.getStringWidth(element.getDisplayString()) / 2, res);
                }
                else if (alignment == CENTER) {
                    alignment = RIGHT;
                    element.getPosition().setRawX(element.getPosition().getRawX(res) - mc.fontRendererObj.getStringWidth(element.getDisplayString()) / 2, res);
                }
                else if (alignment == RIGHT) {
                    alignment = LEFT;
                    element.getPosition().setRawX(element.getPosition().getRawX(res) + mc.fontRendererObj.getStringWidth(element.getDisplayString()), res);
                }
                element.setAlignment(alignment);
                button.displayString = "Alignment: " + element.getAlignment().getName();
                break;
            case 15:
                element.setInverted(!element.isInverted());
                button.displayString = "Inverted: " + (element.isInverted() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            case 16:
                element.setChroma(!element.useChroma());
                button.displayString = "Chroma: " + (element.useChroma() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            default:
                Setting s = customButtons.get(button.id);
                if (s instanceof BooleanSetting) {
                    BooleanSetting setting = (BooleanSetting) s;
                    setting.set(!setting.get());
                    button.displayString = setting.getName() + ": " + (setting.get() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                } else if (s instanceof ArraySetting) {
                    ArraySetting setting = (ArraySetting) s;
                    button.displayString = setting.getName() + ": " + setting.next();
                }
                break;
        }
    }

    @Override
    public void sliderUpdated(GuiSlider button) {
        switch (button.id) {
            case 3:
                element.getPosition().setScale((float) button.getValue() / 100f);
                break;
            case 7:
                element.setTextColor(new Color(button.getValueInt(), element.getTextColor().getGreen(), element.getTextColor().getBlue(), element.getTextColor().getAlpha()));
                break;
            case 8:
                element.setTextColor(new Color(element.getTextColor().getRed(), button.getValueInt(), element.getTextColor().getBlue(), element.getTextColor().getAlpha()));
                break;
            case 9:
                element.setTextColor(new Color(element.getTextColor().getRed(), element.getTextColor().getGreen(), button.getValueInt(), element.getTextColor().getAlpha()));
                break;
            case 10:
                element.setBgColor(new Color(button.getValueInt(), element.getBgColor().getGreen(), element.getBgColor().getBlue(), element.getTextColor().getAlpha()));
                break;
            case 11:
                element.setBgColor(new Color(element.getBgColor().getRed(), button.getValueInt(), element.getBgColor().getBlue(), element.getTextColor().getAlpha()));
                break;
            case 12:
                element.setBgColor(new Color(element.getBgColor().getRed(), element.getBgColor().getGreen(), button.getValueInt(), element.getTextColor().getAlpha()));
                break;
            case 13:
                element.setBgColor(new Color(element.getBgColor().getRed(), element.getBgColor().getGreen(), element.getBgColor().getBlue(), button.getValueInt()));
                break;
            default:
                Setting s = customButtons.get(button.id);
                if (s instanceof IntegerSetting) {
                    IntegerSetting setting = (IntegerSetting) s;
                    setting.set(button.getValueInt());
                } else if (s instanceof DoubleSetting) {
                    DoubleSetting setting = (DoubleSetting) s;
                    setting.set(button.getValue());
                }
                break;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (GuiTextField textField : textFieldList) {
            textField.textboxKeyTyped(typedChar, keyCode);
            Setting s = customButtons.get(textField.getId());
            if (s instanceof StringSetting) {
                StringSetting setting = (StringSetting) s;
                setting.set(textField.getText());
            }
        }
        super.keyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE)
            mc.displayGuiScreen(new GuiMain());
    }

    @Override
    public void onGuiClosed() {
        element.getConfig().save();
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        ScaledResolution res = new ScaledResolution(mc);
        if (dragging == null) {
            if (clickedMouseButton == 0) {
                for (Element e : EvergreenHUD.getInstance().getElementManager().getElements()) {
                    if (e.getHitbox().isMouseOver(mouseX, mouseY)) {
                        dragging = e;
                        xOff = mouseX - e.getPosition().getRawX(res);
                        yOff = mouseY - e.getPosition().getRawY(res);
                        break;
                    }
                }

            }
        }
        else {
            dragging.getPosition().setRawX(mouseX - xOff, res);
            dragging.getPosition().setRawY(mouseY - yOff, res);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = null;
        xOff = yOff = 0;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (GuiTextField textField : textFieldList) {
            textField.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
}
