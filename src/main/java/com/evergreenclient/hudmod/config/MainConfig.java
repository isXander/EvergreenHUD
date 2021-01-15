/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.config;

import com.evergreenclient.hudmod.EvergreenHUD;
import com.evergreenclient.hudmod.elements.ElementManager;
import com.evergreenclient.hudmod.utils.json.BetterJsonObject;
import net.minecraft.client.Minecraft;

import java.io.File;

public class MainConfig {

    private static final int VERSION = 1;

    private final ElementManager manager;
    public final File configFile;

    public MainConfig(ElementManager manager) {
        this.manager = manager;
        this.configFile = new File(Minecraft.getMinecraft().mcDataDir, "config/evergreenhud/config.json");
    }

    public void load() {
        BetterJsonObject root;
        try {
            root = BetterJsonObject.getFromFile(this.configFile);
        } catch (Exception e) {
            save();
            return;
        }

        // new config version so we need to reset the config
        if (root.optInt("version") != VERSION) {
            manager.getLogger().warn("Resetting configuration! Older or newer config version detected.");
            EvergreenHUD.getInstance().notifyConfigReset();
            save();
            return;
        }
        manager.setEnabled(root.optBoolean("enabled"));
        manager.setShowInChat(root.optBoolean("chat"));
        manager.setShowInDebug(root.optBoolean("debug"));
        manager.setColorsInGui(root.optBoolean("colors"));
    }

    public void save() {
        BetterJsonObject root = new BetterJsonObject();
        root.addProperty("version", VERSION);
        root.addProperty("enabled", manager.isEnabled());
        root.addProperty("chat", manager.doShowInChat());
        root.addProperty("debug", manager.doShowInDebug());
        root.addProperty("colors", manager.doColorsInGui());

        root.writeToFile(this.configFile);
    }

}
