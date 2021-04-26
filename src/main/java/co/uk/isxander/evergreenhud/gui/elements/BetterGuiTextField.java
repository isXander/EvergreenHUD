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

package co.uk.isxander.evergreenhud.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.Collections;

public class BetterGuiTextField extends GuiTextField {

    private String description = "";

    public BetterGuiTextField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void drawTextBoxDescription(Minecraft mc, int mouseX, int mouseY) {
        super.drawTextBox();
        if (!description.trim().equals("") && mouseX >= this.xPosition && mouseX <= this.xPosition + this.width && mouseY >= this.yPosition && mouseY <= this.yPosition + this.height) {
            GuiUtils.drawHoveringText(Collections.singletonList(description), mouseX, mouseY, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, -1, Minecraft.getMinecraft().fontRendererObj);
            GlStateManager.disableLighting();
        }
    }
}
