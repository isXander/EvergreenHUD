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

package co.uk.isxander.evergreenhud.config.convert.impl;

import co.uk.isxander.evergreenhud.config.convert.ConfigConverter;
import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.elements.ElementManager;
import co.uk.isxander.xanderlib.utils.Resolution;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.client.gui.ScaledResolution;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class LunarClientConverter extends ConfigConverter {

    public static final File DEFAULT_DIR = new File(new File(System.getProperty("user.home")), ".lunarclient/settings/game");
    public static final String PROFILE_MANAGER_NAME = "profile_manager.json";
    public static final Map<String, String> IDENTIFIER_MAP = new HashMap<String, String>() {{
        put("fps", "FPS");
        put("cps", "CPS");
        put("armorstatus", "ARMOUR");
        put("coords", "COORDS");
        put("daycounter", "DAY");
        put("directionhud", "DIRECTION");
        put("ping", "PING");
        put("clock", "TIME");
        put("memory", "MEMORY");
        put("combo", "COMBO");
        put("range", "REACH");
    }};

    public LunarClientConverter(File configFolder) {
        super(configFolder);
    }

    @Override
    public void process(ElementManager manager) {
        try {
            // get profile manager
            JsonArray profileManager = new JsonParser().parse(new FileReader(new File(getConfigFolder(), PROFILE_MANAGER_NAME))).getAsJsonArray();

            // get the active profile so we can access it
            String profileName = "Default";
            for (JsonElement profileElement : profileManager) {
                BetterJsonObject profileObj = new BetterJsonObject(profileElement.getAsJsonObject());
                if (profileObj.optBoolean("active", false)) {
                    profileName = profileObj.optString("name");
                    break;
                }
            }

            File profileFolder = new File(getConfigFolder(), profileName);
            BetterJsonObject modsJsonList = BetterJsonObject.getFromFile(new File(profileFolder, "mods.json"));
            for (String key : modsJsonList.getAllKeys()) {
                BetterJsonObject modJson = modsJsonList.getObj(key);
                if (!modJson.optBoolean("seen", false)) continue;

                Element element = manager.getNewElementInstance(IDENTIFIER_MAP.get(key));
                if (element != null) {

                    if (modJson.has("position")) {
                        ScaledResolution res = Resolution.get();
                        int originX = 0;
                        int originY = 0;
                        switch (modJson.optString("position", "top_left")) {
                            case "bottom_left":
                                originX = 0;
                                originY = res.getScaledHeight();
                                break;
                            case "bottom_right":
                                originX = res.getScaledWidth();
                                originY = res.getScaledHeight();
                                break;
                            case "top_left":
                                originX = 0;
                                originY = 0;
                                break;
                            case "top_right":
                                originX = res.getScaledWidth();
                                originY = 0;
                                break;
                        }

                        int rawX = originX + modJson.optInt("x", 0);
                        int rawY = originY + modJson.optInt("y", 0);

                        element.getPosition().setRawX(rawX, res);
                        element.getPosition().setRawY(rawY, res);
                        element.getPosition().setScale(modJson.optFloat("scale", 1f));
                        manager.addElement(element);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
