/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.command;

import club.sk1er.mods.core.gui.notification.Notifications;
import club.sk1er.mods.core.util.Multithreading;
import com.evergreenclient.hudmod.EvergreenHUD;
import com.evergreenclient.hudmod.gui.screens.impl.GuiMain;
import com.evergreenclient.hudmod.update.UpdateChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EvergreenHudCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "assets/evergreenhud";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>(Arrays.asList(
                "evergreenhud",
                "hud",
                "evergreen",
                "hudmod"
        ));
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/evergreenhud [update|check]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("update") || args[0].equalsIgnoreCase("check")) {
                Multithreading.runAsync(() -> {
                    if (EvergreenHUD.getInstance().isDevelopment())
                        Notifications.INSTANCE.pushNotification("EvergreenHUD", "You are on a development version. There are no updates available.");
                    else if (UpdateChecker.updateAvailable()) {
                        EvergreenHUD.notifyUpdate();
                    } else {
                        Notifications.INSTANCE.pushNotification("EvergreenHUD", "There are no updates available.");
                    }
                });
            }
        }
        else {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiMain());
        MinecraftForge.EVENT_BUS.unregister(this);
    }

}
