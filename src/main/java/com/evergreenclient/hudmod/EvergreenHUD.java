/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod;

import club.sk1er.mods.core.gui.notification.Notifications;
import com.evergreenclient.hudmod.command.EvergreenHudCommand;
import com.evergreenclient.hudmod.elements.ElementManager;
import com.evergreenclient.hudmod.forge.modcore.ModCoreInstaller;
import com.evergreenclient.hudmod.gui.GuiMain;
import com.evergreenclient.hudmod.update.UpdateChecker;
import com.evergreenclient.hudmod.utils.Version;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.net.URI;

@Mod(modid = EvergreenHUD.MOD_ID, name = EvergreenHUD.NAME, version = EvergreenHUD.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]")
public class EvergreenHUD {

    public static final String MOD_ID = "evergreenhud";
    public static final String NAME = "EvergreenHUD";
    public static final String VERSION = "1.2";

    @Mod.Instance(EvergreenHUD.MOD_ID)
    private static EvergreenHUD instance;

    private ElementManager elementManager;
    private boolean development;
    private Logger logger = LogManager.getLogger("EvergreenHUD");

    private boolean reset = false;

    private KeyBinding keybind = new KeyBinding("Open GUI", Keyboard.KEY_RSHIFT, "Evergreen");

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModCoreInstaller.initializeModCore(Minecraft.getMinecraft().mcDataDir);

        ClientCommandHandler.instance.registerCommand(new EvergreenHudCommand());
        ClientRegistry.registerKeyBinding(keybind);
        MinecraftForge.EVENT_BUS.register(elementManager = new ElementManager());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Version lastestVersion = UpdateChecker.getLatestVersion();
        Version currentVersion = new Version(EvergreenHUD.VERSION);
        if (lastestVersion.newerThan(currentVersion)) {
            logger.warn("Discovered new version: " + lastestVersion.toString() + ". Current Version: " + currentVersion.toString());
            notifyUpdate();
        } else if (!Version.sameVersion(lastestVersion, currentVersion)) {
            logger.warn("Running on Development Version");
            development = true;
        }

        if (reset) {
            reset = false;
            Notifications.INSTANCE.pushNotification("EvergreenHUD", "The configuration has been reset due to a version change that makes your configuration incompatible with the current version.");
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (keybind.isPressed())
            Minecraft.getMinecraft().displayGuiScreen(new GuiMain());
    }

    public static void notifyUpdate() {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Notifications.INSTANCE.pushNotification("EvergreenHUD", "There is an update available that you can download right now. Click here to download.", () -> {
                try {
                    Desktop.getDesktop().browse(new URI("https://short.evergreenclient.com/GlYH5z"));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });
        } else {
            Notifications.INSTANCE.pushNotification("EvergreenHUD", "There is an update available that you can download right now.");
        }
    }

    public static EvergreenHUD getInstance() {
        return instance;
    }

    public ElementManager getElementManager() {
        return elementManager;
    }

    public void notifyConfigReset() {
        reset = true;
    }

    public boolean isDevelopment() {
        return development;
    }

}
