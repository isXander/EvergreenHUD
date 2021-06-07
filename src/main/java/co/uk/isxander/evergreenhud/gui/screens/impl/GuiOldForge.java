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

import club.sk1er.mods.core.util.ModCoreDesktop;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.EnumChatFormatting;

import java.net.URI;
import java.net.URISyntaxException;

public class GuiOldForge extends GuiMessageScreen {

    public GuiOldForge() {
        super(
                EnumChatFormatting.RED + EnumChatFormatting.BOLD.toString() + "EvergreenHUD has encountered an issue!",
                "",
                "EvergreenHUD requires a newer forge version.",
                "Please update your forge installation to continue to use EvergreenHUD.",
                "",
                "It is " + EnumChatFormatting.BOLD + "ESSENTIAL " + EnumChatFormatting.RESET + "that EvergreenHUD should run on the latest version.",
                "If you should choose to not update and ignore this warning,",
                "expect random crashes with other modern mods!"
        );
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height - 50, 200, 20, "Download Recommended Forge"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height - 28, 200, 20, "Continue without EvergreenHUD"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            try {
                ModCoreDesktop.INSTANCE.browse(new URI("https://maven.minecraftforge.net/net/minecraftforge/forge/1.8.9-11.15.1.2318-1.8.9/forge-1.8.9-11.15.1.2318-1.8.9-installer.jar"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (button.id == 1) {
            mc.displayGuiScreen(new GuiMainMenu());
        }
    }
}
