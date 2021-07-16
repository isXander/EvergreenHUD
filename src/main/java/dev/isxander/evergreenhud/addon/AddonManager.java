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

package dev.isxander.evergreenhud.addon;

import dev.isxander.evergreenhud.EvergreenHUD;

import java.util.ArrayList;
import java.util.List;

public class AddonManager {

    public static final int API_VERSION = 1;
    public static final String REPO_URL = "https://raw.githubusercontent.com/isXander/EvergreenHUD-REPO/main/assets/";

    private static AddonManager instance;

    public final List<EvergreenAddon> addons;

    private AddonManager() {
        addons = new ArrayList<>();
    }

    public void registerAddon(EvergreenAddon addon) {
        addons.add(addon);
        EvergreenHUD.LOGGER.info("Registered Addon: " + addon.metadata().getName());
    }

    public void onInit() {
        addons.forEach(EvergreenAddon::init);
    }

    public void onPostInit() {
        addons.forEach(EvergreenAddon::postInit);
    }

    public void onConfigLoad() {
        addons.forEach(EvergreenAddon::configLoad);
    }

    public static AddonManager getInstance() {
        if (instance == null) instance = new AddonManager();
        return instance;
    }

}
