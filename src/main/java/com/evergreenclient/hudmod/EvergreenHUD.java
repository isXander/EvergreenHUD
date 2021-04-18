/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/Evergreen-Client/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package com.evergreenclient.hudmod;

import co.uk.isxander.xanderlib.XanderLib;
import co.uk.isxander.xanderlib.utils.Version;
import com.evergreenclient.hudmod.command.EvergreenHudCommand;
import com.evergreenclient.hudmod.elements.ElementManager;
import com.evergreenclient.hudmod.gui.screens.impl.GuiMain;
import com.evergreenclient.hudmod.update.UpdateChecker;
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
import java.io.File;
import java.net.URI;

@Mod(modid = EvergreenHUD.MOD_ID, name = EvergreenHUD.NAME, version = EvergreenHUD.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]")
public class EvergreenHUD {

    public static final String MOD_ID = "evergreenhud";
    public static final String NAME = "EvergreenHUD";
    public static final String VERSION = "2.0";
    public static final String UPDATE_NAME = "the next step.";

    public static final Version PARSED_VERSION = new Version(VERSION);
    public static final Logger LOGGER = LogManager.getLogger("EvergreenHUD");

    @Mod.Instance(EvergreenHUD.MOD_ID)
    private static EvergreenHUD instance;

    private ElementManager elementManager;
    private boolean development;
    private boolean firstLaunch = false;

    private boolean reset = false;

    private KeyBinding keybind = new KeyBinding("Open GUI", Keyboard.KEY_HOME, "Evergreen");

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        XanderLib.getInstance().initPhase();

        firstLaunch = !new File(Minecraft.getMinecraft().mcDataDir, "config/evergreenhud").exists();

        ClientCommandHandler.instance.registerCommand(new EvergreenHudCommand());
        ClientRegistry.registerKeyBinding(keybind);
        MinecraftForge.EVENT_BUS.register(elementManager = new ElementManager());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Version latestVersion = UpdateChecker.getLatestVersion();
        Version currentVersion = EvergreenHUD.PARSED_VERSION;
        if (latestVersion.newerThan(currentVersion)) {
            LOGGER.warn("Discovered new version: " + latestVersion + ". Current Version: " + currentVersion);
            notifyUpdate(latestVersion);
        } else if (!Version.sameVersion(latestVersion, currentVersion)) {
            LOGGER.warn("Running on Development Version");
            development = true;
        }

        if (reset) {
            reset = false;
            XanderLib.getInstance().getNotificationManager().push("EvergreenHUD", "The configuration has been reset due to a version change that makes your configuration incompatible with the current version.");
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (keybind.isPressed())
            Minecraft.getMinecraft().displayGuiScreen(new GuiMain());
    }

    public static void notifyUpdate(Version latestVersion) {
        XanderLib.getInstance().getNotificationManager().push("EvergreenHUD", "You are running an outdated version.\nCurrent: " + EvergreenHUD.PARSED_VERSION.toString() + "\nLatest: " + latestVersion.toString() + "\n\nClick here to download.", () -> {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("https://short.evergreenclient.com/GlYH5z"));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                XanderLib.getInstance().getNotificationManager().push("EvergreenHUD", "Unfortunately, your computer does not seem to support web-browsing so the mod could not open the download page.\n\nPlease navigate to \"https://short.evergreenclient.com/GlYH5z\"" );
            }
        });
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

    public boolean isFirstLaunch() {
        return this.firstLaunch;
    }

}
