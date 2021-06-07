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

package co.uk.isxander.evergreenhud;

import club.sk1er.mods.core.ModCore;
import club.sk1er.mods.core.gui.notification.Notifications;
import club.sk1er.mods.core.util.MinecraftUtils;
import club.sk1er.mods.core.util.ModCoreDesktop;
import club.sk1er.mods.core.util.Multithreading;
import co.uk.isxander.evergreenhud.addon.AddonManager;
import co.uk.isxander.evergreenhud.command.EvergreenHudCommand;
import co.uk.isxander.evergreenhud.config.ElementConfig;
import co.uk.isxander.evergreenhud.config.convert.impl.ChromaHudConverter;
import co.uk.isxander.evergreenhud.config.convert.impl.SimpleHudConverter;
import co.uk.isxander.evergreenhud.elements.ElementManager;
import co.uk.isxander.evergreenhud.gui.screens.impl.GuiMain;
import co.uk.isxander.evergreenhud.repo.BlacklistManager;
import co.uk.isxander.evergreenhud.repo.ReleaseChannel;
import co.uk.isxander.evergreenhud.repo.UpdateChecker;
import co.uk.isxander.xanderlib.XanderLib;
import co.uk.isxander.xanderlib.ui.editor.AbstractGuiModifier;
import co.uk.isxander.xanderlib.utils.Constants;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.net.URI;
import java.util.List;

@Mod(modid = EvergreenHUD.MOD_ID, name = EvergreenHUD.MOD_NAME, version = EvergreenHUD.MOD_VERSION, clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]")
public class EvergreenHUD implements Constants {

    public static final String MOD_ID = "evergreenhud";
    public static final String MOD_NAME = "EvergreenHUD";
    public static final String MOD_VERSION = "@BUILD_VER@";
    public static final String UPDATE_NAME = "the next step.";
    public static final ReleaseChannel CHANNEL = ReleaseChannel.BETA;

    public static final Logger LOGGER = LogManager.getLogger("EvergreenHUD");
    public static final File DATA_DIR = new File(mc.mcDataDir, "config/evergreenhud");
    private String version = null;

    @Mod.Instance(EvergreenHUD.MOD_ID)
    private static EvergreenHUD instance;

    private ElementManager elementManager;
    private boolean development;

    private boolean firstLaunch = false;
    private boolean versionTwoFirstLaunch = false;

    private boolean notify = false;
    private boolean reset = false;

    private final KeyBinding keybind = new KeyBinding("Open EvergreenHUD GUI", Keyboard.KEY_HOME, "EvergreenHUD");
    private boolean chromaHud = false;
    private boolean simpleHud = false;
    private boolean welcome = false;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModCore.getInstance().initialize(mc.mcDataDir);
        XanderLib.getInstance().initPhase();

        ProgressManager.ProgressBar progress = ProgressManager.push("EvergreenHUD", 9);

        progress.step("Blacklist Check");
        if (BlacklistManager.isVersionBlacklisted(MOD_VERSION)) {
            throw new RuntimeException("The current version of this mod has been blacklisted.\n"
                    + "Please check the discord server for updates.\n"
                    + "https://discord.gg/AJv5ZnNT8q");
        }

        progress.step("Forge Check");
        if (ForgeVersion.getBuildVersion() < 2318 && ForgeVersion.getBuildVersion() != 0) {
            throw new RuntimeException("The current version of forge is outdated and causes issues with EvergreenHUD.\n"
                    + "Please update to Forge 1.8.9 Version 2318.\n"
                    + "https://maven.minecraftforge.net/net/minecraftforge/forge/1.8.9-11.15.1.2318-1.8.9/forge-1.8.9-11.15.1.2318-1.8.9-installer.jar");
        }

        progress.step("Evergreen Check");
        firstLaunch = !DATA_DIR.exists();
        versionTwoFirstLaunch = !ElementConfig.CONFIG_FILE.exists();

