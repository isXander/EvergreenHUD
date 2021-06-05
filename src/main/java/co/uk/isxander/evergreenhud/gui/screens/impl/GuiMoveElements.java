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
import co.uk.isxander.evergreenhud.gui.screens.GuiScreenElements;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiMoveElements extends GuiScreenElements {

    @Override
    public void initGui() {
        super.initGui();

        Notifications.INSTANCE.pushNotification("EvergreenHUD", "Press escape once you are finished!");
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(new GuiMain());
        }
    }
}
