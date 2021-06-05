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

package co.uk.isxander.evergreenhud.gui.screens.impl;

import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.gui.components.*;
import co.uk.isxander.evergreenhud.gui.screens.GuiScreenElements;
import co.uk.isxander.evergreenhud.settings.Setting;
import co.uk.isxander.evergreenhud.settings.impl.*;
import co.uk.isxander.xanderlib.utils.StringUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.*;

public class GuiElementConfig extends GuiScreenElements {

    public final Element element;

    protected final Map<Integer, Setting> customButtons = new HashMap<>();
    protected final List<BetterGuiTextField> textFieldList = new ArrayList<>();
    protected String currentCategory;
    protected CategoryScrollPane categoryScrollPane;

    public GuiElementConfig(Element element) {
        this.element = element;
        this.currentCategory = null;
    }

    @Override
    public void initGui() {
        super.initGui();

        List<String> categories = new ArrayList<>();
        for (Setting setting : element.getCustomSettings()) {
            if (!setting.isInternal() && !categories.contains(setting.getCategory())) {
                categories.add(setting.getCategory());
            }
        }
        //Collections.sort(categories);
        currentCategory = categories.get(0);
        this.categoryScrollPane = new CategoryScrollPane(width / 6, height, 0, height, 0, 20, width, height, categories, (category, id) -> {
            currentCategory = category;

            addButtons();
        });

        addButtons();
    }

    public void addButtons() {
        this.buttonList.clear();
        this.textFieldList.clear();

        this.buttonList.add(new GuiButtonAlt(0, width / 2, height - 20, 90, 20, "Finished"));
        this.buttonList.add(new GuiButtonAlt(1, width / 2 - 90, height - 20, 90, 20, "Reset"));

        int id = 2;
        int row = 0;
        for (Setting s : element.getCustomSettings()) {
            if (s.isInternal() || s.isDisabled())
                continue;

            if (currentCategory != null) {
                if (!s.getCategory().equalsIgnoreCase(currentCategory))
                    continue;
            }

            int x = (id % 2 == 0 ? left() : right());
            int y = getRow(row);
            if (s instanceof BooleanSetting) {
                BooleanSetting setting = (BooleanSetting) s;
                this.buttonList.add(new BetterGuiButton(id, x, y, 120, 20, setting.getName() + ": " + (setting.get() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF"), setting.getDescription()));
            } else if (s instanceof IntegerSetting) {
                IntegerSetting setting = (IntegerSetting) s;
                this.buttonList.add(new BetterGuiSlider(id, x, y, 120, 20, setting.getName() + ": ", setting.getSuffix(), setting.getMin(), setting.getMax(), setting.get(), false, true, (b) -> setting.set(b.getValueInt()), setting.getDescription()));
            } else if (s instanceof FloatSetting) {
                FloatSetting setting = (FloatSetting) s;
                this.buttonList.add(new BetterGuiSlider(id, x, y, 120, 20, setting.getName() + ": ", setting.getSuffix(), setting.getMin(), setting.getMax(), setting.get(), true, true, (b) -> setting.set((float) b.getValue()), setting.getDescription()));
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
                textInput.setMaxStringLength(256);
                textInput.setVisible(true);
                textInput.setCanLoseFocus(true);
                textInput.setFocused(false);
                textFieldList.add(textInput);
            } else if (s instanceof EnumSetting) {
                EnumSetting<?> setting = (EnumSetting<?>) s;
                this.buttonList.add(new BetterGuiButton(id, x, y, 120, 20, setting.getName() + ": " + StringUtils.capitalizeEnum(setting.get().name().replaceAll("_", " ")), setting.getDescription()));
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

        this.categoryScrollPane.drawScreen(mouseX, mouseY, partialTicks);

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
                } else if (s instanceof EnumSetting) {
                    EnumSetting<?> setting = (EnumSetting<?>) s;
                    setting.next();
                    button.displayString = setting.getName() + ": " + StringUtils.capitalizeEnum(setting.get().name().replaceAll("_", " "));
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
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (GuiTextField textField : textFieldList) {
            textField.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
}
