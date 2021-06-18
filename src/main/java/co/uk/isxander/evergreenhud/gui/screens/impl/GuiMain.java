/*
 * Copyright (C) isXander [2019 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package co.uk.isxander.evergreenhud.gui.screens.impl;

import club.sk1er.mods.core.gui.notification.Notifications;
import co.uk.isxander.evergreenhud.addon.AddonManager;
import co.uk.isxander.evergreenhud.gui.components.GuiButtonAlt;
import co.uk.isxander.evergreenhud.gui.screens.GuiScreenElements;
import co.uk.isxander.evergreenhud.EvergreenHUD;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiMain extends GuiScreenElements {

    public GuiMain(GuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initGui() {
        super.initGui();

        this.buttonList.add(new GuiButtonAlt(0, width / 2,      height - 20, 90, 20, "Config"));
        this.buttonList.add(new GuiButtonAlt(1, width / 2 - 90, height - 20, 90, 20, "Add"));
        this.buttonList.add(new GuiButtonAlt(2, width / 2 - 90, height - 40, 90, 20, "Convert"));
        this.buttonList.add(new GuiButtonAlt(3, width / 2, height - 40, 90, 20, "Move"));

        if (!EvergreenHUD.getInstance().getElementManager().isEnabled())
            Notifications.INSTANCE.pushNotification("EvergreenHUD", "The mod is disabled. You will not see the hud in-game unless you enable it.");

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GlStateManager.pushMatrix();
        float scale = 2;
        GlStateManager.scale(scale, scale, 0);
        drawCenteredString(mc.fontRendererObj, EnumChatFormatting.GREEN + "EvergreenHUD", (int)(width / 2 / scale), (int)(5 / scale), -1);
        GlStateManager.popMatrix();
        drawCenteredString(mc.fontRendererObj, EvergreenHUD.MOD_VERSION + "-" + EvergreenHUD.CHANNEL.jsonName.toUpperCase(), width / 2, 25, -1);

        if (dragging == null || !EvergreenHUD.getInstance().getElementManager().isHideComponentsOnElementDrag()) {
            mc.fontRendererObj.drawString("Addon Count: " + AddonManager.getInstance().addons.size(), 2, height - mc.fontRendererObj.FONT_HEIGHT - 2, -1, true);
            noElementsDrawScreen(mouseX, mouseY, partialTicks);
        }
        drawElements(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                mc.displayGuiScreen(new GuiMainConfig(EvergreenHUD.getInstance().getElementManager(), this));
                break;
            case 1:
                mc.displayGuiScreen(new GuiAddElement(this));
                break;
            case 2:
                mc.displayGuiScreen(new GuiConfigConverter(this));
                break;
            case 3:
                mc.displayGuiScreen(new GuiMoveElements(this));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE)
            mc.displayGuiScreen(getParentScreen());
    }

}
