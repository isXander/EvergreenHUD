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

package co.uk.isxander.evergreenhud.command;

import club.sk1er.mods.core.ModCore;
import club.sk1er.mods.core.gui.notification.Notifications;
import club.sk1er.mods.core.util.MinecraftUtils;
import club.sk1er.mods.core.util.Multithreading;
import co.uk.isxander.evergreenhud.addon.EvergreenAddon;
import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.gui.screens.impl.GuiMain;
import co.uk.isxander.xanderlib.utils.*;
import co.uk.isxander.evergreenhud.EvergreenHUD;
import co.uk.isxander.evergreenhud.github.UpdateChecker;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import okhttp3.*;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EvergreenHudCommand extends CommandBase implements Constants {

    @Override
    public String getCommandName() {
        return "evergreenhud";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>(Arrays.asList(
                "evergreenhud",
                "hud",
                "evergreen",
                "hudmod",
                "xanderhud"
        ));
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/evergreenhud [update|version|debug]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "update":
                case "check":
                    Multithreading.runAsync(() -> {
                        if (EvergreenHUD.getInstance().isDevelopment()) {
                            Notifications.INSTANCE.pushNotification("EvergreenHUD", "You are on a development version. There are no updates available.");
                        } else {
                            String latest = UpdateChecker.getNeededVersion();
                            if (!latest.equalsIgnoreCase(EvergreenHUD.MOD_VERSION)) {
                                EvergreenHUD.notifyUpdate(latest);
                            } else {
                                Notifications.INSTANCE.pushNotification("EvergreenHUD", "There are no updates available.");
                            }
                        }
                    });
                    break;
                case "version":
                    Notifications.INSTANCE.pushNotification("EvergreenHUD", "You are running on version " + EvergreenHUD.MOD_VERSION + "\nIf you want to check for updates, use \"/evergreenhud update\"");
                    break;
                case "support":
                case "debug":
                    StringBuilder sb = new StringBuilder();

                    sb.append("--- EvergreenHUD Info ---").append("\n");
                    sb.append("Version: ").append(EvergreenHUD.MOD_VERSION).append("\n");
                    sb.append("\n");

                    sb.append("--- EvergreenHUD Elements ---").append("\n");
                    for (Element element : EvergreenHUD.getInstance().getElementManager().getCurrentElements()) {
                        sb.append("[").append(element.getMetadata().getName()).append("](");
                        for (String configLine : element.generateJson().toPrettyString().split("\n")) {
                            sb.append("\n").append("    ").append(configLine);
                        }
                        sb.append("\n)\n");
                    }
                    sb.append("\n");

                    sb.append("--- EvergreenHUD Addons ---").append("\n");
                    sb.append("[");
                    for (EvergreenAddon addon : EvergreenHUD.getInstance().getAddonManager().addons) {
                        sb.append("    ").append(addon.metadata().name).append(" v").append(addon.metadata().version).append("\n");
                    }
                    sb.append("]\n\n");

                    sb.append("--- Other Info ---").append("\n");
                    sb.append("Environment: ").append(MinecraftUtils.isDevelopment() ? "Development" : "Production").append("\n");
                    sb.append("Current Screen: ").append(mc.currentScreen == null ? "None" : mc.currentScreen.getClass().getName()).append("\n");
                    sb.append("Server: ");
                    if (mc.getCurrentServerData() == null) sb.append("None");
                    else {
                        ServerData server = mc.getCurrentServerData();
                        sb.append(server.serverIP).append(" : ").append(server.pingToServer).append("ms");
                    }
                    sb.append("\n");
                    sb.append("In-Game: ").append(mc.theWorld != null && mc.thePlayer != null).append("\n");
                    sb.append("\n");

                    sb.append("--- System Info ---").append("\n");
                    sb.append("Java Version: ").append(System.getProperty("java.version")).append(" ").append(mc.isJava64bit() ? "64" : "32").append("bit\n");
                    long totalMem = Runtime.getRuntime().totalMemory();
                    long freeMem = Runtime.getRuntime().freeMemory();
                    long maxMem = Runtime.getRuntime().maxMemory();
                    sb.append("Memory: ").append(bytesToMb(totalMem - freeMem)).append("/").append(bytesToMb(maxMem)).append("MB").append("\n");
                    sb.append("CPU: ").append(OpenGlHelper.getCpu()).append("\n");
                    sb.append("Renderer: ").append(GL11.glGetString(GL11.GL_RENDERER)).append("\n");
                    sb.append("Driver: ").append(GL11.glGetString(GL11.GL_VERSION)).append("\n");
                    sb.append("\n");

                    sb.append("--- Mod List ---").append("\n");
                    List<ModContainer> activeMods = Loader.instance().getActiveModList();
                    for (ModContainer mod : Loader.instance().getModList()) {
                        sb.append("[")
                                .append(mod.getMetadata().modId)
                                .append("-")
                                .append(mod.getMetadata().version);
                        if (!activeMods.contains(mod))
                            sb.append(" (disabled)");
                        sb.append("]");

                        sb.append("(")
                                .append(mod.getSource().getName())
                        .append(")");

                        sb.append("\n");
                    }

                    Multithreading.runAsync(() -> {
                        OkHttpClient client = new OkHttpClient();
                        Request request = HttpsUtils.setupRequest("https://hst.sh/documents")
                                .post(RequestBody.create(MediaType.parse("text/utf-8"), sb.toString()))
                                .build();
                        try (Response response = client.newCall(request).execute()) {
                            String string = response.body().string();
                            if (!string.startsWith("{")) {
                                sb.insert(0, "```\n");
                                sb.append("\n```");
                                ClipboardUtils.setClipboard("**EvergreenHUD Debug Failed to Upload**\n" + sb);
                                Notifications.INSTANCE.pushNotification("EvergreenHUD", "Failed to upload debug information to pastebin.\nReason: " + string + "\n\nThe raw debug info has been copied to your clipboard instead.");
                                return;
                            }
                            BetterJsonObject json = new BetterJsonObject(string);

                            ClipboardUtils.setClipboard("**EvergreenHUD Debug**\nhttps://hst.sh/" + json.optString("key"));

                            Notifications.INSTANCE.pushNotification("EvergreenHUD", "Debug info has been copied to the clipboard.\nPlease paste it in the discord server to get support.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
                    break;
                default:
                    Notifications.INSTANCE.pushNotification("EvergreenHUD", "Unknown subcommand.\nUsage: " + getCommandUsage(null));
            }
        } else {
            ModCore.getInstance().getGuiHandler().open(new GuiMain());
        }
    }

    private static long bytesToMb(long bytes) {
        return bytes / 1024L / 1024L;
    }

}
