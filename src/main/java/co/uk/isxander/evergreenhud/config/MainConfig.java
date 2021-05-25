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

package co.uk.isxander.evergreenhud.config;

import co.uk.isxander.evergreenhud.elements.ElementManager;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import co.uk.isxander.evergreenhud.EvergreenHUD;

import java.io.File;

public class MainConfig {

    private static final int VERSION = 1;
    public static final File CONFIG_FILE = new File(EvergreenHUD.DATA_DIR, "config.json");

    private final ElementManager manager;

    public MainConfig(ElementManager manager) {
        this.manager = manager;
    }

    public void load() {
        BetterJsonObject root;
        try {
            root = BetterJsonObject.getFromFile(CONFIG_FILE);
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
    }

    public void save() {
        BetterJsonObject root = new BetterJsonObject();
        root.addProperty("version", VERSION);
        root.addProperty("enabled", manager.isEnabled());

        root.writeToFile(CONFIG_FILE);
    }

}
