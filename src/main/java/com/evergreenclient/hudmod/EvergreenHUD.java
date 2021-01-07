/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod;

import com.evergreenclient.hudmod.command.EvergreenHudCommand;
import com.evergreenclient.hudmod.elements.ElementManager;
import com.evergreenclient.hudmod.gui.MainGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = "evergreenhud", name = "EvergreenHUD", version = "0.1", clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]")
public class EvergreenHUD {

    @Mod.Instance("evergreenhud")
    private static EvergreenHUD instance;

    private ElementManager elementManager;

    private KeyBinding keybind = new KeyBinding("Open GUI", Keyboard.KEY_RSHIFT, "Evergreen");

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new EvergreenHudCommand());
        ClientRegistry.registerKeyBinding(keybind);
        MinecraftForge.EVENT_BUS.register(elementManager = new ElementManager());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (keybind.isPressed())
            Minecraft.getMinecraft().displayGuiScreen(new MainGUI());
    }

    public static EvergreenHUD getInstance() {
        return instance;
    }

    public ElementManager getElementManager() {
        return elementManager;
    }

}
