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

package dev.isxander.evergreenhud.addon.repo;

import dev.isxander.evergreenhud.addon.AddonManager;
import dev.isxander.xanderlib.utils.json.BetterJsonObject;
import lombok.Getter;

public class AddonEntry {

    @Getter private final String name, description, addonVersion;
    @Getter private final int apiVersion;

    public AddonEntry(String name, String description, String addonVersion, int apiVersion) {
        this.name = name;
        this.description = description;
        this.addonVersion = addonVersion;
        this.apiVersion = apiVersion;
    }

    public AddonEntry(BetterJsonObject json) {
        this(json.optString("name"), json.optString("description"), json.optString("addon_version"), json.optInt("api_version"));
    }

    public String getDownloadUrl() {
        return AddonManager.REPO_URL + "jars/" + name + "/" + addonVersion + ".jar";
    }

}
