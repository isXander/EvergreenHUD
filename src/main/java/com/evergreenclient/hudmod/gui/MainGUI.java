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
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class MainGUI extends GuiScreen {

    private Element dragging = null;
    private int offX = 0, offY = 0;

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButtonExt(0, width / 2 + 1,       height - 20, 90, 20, "Finished"));
        this.buttonList.add(new GuiButtonExt(1, width / 2 - 1 - 90, height - 20, 90, 20, "Reset"));
//        int columnCount = 0;
//        int elementCount = 1;
//        for (Element e : EvergreenHUD.getInstance().getElementManager().getElements()) {
//            int y = 30 + ((22 * elementCount) - ((height - 60) * columnCount));
//            if (y > height - 60)
//                columnCount++;
//            this.buttonList.add(new GuiButtonExt(elementCount + 1, (width / 2 - 40) * (columnCount + 1), y, 80, 20, e.getMetadata().getName()));
//            elementCount++;
//        }



        int column = 0;
        int element = 1;
        final int columnOffset = 60;
        for (Element e : EvergreenHUD.getInstance().getElementManager().getElements()) {
            int y = 30 + ((22 * element) - ((height - 108) * column));
            if (y > height - 60) {
                column++;
                y = 30 + ((22 * element) - ((height - 108) * column));
                for (GuiButton button : this.buttonList) {
                    if (button.id > 1)
                        button.xPosition -= columnOffset * column + column;
                }
            }
            this.buttonList.add(new GuiButtonExt(element + 1, width / 2 - 60 + (columnOffset * column), y, 120, 20, e.getMetadata().getName()));
            element++;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        for (Element e : EvergreenHUD.getInstance().getElementManager().getElements())
            if (e.isEnabled()) e.render();
        GlStateManager.pushMatrix();
        float scale = 2;
        GlStateManager.scale(scale, scale, 0);
        drawCenteredString(mc.fontRendererObj, EnumChatFormatting.GREEN + "Evergreen" + EnumChatFormatting.RESET + "HUD", (int)(width / 2 / scale), (int)(5 / scale), -1);
        GlStateManager.popMatrix();
        drawCenteredString(mc.fontRendererObj, EvergreenHUD.VERSION, width / 2, 25, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                mc.displayGuiScreen(null);
                break;
            case 1:
                EvergreenHUD.getInstance().getElementManager().resetAll();
                break;
            default:
                mc.displayGuiScreen(new ElementGUI(EvergreenHUD.getInstance().getElementManager().getElements().get(button.id - 2)));
                break;
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

        if (dragging == null) {
            if (clickedMouseButton == 0) {
                for (Element e : EvergreenHUD.getInstance().getElementManager().getElements()) {
                    if (e.getHitbox().isMouseOver(mouseX, mouseY)) {
                        dragging = e;
                        offX = mouseX - e.getPosition().x;
                        offY = mouseY - e.getPosition().y;
                        break;
                    }
                }
            }
        }
        else {
            dragging.getPosition().x = mouseX - offX;
            dragging.getPosition().y = mouseY - offY;
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
