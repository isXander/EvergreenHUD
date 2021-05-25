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

package co.uk.isxander.evergreenhud.repo;

import co.uk.isxander.xanderlib.utils.HttpsUtils;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import co.uk.isxander.evergreenhud.EvergreenHUD;

public class UpdateChecker {

    private static final String URL = "https://raw.githubusercontent.com/isXander/EvergreenHUD/main/version.json";
    private static BetterJsonObject cache = null;

    public static String getNeededVersion() {
        if (cache == null) cache();

        if (EvergreenHUD.RELEASE)
            return cache.optString("release");
        else
            return cache.optString("prerelease");
    }

    public static void cache() {
        cache = new BetterJsonObject(HttpsUtils.getString(URL));
    }

}
