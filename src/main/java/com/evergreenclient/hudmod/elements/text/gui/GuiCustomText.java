/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.text.gui;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.elements.text.TextVariable;
import com.evergreenclient.hudmod.gui.elements.GuiScreenExt;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiCustomText extends GuiScreenExt {

    private final Element element;
    private GuiTextField text;

    private List<TextVariable> vars = new ArrayList<>();

    public GuiCustomText(Element element) {
        this.element = element;
    }

    @Override
    public void initGui() {
        this.text = new GuiTextField(0, mc.fontRendererObj, (width / 2) - 58, height / 4 + 2, 118, 18);
        this.text.setText(element.getDisplayString());
        this.text.setEnabled(false);
        this.text.setDisabledTextColour(-1);
        this.text.setEnableBackgroundDrawing(true);
        this.text.setFocused(false);

        this.buttonList.add(new GuiButtonExt(1, width / 2 - 60, getRow(6), 120, 20, "Save"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        text.drawTextBox();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:

                mc.displayGuiScreen(null);
                break;
        }
    }
}
