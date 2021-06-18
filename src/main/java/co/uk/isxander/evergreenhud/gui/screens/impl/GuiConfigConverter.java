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
import co.uk.isxander.evergreenhud.config.convert.impl.ChromaHudConverter;
import co.uk.isxander.evergreenhud.config.convert.impl.LunarClientConverter;
import co.uk.isxander.evergreenhud.config.convert.impl.SimpleHudConverter;
import co.uk.isxander.evergreenhud.gui.components.GuiButtonAlt;
import co.uk.isxander.evergreenhud.gui.screens.GuiScreenElements;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

public class GuiConfigConverter extends GuiScreenElements {

    public GuiConfigConverter(GuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initGui() {
        super.initGui();

        this.buttonList.add(new GuiButtonAlt(0, width / 2 - 90 - 1, height - 20, 182, 20, "Back"));

        this.buttonList.add(new GuiButtonAlt(1, left(), getRow(0), 242, 20, "ChromaHUD"));
        this.buttonList.add(new GuiButtonAlt(2, left(), getRow(1), 242, 20, "SimpleHUD"));
        //this.buttonList.add(new GuiButtonAlt(3, left(), getRow(2), 242, 20, "Lunar Client"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                mc.displayGuiScreen(getParentScreen());
                break;
            case 1:
                new ChromaHudConverter(ChromaHudConverter.DEFAULT_DIR).process(EvergreenHUD.getInstance().getElementManager());
                break;
            case 2:
                new SimpleHudConverter(SimpleHudConverter.DEFAULT_DIR).process(EvergreenHUD.getInstance().getElementManager());
                break;
            case 3:
                new LunarClientConverter(LunarClientConverter.DEFAULT_DIR).process(EvergreenHUD.getInstance().getElementManager());
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GlStateManager.pushMatrix();
        float scale = 2;
        GlStateManager.scale(scale, scale, 0);
        drawCenteredString(mc.fontRendererObj, EnumChatFormatting.GREEN + "Config Converter", (int)(width / 2 / scale), (int)(5 / scale), -1);
        GlStateManager.popMatrix();
        drawCenteredString(mc.fontRendererObj, "Convert other HUD mod configs to EvergreenHUD!", width / 2, 25, -1);

        if (dragging == null || !EvergreenHUD.getInstance().getElementManager().isHideComponentsOnElementDrag()) {
            noElementsDrawScreen(mouseX, mouseY, partialTicks);
        }
        drawElements(mouseX, mouseY, partialTicks);
    }

}
