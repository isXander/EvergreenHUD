/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.command;

import club.sk1er.mods.core.gui.notification.Notifications;
import com.evergreenclient.hudmod.EvergreenHUD;
import com.evergreenclient.hudmod.gui.MainGUI;
import com.evergreenclient.hudmod.update.UpdateChecker;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.net.URI;
import java.util.function.Function;
import java.util.function.Supplier;

public class EvergreenHudCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "evergreenhud";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("update")) {
                if (UpdateChecker.updateAvailable()) {
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
            }
        }
        else {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft.getMinecraft().displayGuiScreen(new MainGUI());
        MinecraftForge.EVENT_BUS.unregister(this);
    }

}
