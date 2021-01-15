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
import com.evergreenclient.hudmod.elements.ElementManager;
import com.evergreenclient.hudmod.gui.elements.GuiScreenExt;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiMainConfig extends GuiScreenExt {

    private final ElementManager manager;

    private Element dragging = null;
    private int offX = 0, offY = 0;

    public GuiMainConfig(ElementManager manager) {
        this.manager = manager;
    }

    @Override
    public void initGui() {
        addButtons();
    }

    private void addButtons() {
        this.buttonList.clear();

        this.buttonList.add(new GuiButtonExt(0, width / 2 + 1,      height - 20, 90, 20, "Finished"));
        this.buttonList.add(new GuiButtonExt(1, width / 2 - 90 - 1, height - 20, 90, 20, "Reset"));

        this.buttonList.add(new GuiButtonExt(2, left(),  getRow(0), 242, 20, "Enabled: "       + (manager.isEnabled()     ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.buttonList.add(new GuiButtonExt(3, left(),  getRow(1), 120, 20, "Show in Chat: "  + (manager.doShowInChat()  ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.buttonList.add(new GuiButtonExt(4, right(), getRow(1), 120, 20, "Show in Debug: " + (manager.doShowInDebug() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.buttonList.add(new GuiButtonExt(5, left(),  getRow(2), 120, 20, "Colors in Gui: " + (manager.doColorsInGui() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        for (Element e : manager.getElements())
            if (e.isEnabled()) e.render();
        GlStateManager.pushMatrix();
        float scale = 2f;
        GlStateManager.scale(scale, scale, 0f);
        drawCenteredString(mc.fontRendererObj, "Configuration", (int)(width / 2 / scale), (int)(5 / scale), -1);
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
                manager.setColorsInGui(!manager.doColorsInGui());
                button.displayString = "Colors in Gui: " + (manager.doColorsInGui() ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE)
            mc.displayGuiScreen(new GuiMain());
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        ScaledResolution res = new ScaledResolution(mc);
        if (dragging == null) {
            if (clickedMouseButton == 0) {
                for (Element e : EvergreenHUD.getInstance().getElementManager().getElements()) {
                    if (e.getHitbox().isMouseOver(mouseX, mouseY)) {
                        dragging = e;
                        offX = mouseX - e.getPosition().getRawX(res);
                        offY = mouseY - e.getPosition().getRawY(res);
                        break;
                    }
                }
            }
        }
        else {
            dragging.getPosition().setRawX(mouseX - offX, res);
            dragging.getPosition().setRawY(mouseY - offY, res);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = null;
        offX = offY = 0;
    }

    @Override
    public void onGuiClosed() {
        manager.getConfig().save();
    }
}
