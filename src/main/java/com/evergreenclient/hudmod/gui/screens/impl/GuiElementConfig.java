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

package com.evergreenclient.hudmod.gui.screens.impl;

import com.evergreenclient.hudmod.EvergreenHUD;
import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.gui.elements.BetterGuiButton;
import com.evergreenclient.hudmod.gui.elements.BetterGuiTextField;
import com.evergreenclient.hudmod.gui.elements.BetterGuiSlider;
import com.evergreenclient.hudmod.gui.screens.GuiScreenElements;
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

public class GuiElementConfig extends GuiScreenElements {

    public final Element element;

    protected final Map<Integer, Setting> customButtons = new HashMap<>();
    protected final List<BetterGuiTextField> textFieldList = new ArrayList<>();

    public GuiElementConfig(Element element) {
        this.element = element;
    }

    @Override
    public void initGui() {
        addButtons();
    }

    public void addButtons() {
        this.buttonList.clear();
        this.textFieldList.clear();

        this.buttonList.add(new GuiButtonExt(0, width / 2 + 1, height - 20, 90, 20, "Finished"));
        this.buttonList.add(new GuiButtonExt(1, width / 2 - 1 - 90, height - 20, 90, 20, "Reset"));

        this.buttonList.add(new BetterGuiSlider(2, left(), getRow(0), 120, 20, "Scale: ", "%", 20, 200, element.getPosition().getScale() * 100f, false, true, this, "Controls the size of the element."));
        this.buttonList.get(2).enabled = element.useScaleSetting();
        this.buttonList.add(new BetterGuiButton(3, right(), getRow(0), 120, 20, "Brackets: " + (element.showBrackets() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF"), "If there are square brackets before and after the text."));
        this.buttonList.get(3).enabled = element.useBracketsSetting();
        this.buttonList.add(new BetterGuiButton(4, left(), getRow(1), 120, 20, "Shadow: " + (element.renderShadow() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF"), "If the text has a shadow."));
        this.buttonList.get(4).enabled = element.useShadowSetting();
        this.buttonList.add(new BetterGuiButton(5, right(), getRow(1), 120, 20, "Title: " + (element.showTitle() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF"), "If the text has the element name in it."));
        this.buttonList.get(5).enabled = element.useTitleSetting();
        this.buttonList.add(new BetterGuiSlider(6, left(), getRow(2), 120, 20, "Text Red: ","", 0, 255, element.getTextColor().getRed(), false, true, this, "How much red is in the color of the text."));
        this.buttonList.get(6).enabled = element.useTextColorSetting();
        this.buttonList.add(new BetterGuiSlider(7, right(), getRow(2), 120, 20, "Text Green: ", "", 0, 255, element.getTextColor().getGreen(), false, true, this, "How much green is in the color of the text."));
        this.buttonList.get(7).enabled = element.useTextColorSetting();
        this.buttonList.add(new BetterGuiSlider(8, left(), getRow(3), 120, 20, "Text Blue: ",  "", 0, 255, element.getTextColor().getBlue(), false, true, this, "How much blue is in the color of the text."));
        this.buttonList.get(8).enabled = element.useTextColorSetting();
        this.buttonList.add(new BetterGuiSlider(9, right(), getRow(3), 120, 20, "Background Red: ", "", 0, 255, element.getBgColor().getRed(), false, true, this, "How much red is in the color of the background."));
        this.buttonList.get(9).enabled = element.useBgColorSetting();
        this.buttonList.add(new BetterGuiSlider(10, left(), getRow(4), 120, 20, "Background Green: ", "", 0, 255, element.getBgColor().getGreen(), false, true, this, "How much green is in the color of the background."));
        this.buttonList.get(10).enabled = element.useBgColorSetting();
        this.buttonList.add(new BetterGuiSlider(11, right(), getRow(4), 120, 20, "Background Blue: ", "", 0, 255, element.getBgColor().getBlue(), false, true, this, "How much blue is in the color of the background."));
        this.buttonList.get(11).enabled = element.useBgColorSetting();
        this.buttonList.add(new BetterGuiSlider(12, left(), getRow(5), 120, 20, "Background Alpha: ", "", 0, 255, element.getBgColor().getAlpha(), false, true, this, "How transparent the background is."));
        this.buttonList.get(12).enabled = element.useBgColorSetting();
        this.buttonList.add(new BetterGuiSlider(13, right(), getRow(5), 120, 20, "Padding Width: ", "", 0, 12, element.getPaddingWidth(), true, true, this, "How much extra width the background box will have."));
        this.buttonList.get(13).enabled = element.usePaddingSetting();
        this.buttonList.add(new BetterGuiSlider(14, left(), getRow(6), 120, 20, "Padding Height: ", "", 0, 12, element.getPaddingHeight(), true, true, this, "How much extra height the background box will have."));
        this.buttonList.get(14).enabled = element.usePaddingSetting();
        this.buttonList.add(new BetterGuiButton(15, right(), getRow(6), 120, 20, "Alignment: " + element.getAlignment().getName(), "When the text grows or shrinks in size, which way the element will move."));
        this.buttonList.get(15).enabled = element.useAlignmentSetting();
        this.buttonList.add(new BetterGuiButton(16, left(), getRow(7), 120, 20, "Inverted: " + (element.isInverted() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF"), "If the title is rendered after the value."));
        this.buttonList.get(16).enabled = element.useInvertedSetting();
        this.buttonList.add(new BetterGuiButton(17, right(), getRow(7), 120, 20, "Chroma: " + (element.useChroma() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF"), "If the color is chroma."));
        this.buttonList.get(17).enabled = element.useChromaSetting();

        int id = 18;
        int row = 8;
        for (Setting s : element.getCustomSettings()) {
            if (s.isInternal())
                continue;

            int x = (id % 2 == 0 ? left() : right());
            int y = getRow(row);
            if (s instanceof BooleanSetting) {
                BooleanSetting setting = (BooleanSetting) s;
                this.buttonList.add(new BetterGuiButton(id, x, y, 120, 20, setting.getName() + ": " + (setting.get() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF"), setting.getDescription()));
            } else if (s instanceof IntegerSetting) {
                IntegerSetting setting = (IntegerSetting) s;
                this.buttonList.add(new BetterGuiSlider(id, x, y, 120, 20, setting.getName() + ": ", setting.getSuffix(), setting.getMin(), setting.getMax(), setting.get(), false, true, this, setting.getDescription()));
            } else if (s instanceof DoubleSetting) {
                DoubleSetting setting = (DoubleSetting) s;
                this.buttonList.add(new BetterGuiSlider(id, x, y, 120, 20, setting.getName() + ": ", setting.getSuffix(), setting.getMin(), setting.getMax(), setting.get(), true, true, this, setting.getDescription()));
            } else if (s instanceof ArraySetting) {
                ArraySetting setting = (ArraySetting) s;
                this.buttonList.add(new BetterGuiButton(id, x, y, 120, 20, setting.getName() + ": " + setting.get(), setting.getDescription()));
            } else if (s instanceof ButtonSetting) {
                ButtonSetting setting = (ButtonSetting) s;
                this.buttonList.add(new BetterGuiButton(id, x, y, 120, 20, setting.getName(), setting.getDescription()));
            } else if (s instanceof StringSetting) {
                StringSetting setting = (StringSetting) s;
                BetterGuiTextField textInput = new BetterGuiTextField(id, mc.fontRendererObj, x + 1, y + 1, 120 - 2, 20 - 2);
                if (!setting.get().equals(setting.getName())) {
                    textInput.setText(setting.get());
                } else {
                    textInput.setText(setting.getName());
                }
                textInput.setDescription(setting.getName() + ": " + setting.getDescription());
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
        GlStateManager.pushMatrix();
        float scale = 2;
        GlStateManager.scale(scale, scale, 0);
        drawCenteredString(mc.fontRendererObj, EnumChatFormatting.GREEN + element.getMetadata().getName(), (int)(width / 2 / scale), (int)(5 / scale), -1);
        GlStateManager.popMatrix();
        drawCenteredString(mc.fontRendererObj, element.getMetadata().getDescription(), width / 2, 25, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (BetterGuiTextField textField : textFieldList) {
            textField.drawTextBox();
        }
        for (BetterGuiTextField textField : textFieldList) {
            textField.drawTextBoxDescription(mc, mouseX, mouseY);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                mc.displayGuiScreen(new GuiMain());
                break;
            case 1:
                element.resetSettings(true);
                addButtons();
                break;
            case 3:
                element.setBrackets(!element.showBrackets());
                button.displayString = "Brackets: " + (element.showBrackets() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            case 4:
                element.setShadow(!element.renderShadow());
                button.displayString = "Shadow: " + (element.renderShadow() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            case 5:
                element.setTitle(!element.showTitle());
                button.displayString = "Title: " + (element.showTitle() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            case 15:
                Alignment alignment = element.getAlignment();
                ScaledResolution res = new ScaledResolution(mc);
                if (alignment == LEFT) {
                    alignment = CENTER;
                    element.getPosition().setRawX(element.getPosition().getRawX(res) - mc.fontRendererObj.getStringWidth(element.getDisplayString()) / 2f, res);
                }
                else if (alignment == CENTER) {
                    alignment = RIGHT;
                    element.getPosition().setRawX(element.getPosition().getRawX(res) - mc.fontRendererObj.getStringWidth(element.getDisplayString()) / 2f, res);
                }
                else if (alignment == RIGHT) {
                    alignment = LEFT;
                    element.getPosition().setRawX(element.getPosition().getRawX(res) + mc.fontRendererObj.getStringWidth(element.getDisplayString()), res);
                }
                element.setAlignment(alignment);
                button.displayString = "Alignment: " + element.getAlignment().getName();
                break;
            case 16:
                element.setInverted(!element.isInverted());
                button.displayString = "Inverted: " + (element.isInverted() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            case 17:
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
                } else if (s instanceof ButtonSetting) {
                    ButtonSetting setting = (ButtonSetting) s;
                    setting.get().run();
                }
                break;
        }
    }

    @Override
    public void sliderUpdated(GuiSlider button) {
        switch (button.id) {
            case 2:
                element.getPosition().setScale((float) button.getValue() / 100f);
                break;
            case 6:
                element.setTextColor(new Color(button.getValueInt(), element.getTextColor().getGreen(), element.getTextColor().getBlue(), element.getTextColor().getAlpha()));
                break;
            case 7:
                element.setTextColor(new Color(element.getTextColor().getRed(), button.getValueInt(), element.getTextColor().getBlue(), element.getTextColor().getAlpha()));
                break;
            case 8:
                element.setTextColor(new Color(element.getTextColor().getRed(), element.getTextColor().getGreen(), button.getValueInt(), element.getTextColor().getAlpha()));
                break;
            case 9:
                element.setBgColor(new Color(button.getValueInt(), element.getBgColor().getGreen(), element.getBgColor().getBlue(), element.getBgColor().getAlpha()));
                break;
            case 10:
                element.setBgColor(new Color(element.getBgColor().getRed(), button.getValueInt(), element.getBgColor().getBlue(), element.getBgColor().getAlpha()));
                break;
            case 11:
                element.setBgColor(new Color(element.getBgColor().getRed(), element.getBgColor().getGreen(), button.getValueInt(), element.getBgColor().getAlpha()));
                break;
            case 12:
                element.setBgColor(new Color(element.getBgColor().getRed(), element.getBgColor().getGreen(), element.getBgColor().getBlue(), button.getValueInt()));
                break;
            case 13:
                element.setPaddingWidth((float)button.getValue());
                break;
            case 14:
                element.setPaddingHeight((float)button.getValue());
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
        super.onGuiClosed();
        EvergreenHUD.getInstance().getElementManager().getElementConfig().save();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (GuiTextField textField : textFieldList) {
            textField.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
}
