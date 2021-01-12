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
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class PositioningGUI extends GuiScreen {

    private Element dragging = null;
    private int offX = 0, offY = 0;

    @Override
    public void initGui() {
        Notifications.INSTANCE.pushNotification("EvergreenHUD", "Press ESCAPE when you are finished.");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (Element e : EvergreenHUD.getInstance().getElementManager().getElements())
            if (e.isEnabled()) e.render();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(new MainGUI());
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        ScaledResolution res = new ScaledResolution(mc);
        if (dragging == null) {
            if (clickedMouseButton == 0) {
                for (Element e : EvergreenHUD.getInstance().getElementManager().getElements()) {
                    if (e.getHitbox().isMouseOver(mouseX, mouseY, e.getPosition().getScale())) {
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
