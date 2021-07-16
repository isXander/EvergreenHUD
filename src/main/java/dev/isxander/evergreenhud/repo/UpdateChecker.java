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

package dev.isxander.evergreenhud.repo;

import dev.isxander.xanderlib.utils.HttpsUtils;
import dev.isxander.xanderlib.utils.json.BetterJsonObject;
import dev.isxander.evergreenhud.EvergreenHUD;

public class UpdateChecker {

    private static final String URL = "https://raw.githubusercontent.com/isXander/EvergreenHUD/main/version.json";
    private static BetterJsonObject cache = null;

    public static String getNeededVersion() {
        if (cache == null) cache();

        return cache.optString(EvergreenHUD.CHANNEL.jsonName);
    }

    public static void cache() {
        cache = new BetterJsonObject(HttpsUtils.getString(URL));
    }

}
