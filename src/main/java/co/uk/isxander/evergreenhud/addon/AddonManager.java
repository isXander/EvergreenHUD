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

package co.uk.isxander.evergreenhud.addon;

import co.uk.isxander.evergreenhud.EvergreenHUD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

public class AddonManager {

    public final List<EvergreenAddon> addons;
    private final Logger logger;

    public AddonManager() {
        logger = LogManager.getLogger("EvergreenHUD Addons");
        addons = new ArrayList<>();
    }

    public void discoverAddons() {
        logger.info("Discovering Addons...");
        long time = System.currentTimeMillis();
        Reflections reflections = new Reflections("");
        for (Class<? extends EvergreenAddon> clazz : reflections.getSubTypesOf(EvergreenAddon.class)) {
            try {
                addons.add(clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info(String.format("Completed: Took %s ms and found %s addons.", System.currentTimeMillis() - time, addons.size()));
    }

    public void onInit() {
        for (EvergreenAddon addon : addons) {
            addon.init();
        }
    }

    public void onConfigLoad() {
        for (EvergreenAddon addon : addons) {
            addon.configLoad();
        }
    }

}
