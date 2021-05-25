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

import co.uk.isxander.evergreenhud.gui.components.GuiButtonAlt;
import co.uk.isxander.evergreenhud.gui.screens.GuiScreenElements;
import co.uk.isxander.evergreenhud.elements.ElementManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiMainConfig extends GuiScreenElements {

    private final ElementManager manager;

    public GuiMainConfig(ElementManager manager) {
        this.manager = manager;
    }

    @Override
    public void initGui() {
        addButtons();
    }

    private void addButtons() {
        this.buttonList.clear();

        this.buttonList.add(new GuiButtonAlt(0, width / 2,      height - 20, 90, 20, "Finished"));
        this.buttonList.add(new GuiButtonAlt(1, width / 2 - 90, height - 20, 90, 20, "Reset"));

        this.buttonList.add(new GuiButtonAlt(2, left(), getRow(0), 242, 20, "Enabled: " + (manager.isEnabled() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.buttonList.add(new GuiButtonAlt(3, left(), getRow(1), 120, 20, "Show in Chat: " + (manager.doShowInChat() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.buttonList.add(new GuiButtonAlt(4, right(), getRow(1), 120, 20, "Show in Debug: " + (manager.doShowInDebug() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.buttonList.add(new GuiButtonAlt(5, left(), getRow(2), 120, 20, "Show Under Gui: " + (manager.isShowUnderGui() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GlStateManager.pushMatrix();
        float scale = 2f;
        GlStateManager.scale(scale, scale, 0f);
        drawCenteredString(mc.fontRendererObj, EnumChatFormatting.GREEN + "Configuration", (int)(width / 2 / scale), (int)(5 / scale), -1);
        GlStateManager.popMatrix();
        drawCenteredString(mc.fontRendererObj, "Configures the main settings for the mod.", width / 2, 25, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0:
                mc.displayGuiScreen(new GuiMain());
                break;
            case 1:
                manager.resetConfig();
                addButtons();
                break;
            case 2:
                manager.setEnabled(!manager.isEnabled());
                button.displayString = "Enabled: " + (manager.isEnabled() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            case 3:
                manager.setShowInChat(!manager.doShowInChat());
                button.displayString = "Show in Chat: " + (manager.doShowInChat() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            case 4:
                manager.setShowInDebug(!manager.doShowInDebug());
                button.displayString = "Show in Debug: " + (manager.doShowInDebug() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            case 5:
                manager.setShowUnderGui(!manager.isShowUnderGui());
                button.displayString = "Show Under Gui: " + (manager.isShowUnderGui() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE)
            mc.displayGuiScreen(new GuiMain());
    }

    @Override
    public void onGuiClosed() {
        manager.getMainConfig().save();
    }
}
