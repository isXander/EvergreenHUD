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

package com.evergreenclient.hudmod.update;

import com.evergreenclient.hudmod.EvergreenHUD;
import com.evergreenclient.hudmod.utils.HttpsUtils;
import com.evergreenclient.hudmod.utils.Version;
import com.evergreenclient.hudmod.utils.BetterJsonObject;

public class UpdateChecker {

    private static final String URL = "https://raw.githubusercontent.com/Evergreen-Client/EvergreenHUD/main/version.json";

    public static Version getLatestVersion() {
        String out = HttpsUtils.getString(URL);
        if (out == null) return new Version(0, 0, 0);
        BetterJsonObject json = new BetterJsonObject(out);
        return new Version(json.optString("latest_v2"));
    }

    public static boolean updateAvailable() {
        return getLatestVersion().newerThan(EvergreenHUD.PARSED_VERSION);
    }

}
