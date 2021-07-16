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

package dev.isxander.evergreenhud.command;

import com.google.common.collect.Sets;
import dev.isxander.evergreenhud.addon.AddonManager;
import dev.isxander.evergreenhud.addon.EvergreenAddon;
import dev.isxander.evergreenhud.elements.Element;
import dev.isxander.evergreenhud.gui.screens.impl.GuiMain;
import dev.isxander.evergreenhud.EvergreenHUD;
import dev.isxander.evergreenhud.repo.UpdateChecker;
import dev.isxander.xanderlib.utils.*;
import static dev.isxander.xanderlib.utils.Constants.*;
import dev.isxander.xanderlib.utils.json.BetterJsonObject;
import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.commands.SubCommand;
import gg.essential.api.utils.Multithreading;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import okhttp3.*;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class EvergreenHudCommand extends Command {

    public EvergreenHudCommand() {
        super("evergreenhud");
    }

    @Override
    public Set<Alias> getCommandAliases() {
        return Sets.newHashSet(
                new Alias("evergreen"),
                new Alias("hudmod"),
                new Alias("xanderhud"),
                new Alias("egh")
        );
    }

    @DefaultHandler
    public void handle() {
        EssentialAPI.getGuiUtil().openScreen(new GuiMain(null));
    }

    @SubCommand(value = "update", aliases = {"check", "version"})
    public void update() {
        Multithreading.runAsync(() -> {
            if (EvergreenHUD.getInstance().isDevelopment()) {
                EssentialAPI.getNotifications().push("EvergreenHUD", "You are on a development version. There are no updates available.");
            } else {
                String latest = UpdateChecker.getNeededVersion();
                if (!latest.equalsIgnoreCase(EvergreenHUD.MOD_VERSION)) {
                    EvergreenHUD.notifyUpdate(latest);
                } else {
                    EssentialAPI.getNotifications().push("EvergreenHUD", "There are no updates available.");
                }
            }
        });
    }

    @SubCommand(value = "debug", aliases = {"debug", "print"})
    public void debug(List<String> args) {
        boolean reduced = false;
        if (!args.isEmpty()) {
            reduced = args.get(0).equalsIgnoreCase("reduced");
        }

        StringBuilder sb = new StringBuilder();

        if (reduced) {
            sb.append("REDUCED DEBUG\n\n");
        }

        sb.append("--- EvergreenHUD Info ---").append("\n");
        sb.append("Version: ").append(EvergreenHUD.MOD_VERSION).append("\n");
        sb.append("\n");

        sb.append("--- EvergreenHUD Elements ---").append("\n");
        for (Element element : EvergreenHUD.getInstance().getElementManager().getCurrentElements()) {
            sb.append("[").append(element.getMetadata().getName()).append("](");
            if (!reduced) {
                for (String configLine : element.generateJson().toPrettyString().split("\n")) {
                    sb.append("\n").append("    ").append(configLine);
                }
                sb.append("\n");
            } else {
                sb.append("reduced");
            }
            sb.append(")\n");
        }
        sb.append("\n");

        sb.append("--- EvergreenHUD Addons ---").append("\n");
        sb.append("[");
        for (EvergreenAddon addon : AddonManager.getInstance().addons) {
            sb.append("    ").append(addon.metadata().getName()).append(" v").append(addon.metadata().getVersion()).append("\n");
        }
        sb.append("]\n\n");

        sb.append("--- Other Info ---").append("\n");
        sb.append("Environment: ").append(EssentialAPI.getMinecraftUtil().isDevelopment() ? "Development" : "Production").append("\n");
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
        sb.append("\n");

        sb.append("--- File: /config/evergreenhud/elements.json ---").append("\n");
        sb.append(EvergreenHUD.getInstance().getElementManager().getElementConfig().generateJson().toPrettyString());
        sb.append("\n\n");

        sb.append("--- File: /config/evergreenhud/config.json ---").append("\n");
        sb.append(EvergreenHUD.getInstance().getElementManager().getMainConfig().generateJson().toPrettyString());
        sb.append("\n\n");

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
                    EssentialAPI.getNotifications().push("EvergreenHUD", "Failed to upload debug information to pastebin.\nReason: " + string + "\n\nThe raw debug info has been copied to your clipboard instead.");
                    return;
                }
                BetterJsonObject json = new BetterJsonObject(string);

                ClipboardUtils.setClipboard("**EvergreenHUD Debug**\nhttps://hst.sh/" + json.optString("key"));

                EssentialAPI.getNotifications().push("EvergreenHUD", "Debug info has been copied to the clipboard.\nPlease paste it in the discord server to get support.");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private static long bytesToMb(long bytes) {
        return bytes / 1024L / 1024L;
    }

}
