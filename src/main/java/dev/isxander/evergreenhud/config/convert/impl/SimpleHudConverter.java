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

package dev.isxander.evergreenhud.config.convert.impl;

import dev.isxander.evergreenhud.config.convert.ConfigConverter;
import dev.isxander.evergreenhud.elements.Element;
import dev.isxander.evergreenhud.elements.ElementManager;
import dev.isxander.evergreenhud.elements.impl.ElementArmour;
import dev.isxander.evergreenhud.elements.impl.ElementText;
import dev.isxander.evergreenhud.elements.type.BackgroundElement;
import dev.isxander.evergreenhud.elements.type.TextElement;
import static dev.isxander.xanderlib.utils.Constants.*;
import dev.isxander.xanderlib.utils.Resolution;
import dev.isxander.xanderlib.utils.json.BetterJsonObject;
import gg.essential.api.EssentialAPI;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SimpleHudConverter extends ConfigConverter {

    public static final File DEFAULT_DIR = new File(mc.mcDataDir, "config/TGMDevelopment/SimpleHUD");
    public static final Map<String, String> IDENTIFIER_MAP = new HashMap<String, String>() {{
        put("ArmourHUD.json", "ARMOUR");
        put("Biome.json", "BIOME");
        put("Coords.json", "COORDS");
        put("CPS.json", "CPS");
        put("Day.json", "DAY");
        put("FPS.json", "FPS");
        put("Memory.json", "MEMORY");
        put("Ping.json", "PING");
        put("Player View.json", "PLAYER_PREVIEW");
        put("Reach Display.json", "REACH");
        put("Server Address.json", "SERVER");
        put("Simple Text.json", "TEXT");
        put("Time.json", "TIME");
    }};

    public SimpleHudConverter(File configFolder) {
        super(configFolder);
    }

    @Override
    public void process(ElementManager manager) {
        File elementFolder = new File(getConfigFolder(), "HUD Elements");
        if (!elementFolder.exists() || !elementFolder.isDirectory()) {
            EssentialAPI.getNotifications().push("Conversion Failed", "Could not find SimpleHUD folder.");
            return;
        }

        int failures = 0;

        for (File elementFile : elementFolder.listFiles()) {
            String evergreenId = IDENTIFIER_MAP.get(elementFile.getName());

            if (evergreenId == null) {
                getLogger().error("Could not find ");
                failures++;
                continue;
            }

            try {
                BetterJsonObject elementJson = BetterJsonObject.getFromFile(elementFile);

                if (elementJson.optBoolean("toggle")) {
                    Element element = manager.getNewElementInstance(evergreenId);

                    BetterJsonObject posJson = elementJson.getObj("position");
                    element.getPosition().setRawX(posJson.optInt("x"), Resolution.get());
                    element.getPosition().setRawY(posJson.optInt("y"), Resolution.get());
                    element.getPosition().setScale(posJson.optFloat("scale"));

                    if (element instanceof BackgroundElement) {
                        BackgroundElement bgElement = (BackgroundElement) element;

                        BetterJsonObject bgJson = elementJson.getObj("background");

                        if (bgJson.optBoolean("toggle")) {
                            BetterJsonObject bgColJson = bgJson.getObj("colour");
                            bgElement.setBgColor(bgColJson.optInt("r"), bgColJson.optInt("g"), bgColJson.optInt("b"), bgColJson.optInt("a"));
                        }
                    }

                    if (element instanceof TextElement) {
                        TextElement textElement = (TextElement) element;

                        BetterJsonObject colJson = elementJson.getObj("colour");
                        textElement.setTextColor(colJson.optInt("r"), colJson.optInt("g"), colJson.optInt("b"));

                        textElement.getBrackets().set(elementJson.optBoolean("show_brackets"));
                        textElement.getChroma().set(elementJson.optBoolean("chroma"));
                        textElement.getTextMode().set(elementJson.optBoolean("text_shadow") ? TextElement.TextMode.SHADOW : TextElement.TextMode.NORMAL);
                        if (!elementJson.optBoolean("show_prefix"))
                            textElement.getTitleText().set("");
                    }

                    if (element instanceof ElementArmour) {
                        ElementArmour armour = (ElementArmour) element;

                        String type = elementJson.optString("type");
                        if (type.equalsIgnoreCase("NONE")) {
                            armour.textDisplay.set("None");
                        }
                    }

                    if (element instanceof ElementText) {
                        ElementText text = (ElementText) element;

                        text.text.set(elementJson.optString("text", "Unknown"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                failures++;
            }
        }

        File mainConfigFile = new File(getConfigFolder(), "main.json");
        try {
            BetterJsonObject mainConfigJson = BetterJsonObject.getFromFile(mainConfigFile);

            manager.setEnabled(mainConfigJson.optBoolean("full_toggle", true));
        } catch (IOException e) {
            e.printStackTrace();
            failures++;
        }

        manager.getMainConfig().save();
        manager.getElementConfig().save();

        EssentialAPI.getNotifications().push("Conversion Completed", "SimpleHUD conversion has been completed."
                + (failures > 0 ? "\n" + failures + " failures encountered." : ""));
    }

}
