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

import lombok.Getter;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;

public class AddonMeta {

    @Getter private final String name, description, version;

    public AddonMeta(String name, String description, String version, int apiVersion) {
        if (AddonManager.API_VERSION != apiVersion) {
            CrashReport crash = CrashReport.makeCrashReport(new UnsupportedEvergreenAddonException("Tried to load " + name + " v" + version + " however it uses API v" + apiVersion + " whilst the running one is " + AddonManager.API_VERSION), (AddonManager.API_VERSION > apiVersion ? "Outdated addon." : "Outdated EvergreenHUD."));
            throw new ReportedException(crash);
        }
        
        this.name = name;
        this.description = description;
        this.version = version;
    }
}
