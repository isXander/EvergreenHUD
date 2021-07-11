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

import co.uk.isxander.evergreenhud.EvergreenHUD;
import co.uk.isxander.evergreenhud.gui.components.GuiButtonAlt;
import co.uk.isxander.evergreenhud.gui.screens.GuiScreenElements;
import co.uk.isxander.evergreenhud.elements.ElementManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiMainConfig extends GuiScreenElements {

    private final ElementManager manager;

    public GuiMainConfig(ElementManager manager, GuiScreen parent) {
        super(parent);
        this.manager = manager;
    }

    @Override
    public void initGui() {
        super.initGui();

        addButtons();
    }

    private void addButtons() {
        this.buttonList.clear();

        this.buttonList.add(new GuiButtonAlt(0, width / 2,      height - 20, 90, 20, "Finished"));
        this.buttonList.add(new GuiButtonAlt(1, width / 2 - 90, height - 20, 90, 20, "Reset"));

        this.buttonList.add(new GuiButtonAlt(2, left(), getRow(0), 242, 20, (manager.isEnabled() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + "Enabled"));
        this.buttonList.add(new GuiButtonAlt(3, left(), getRow(1), 242, 20, (manager.isUseAlternateLook() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + "Alternate Look"));
        this.buttonList.add(new GuiButtonAlt(4, left(), getRow(2), 242, 20, (manager.isCheckForUpdates() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + "Check For Updates"));
        this.buttonList.add(new GuiButtonAlt(5, left(), getRow(3), 242, 20, (manager.isHideComponentsOnElementDrag() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + "Hide Components on Element Drag"));
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

        if (dragging == null || !EvergreenHUD.getInstance().getElementManager().isHideComponentsOnElementDrag()) {
            noElementsDrawScreen(mouseX, mouseY, partialTicks);
        }
        drawElements(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0:
                mc.displayGuiScreen(new GuiMain(this));
                break;
            case 1:
                manager.resetConfig();
                addButtons();
                break;
            case 2:
                manager.setEnabled(!manager.isEnabled());
                button.displayString = (manager.isEnabled() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + "Enabled";
                break;
            case 3:
                manager.setUseAlternateLook(!manager.isUseAlternateLook());
                button.displayString = (manager.isUseAlternateLook() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + "Alternate Look";
                break;
            case 4:
                manager.setCheckForUpdates(!manager.isCheckForUpdates());
                button.displayString = (manager.isCheckForUpdates() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + "Check For Updates";
                break;
            case 5:
                manager.setHideComponentsOnElementDrag(!manager.isHideComponentsOnElementDrag());
                button.displayString = (manager.isHideComponentsOnElementDrag() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + "Hide Components on Element Drag";
                break;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE)
            mc.displayGuiScreen(getParentScreen());
    }
}
