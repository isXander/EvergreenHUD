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

package co.uk.isxander.evergreenhud.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.Collections;

public class BetterGuiButton extends GuiButtonAlt {

    public String description;

    public BetterGuiButton(int id, int xPos, int yPos, String displayString, String description) {
        super(id, xPos, yPos, displayString);
        this.description = description;
    }

    public BetterGuiButton(int id, int xPos, int yPos, int width, int height, String displayString, String description) {
        super(id, xPos, yPos, width, height, displayString);
        this.description = description;
    }

    public void drawButtonDescription(Minecraft mc, int mouseX, int mouseY) {
        if (enabled && !description.trim().equals("") && mouseX >= xPosition && mouseX <= xPosition + width && mouseY >= yPosition && mouseY <= yPosition + height) {
            GuiUtils.drawHoveringText(Collections.singletonList(description), mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, mc.fontRendererObj);
            GlStateManager.disableLighting();
        }
    }
}
