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

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GuiOldForge extends GuiErrorScreen {

    public GuiOldForge() {
        super(
                EnumChatFormatting.RED + EnumChatFormatting.BOLD.toString() + "EvergreenHUD has encountered an issue",
                "",
                "EvergreenHUD requires a newer forge version.",
                "Please update your forge installation to continue to use EvergreenHUD.",
                "",
                "You " + EnumChatFormatting.BOLD + "WILL " + EnumChatFormatting.RESET + "encounter unexpected crashes if you continue anyway."
        );
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height - 50, 200, 20, "Download Recommended Forge"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height - 28, 200, 20, "Continue Without Evergreen"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            try {
                Desktop.getDesktop().browse(new URI("https://files.minecraftforge.net/net/minecraftforge/forge/index_1.8.9.html"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (button.id == 1) {
            mc.displayGuiScreen(new GuiMainMenu());
        }
    }
}