        progress.step("Registering Hooks");
        ClientCommandHandler.instance.registerCommand(new EvergreenHudCommand());
        ClientRegistry.registerKeyBinding(keybind);
        XanderLib.getInstance().getGuiEditor().addModifier(GuiOptions.class, new AbstractGuiModifier() {
            @Override
            public void onInitGuiPost(GuiScreen screen, List<GuiButton> buttonList) {
                if (mc.theWorld != null)
                    buttonList.add(new GuiButton(991, screen.width / 2 + 5, screen.height / 6 + 24 - 6, 150, 20, "EvergreenHUD..."));
            }

            @Override
            public void onActionPerformedPost(GuiScreen screen, List<GuiButton> buttonList, GuiButton button) {
                if (button.id == 991) {
                    mc.displayGuiScreen(new GuiMain());
                }
            }
        });

        progress.step("Initialising Element Manager");
        MinecraftForge.EVENT_BUS.register(elementManager = new ElementManager());
        progress.step("Initialising Addons");
        AddonManager.getInstance().onInit();
        progress.step("Loading Main Config");
        elementManager.getMainConfig().load();
        progress.step("Loading Element Configs");
        elementManager.getElementConfig().load();
        AddonManager.getInstance().onConfigLoad();

        progress.step("Finishing Up");
        MinecraftForge.EVENT_BUS.register(this);

        if (isFirstLaunch() || isVersionTwoFirstLaunch()) {
            if (new File(ChromaHudConverter.DEFAULT_DIR, ChromaHudConverter.CONFIG_FILE).exists()) {
                chromaHud = true;
            }
            if (SimpleHudConverter.DEFAULT_DIR.exists()) {
                simpleHud = true;
            }
            welcome = true;
        }

        ProgressManager.pop(progress);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Multithreading.runAsync(() -> {
            if (MinecraftUtils.isDevelopment()) {
                LOGGER.warn("Running on non-public version. Skipped update check.");
                development = true;
            } else {
                version = UpdateChecker.getNeededVersion();
                if (!version.equalsIgnoreCase(EvergreenHUD.MOD_VERSION)) {
                    LOGGER.warn("Mod is out of date. " + EvergreenHUD.MOD_VERSION + " > " + version);
                    notify = true;
                }
            }
        });

        AddonManager.getInstance().onPostInit();
    }

    @Mod.EventHandler
    public void onFMLLoadComplete(FMLLoadCompleteEvent event) {

        if (chromaHud) {
            Notifications.INSTANCE.pushNotification("ChromaHUD Detected", "An existing ChromaHUD configuration has been detected. Would you like to convert it to EvergreenHUD?", () -> {
                new ChromaHudConverter(ChromaHudConverter.DEFAULT_DIR).process(EvergreenHUD.getInstance().getElementManager());

                return null;
            });
        }

        if (simpleHud) {
            Notifications.INSTANCE.pushNotification("SimpleHUD Detected", "An existing SimpleHUD configuration has been detected. Would you like to convert it to EvergreenHUD?", () -> {
                new SimpleHudConverter(SimpleHudConverter.DEFAULT_DIR).process(EvergreenHUD.getInstance().getElementManager());

                return null;
            });
        }

        if (welcome) {
            Notifications.INSTANCE.pushNotification("EvergreenHUD", "Use /evergreenhud to configure.");
        }

        if (notify) {
            notifyUpdate(version);
        }

        if (reset) {
            reset = false;
            Notifications.INSTANCE.pushNotification("EvergreenHUD", "The configuration has been reset due to a version change that makes your configuration incompatible with the current version.");
        }

    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (keybind.isPressed())
            mc.displayGuiScreen(new GuiMain());
    }

    public static void notifyUpdate(String latestVersion) {
        Notifications.INSTANCE.pushNotification("EvergreenHUD", "You are running an outdated version.\nCurrent: " + EvergreenHUD.MOD_VERSION + "\nLatest: " + latestVersion + "\n\nClick here to download.", () -> {
            try {
                ModCoreDesktop.INSTANCE.browse(new URI("https://short.evergreenclient.com/GlYH5z"));
            } catch (Exception e) {
                e.printStackTrace();
                Notifications.INSTANCE.pushNotification("EvergreenHUD", "Unfortunately, your computer does not seem to support web-browsing so the mod could not open the download page.\n\nPlease navigate to \"https://short.evergreenclient.com/GlYH5z\"" );
            }
            return null;
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

    public boolean isVersionTwoFirstLaunch() {
        return this.versionTwoFirstLaunch;
    }

}
