package co.uk.isxander.evergreenhud.gui.screens.impl;

import club.sk1er.mods.core.gui.notification.Notifications;
import co.uk.isxander.evergreenhud.gui.screens.GuiScreenElements;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiMoveElements extends GuiScreenElements {

    @Override
    public void initGui() {
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
