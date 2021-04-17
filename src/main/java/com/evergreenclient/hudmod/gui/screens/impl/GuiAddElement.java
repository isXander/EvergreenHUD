/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.gui.screens.impl;

import com.evergreenclient.hudmod.EvergreenHUD;
import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.elements.ElementManager;
import com.evergreenclient.hudmod.elements.ElementType;
import com.evergreenclient.hudmod.elements.impl.ElementText;
import com.evergreenclient.hudmod.gui.screens.GuiScreenElements;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GuiAddElement extends GuiScreenElements {

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButtonExt(0, width / 2 - 90 - 1, height - 20, 182, 20, "Back"));

        final int startY = 50;
        final int buttonGap = 2;
        final int buttonWidth = 120;
        final int buttonHeight = 20;
        final int startButtonIndex = 1;
        int column = 0;
        int row = 0;
        int index = 1;
        List<ElementType> elements = Arrays.stream(ElementType.values()).sorted(Comparator.comparing(o -> o.getElement().getMetadata().getName())).collect(Collectors.toList());
        for (ElementType type : elements) {
            Element e = type.getElement();
            if (startY + (row * buttonHeight + row * buttonGap) > height - 60 && index - 1 < elements.size()) {
                column++;
                row = 0;
                for (GuiButton button : this.buttonList) {
                    if (button instanceof ElementButton) {
                        button.xPosition -= (buttonWidth / 2) + buttonGap;
                    }
                }
            }

            int x = width / 2 + ((buttonWidth / 2) * column) - (buttonWidth / 2);
            int y = startY + (row * buttonHeight + row * buttonGap);

            this.buttonList.add(new ElementButton(startButtonIndex + index, x, y, buttonWidth, buttonHeight, e.getMetadata().getName(), e));

            row++;
            index++;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GlStateManager.pushMatrix();
        float scale = 2;
        GlStateManager.scale(scale, scale, 0);
        drawCenteredString(mc.fontRendererObj, EnumChatFormatting.GREEN + "Element Manager", (int)(width / 2 / scale), (int)(5 / scale), -1);
        GlStateManager.popMatrix();
        drawCenteredString(mc.fontRendererObj, "Manage all the elements in the mod!", width / 2, 25, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            mc.displayGuiScreen(new GuiMain());
        } else {
            if (button instanceof ElementButton) {
                ElementButton eb = (ElementButton) button;
                EvergreenHUD.getInstance().getElementManager().addElement(eb.getElement());
                mc.displayGuiScreen(eb.getElement().getElementConfigGui());
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE)
            mc.displayGuiScreen(new GuiMain());
    }

    private static class ElementButton extends GuiButtonExt {

        private final Element element;

        public ElementButton(int id, int xPos, int yPos, String displayString, Element element) {
            super(id, xPos, yPos, displayString);
            this.element = element;
        }

        public ElementButton(int id, int xPos, int yPos, int width, int height, String displayString, Element element) {
            super(id, xPos, yPos, width, height, displayString);
            this.element = element;
        }

        public Element getElement() {
            return this.element;
        }

    }

}
