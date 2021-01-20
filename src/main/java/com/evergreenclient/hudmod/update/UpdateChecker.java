/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.update;

import com.evergreenclient.hudmod.EvergreenHUD;
import com.evergreenclient.hudmod.utils.HttpsUtils;
import com.evergreenclient.hudmod.utils.json.BetterJsonObject;

public class UpdateChecker {

    private static final String URL = "https://raw.githubusercontent.com/Evergreen-Client/EvergreenHUD/main/version.json";

    public static double getLatestVersion() {
        String out = HttpsUtils.getString(URL);
        if (out == null) return 0;
        BetterJsonObject json = new BetterJsonObject(out);
        return json.optDouble("latest");
    }

    public static boolean updateAvailable() {
        return getLatestVersion() > Double.parseDouble(EvergreenHUD.VERSION);
    }

}
