/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.gui;

import club.sk1er.mods.core.gui.notification.Notifications;
import com.evergreenclient.hudmod.EvergreenHUD;
import com.evergreenclient.hudmod.elements.Element;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiMain extends GuiScreen {

    private Element dragging = null;
    private int offX = 0, offY = 0;

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButtonExt(0, width / 2 + 1,      height - 20, 90, 20, "Config"));
        this.buttonList.add(new GuiButtonExt(1, width / 2 - 1 - 90, height - 20, 90, 20, "Positioning"));

        int column = 0;
        int element = 2;
        final int columnOffset = 61;
        int y = 50;
        for (Element e : EvergreenHUD.getInstance().getElementManager().getElements()) {
            if (y > height - 60) {
                column++;
                y = 50;
                for (GuiButton button : this.buttonList) {
                    if (button.id > 1) {
                        button.xPosition -= columnOffset * column;
                    }
                }

            }
            this.buttonList.add(new GuiButtonExt(element, width / 2 - 60 + (columnOffset * (column)), y, 120, 20, e.getMetadata().getName()));
            y += 22;
            element++;
        }

        if (!EvergreenHUD.getInstance().getElementManager().isEnabled())
            Notifications.INSTANCE.pushNotification("EvergreenHUD", "The mod is disabled. You will not see the hud in-game unless you enable it.");

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        for (Element e : EvergreenHUD.getInstance().getElementManager().getElements())
            if (e.isEnabled()) e.render();
        GlStateManager.pushMatrix();
        float scale = 2;
        GlStateManager.scale(scale, scale, 0);
        drawCenteredString(mc.fontRendererObj, EnumChatFormatting.GREEN + "EvergreenHUD " + EvergreenHUD.VERSION, (int)(width / 2 / scale), (int)(5 / scale), -1);
        GlStateManager.popMatrix();
        drawCenteredString(mc.fontRendererObj, (EvergreenHUD.getInstance().isDevelopment() ? "Development" : "evergreenclient.com"), width / 2, 25, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                mc.displayGuiScreen(new GuiMainConfig(EvergreenHUD.getInstance().getElementManager()));
                break;
            case 1:
                mc.displayGuiScreen(new GuiElementPosition());
                break;
            default:
                mc.displayGuiScreen(new GuiElementConfig(EvergreenHUD.getInstance().getElementManager().getElements().get(button.id - 2)));
                break;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE)
            mc.displayGuiScreen(null);
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
        EvergreenHUD.getInstance().getElementManager().saveAll();
    }
}
