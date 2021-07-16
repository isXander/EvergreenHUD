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

package dev.isxander.evergreenhud;

import dev.isxander.evergreenhud.addon.AddonManager;
import dev.isxander.evergreenhud.config.convert.impl.ChromaHudConverter;
import dev.isxander.evergreenhud.config.convert.impl.SimpleHudConverter;
import dev.isxander.evergreenhud.elements.ElementManager;
import dev.isxander.evergreenhud.gui.screens.impl.GuiConfigConverter;
import dev.isxander.evergreenhud.repo.BlacklistManager;
import dev.isxander.evergreenhud.gui.screens.impl.GuiOldForge;
import dev.isxander.evergreenhud.repo.ReleaseChannel;
import dev.isxander.xanderlib.XanderLib;
import dev.isxander.xanderlib.ui.editor.AbstractGuiModifier;
import static dev.isxander.xanderlib.utils.Constants.*;
import dev.isxander.evergreenhud.command.EvergreenHudCommand;
import dev.isxander.evergreenhud.config.ElementConfig;
import dev.isxander.evergreenhud.gui.screens.impl.GuiMain;
import dev.isxander.evergreenhud.repo.UpdateChecker;
import gg.essential.api.EssentialAPI;
import gg.essential.api.utils.Multithreading;
import gg.essential.universal.UDesktop;
import kotlin.Unit;
import lombok.Getter;
import net.minecraft.client.gui.*;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.net.URI;
import java.util.List;

@Mod(modid = EvergreenHUD.MOD_ID, name = EvergreenHUD.MOD_NAME, version = EvergreenHUD.MOD_REVISION, clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]", guiFactory = "dev.isxander.evergreenhud.forge.EvergreenGuiFactory")
public class EvergreenHUD {

    public static final String MOD_ID = "evergreenhud";
    public static final String MOD_NAME = "EvergreenHUD";
    public static final String MOD_VERSION = "@BUILD_VER_NORMAL@";
    public static final String MOD_REVISION = "@BUILD_GIT_COMMIT_HASH@";
    public static final ReleaseChannel CHANNEL = ReleaseChannel.BETA;

    public static final Logger LOGGER = LogManager.getLogger("EvergreenHUD");
    public static final File DATA_DIR = new File(mc.mcDataDir, "config/evergreenhud");

    @Getter
    @Mod.Instance(EvergreenHUD.MOD_ID)
    private static EvergreenHUD instance;

    @Getter private ElementManager elementManager;
    @Getter private boolean development;

    @Getter private boolean firstLaunch = false;
    @Getter private boolean versionTwoFirstLaunch = false;

    private boolean disabled = false;
    private boolean blacklisted = false;

    private boolean reset = false;

    private final KeyBinding guiKeybind = new KeyBinding("Open GUI", Keyboard.KEY_HOME, "EvergreenHUD");

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        XanderLib.getInstance().initPhase();

        ProgressManager.ProgressBar progress = ProgressManager.push("EvergreenHUD", 9);

        progress.step("Blacklist Check");
        try {
            blacklisted = BlacklistManager.isVersionBlacklisted(MOD_VERSION);
        } catch (NullPointerException e) {
            LOGGER.error("Failed to check if version is blacklisted. This is likely an internet issue. Stacktrace: ");
            e.printStackTrace();
            EssentialAPI.getNotifications().push("EvergreenHUD", "Failed to check if version is blacklisted. This is likely a connection issue.");
        }

        if (blacklisted) disable();

        progress.step("Forge Check");
        if (ForgeVersion.getBuildVersion() < 2318 && ForgeVersion.getBuildVersion() != 0) {
            disable();
            EssentialAPI.getGuiUtil().openScreen(new GuiOldForge());
        }

        if (disabled) {
            // stupid forge throwing exception useless i hate you
            for (int i = progress.getStep(); i < progress.getSteps(); i++) {
                progress.step("ABORTED");
            }

            ProgressManager.pop(progress);
            return;
        }

        progress.step("Evergreen Check");
        firstLaunch = !DATA_DIR.exists();
        versionTwoFirstLaunch = !ElementConfig.CONFIG_FILE.exists();

