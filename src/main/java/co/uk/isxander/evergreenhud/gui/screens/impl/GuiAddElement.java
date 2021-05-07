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

package co.uk.isxander.evergreenhud.gui.screens.impl;

import co.uk.isxander.evergreenhud.gui.elements.GuiButtonAlt;
import co.uk.isxander.evergreenhud.gui.screens.GuiScreenElements;
import co.uk.isxander.evergreenhud.EvergreenHUD;
import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.elements.ElementType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GuiAddElement extends GuiScreenElements {

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButtonAlt(0, width / 2 - 90 - 1, height - 20, 182, 20, "Back"));

        final int startY = 50;
        final int buttonGap = 2;
        final int buttonWidth = 120;
        final int buttonHeight = 20;
        final int startButtonIndex = 1;
        int column = 0;
        int row = 0;
        int index = 1;

        List<Element> elements = new ArrayList<>();
        ElementType.instance.getTypes().forEach((name, elementClass) -> {
            elements.add(ElementType.instance.getElement(name));
        });
        elements.sort(Comparator.comparing(o -> o.getMetadata().getName()));

        for (Element e : elements) {
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

    private static class ElementButton extends GuiButtonAlt {

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