        progress.step("Registering Hooks");
        new EvergreenHudCommand().register();
        ClientRegistry.registerKeyBinding(guiKeybind);
        XanderLib.getInstance().getGuiEditor().addModifier(GuiOptions.class, new AbstractGuiModifier() {
            @Override
            public void onInitGuiPost(GuiScreen screen, List<GuiButton> buttonList) {
                buttonList.add(new GuiButton(991, screen.width / 2 + 5, screen.height / 6 + 24 - 6, 150, 20, "EvergreenHUD..."));
            }

            @Override
            public void onActionPerformedPost(GuiScreen screen, List<GuiButton> buttonList, GuiButton button) {
                if (button.id == 991) {
                    mc.displayGuiScreen(new GuiMain(mc.currentScreen));
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
            boolean chromaHud = new File(ChromaHudConverter.DEFAULT_DIR, ChromaHudConverter.CONFIG_FILE).exists();
            boolean simpleHud = SimpleHudConverter.DEFAULT_DIR.exists();

            if (chromaHud && simpleHud) {
                EssentialAPI.getNotifications().push("EvergreenHUD", "Multiple HUD mod configurations have been identified. Click this notification to pick which HUD mod to convert.", () -> {
                    mc.displayGuiScreen(new GuiConfigConverter(mc.currentScreen));

                    return Unit.INSTANCE;
                });
            } else if (chromaHud) {
                EssentialAPI.getNotifications().push("ChromaHUD Detected", "An existing ChromaHUD configuration has been detected. Would you like to convert it to EvergreenHUD?", () -> {
                    new ChromaHudConverter(ChromaHudConverter.DEFAULT_DIR).process(EvergreenHUD.getInstance().getElementManager());

                    return Unit.INSTANCE;
                });
            } else if (simpleHud) {
                EssentialAPI.getNotifications().push("SimpleHUD Detected", "An existing SimpleHUD configuration has been detected. Would you like to convert it to EvergreenHUD?", () -> {
                    new SimpleHudConverter(SimpleHudConverter.DEFAULT_DIR).process(EvergreenHUD.getInstance().getElementManager());

                    return Unit.INSTANCE;
                });
            }

            EssentialAPI.getNotifications().push("EvergreenHUD", "Use /evergreenhud to configure.");
        }

        ProgressManager.pop(progress);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (blacklisted) {
            EssentialAPI.getNotifications().push("EvergreenHUD",
                    "The current version of this mod has been blacklisted.\n"
                    + "Please check the discord server for updates.\n"
                    + "Click to join the discord.",

            () -> {
                try {
                    UDesktop.browse(new URI("https://discord.gg/AJv5ZnNT8q"));
                } catch (Exception e) {
                    e.printStackTrace();
                    EssentialAPI.getNotifications().push("EvergreenHUD", "An error was encountered while trying to open the link.");
                }
                return Unit.INSTANCE;
            });
        }
        if (disabled) return;

        if (getElementManager().isCheckForUpdates()) {
            Multithreading.runAsync(() -> {
                if (EssentialAPI.getMinecraftUtil().isDevelopment()) {
                    LOGGER.warn("Running in development environment. Skipped update check.");
                    development = true;
                } else {
                    try {
                        String version = UpdateChecker.getNeededVersion();
                        if (!version.equalsIgnoreCase(EvergreenHUD.MOD_VERSION)) {
                            LOGGER.warn("Mod is out of date. " + EvergreenHUD.MOD_VERSION + " > " + version);
                            notifyUpdate(version);
                        }
                    } catch (NullPointerException e) {
                        LOGGER.error("Failed to check if version is up to date. This is likely an internet issue. Stacktrace: ");
                        e.printStackTrace();
                        EssentialAPI.getNotifications().push("EvergreenHUD", "Failed to check if version is up to date. This is likely a connection issue.");
                    }

                }
            });
        } else {
            LOGGER.info("User disabled update check - skipping.");
        }

        if (reset) {
            reset = false;
            EssentialAPI.getNotifications().push("EvergreenHUD", "The configuration has been reset due to a version change that makes your configuration incompatible with the current version.");
        }

        AddonManager.getInstance().onPostInit();
    }

    public void disable() {
        disabled = true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (guiKeybind.isPressed())
            mc.displayGuiScreen(new GuiMain(null));
    }

    public static void notifyUpdate(String latestVersion) {
        EssentialAPI.getNotifications().push("EvergreenHUD", "You are running an outdated version.\nCurrent: " + EvergreenHUD.MOD_VERSION + "\nLatest: " + latestVersion + "\n\nClick here to download.", () -> {
            try {
                UDesktop.browse(new URI("https://github.com/isXander/EvergreenHUD/releases"));
            } catch (Exception e) {
                e.printStackTrace();
                EssentialAPI.getNotifications().push("EvergreenHUD", "Unfortunately, your computer does not seem to support web-browsing so the mod could not open the download page.\n\nPlease navigate to \"https://short.evergreenclient.com/GlYH5z\"" );
            }
            return Unit.INSTANCE;
        });
    }

    public void notifyConfigReset() {
        reset = true;
    }

}
