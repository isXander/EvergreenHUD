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

import club.sk1er.mods.core.gui.notification.Notifications;
import co.uk.isxander.evergreenhud.config.convert.ConfigConverter;
import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.elements.ElementManager;
import co.uk.isxander.evergreenhud.elements.impl.*;
import co.uk.isxander.evergreenhud.elements.type.TextElement;
import co.uk.isxander.xanderlib.utils.Position;
import co.uk.isxander.xanderlib.utils.Resolution;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChromaHudConverter extends ConfigConverter {

    public static final File DEFAULT_DIR = new File(mc.mcDataDir, "config");
    public static final String CONFIG_FILE = "ChromaHUD.cfg";
    public static final Map<String, String> IDENTIFIER_MAP = new HashMap<String, String>() {{
        put("CORDS", "COORDS");
        put("PING", "PING");
        put("DIRECTION", "DIRECTION");
        put("CPS", "CPS");
        put("FPS", "FPS");
        put("TEXT", "TEXT");
        put("TIME", "TIME");
        put("ARMOUR_HUD", "ARMOUR");
        put("C_COUNTER", "CHUNK_COUNT");
    }};

    private int failures;

    public ChromaHudConverter(File configFolder) {
        super(configFolder);
        this.failures = 0;
    }

    @Override
    public void process(ElementManager manager) {
        File configFile = new File(getConfigFolder(), CONFIG_FILE);

        if (!configFile.exists() || configFile.isDirectory()) {
            Notifications.INSTANCE.pushNotification("Conversion Failed", "Could not find ChromaHUD folder.");
            return;
        }

        try {
            BetterJsonObject config = BetterJsonObject.getFromFile(new File(getConfigFolder(), CONFIG_FILE));

            processMain(manager, config);
            processElements(manager, config);

            manager.getMainConfig().save();
            manager.getElementConfig().save();

            Notifications.INSTANCE.pushNotification("Conversion Completed", "ChromaHUD conversion has been completed."
                    + (failures > 0 ? "\n" + failures + " failures encountered." : ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processMain(ElementManager manager, BetterJsonObject chromaHudConfig) {
        manager.setEnabled(chromaHudConfig.optBoolean("enabled", true));
    }

    private void processElements(ElementManager manager, BetterJsonObject chromaHudConfig) {
        JsonArray elementArray = chromaHudConfig.get("elements").getAsJsonArray();
        ScaledResolution res = Resolution.get();

        for (JsonElement part : elementArray) {
            if (!part.isJsonObject()) {
                getLogger().warn("Element Json Array contains non-json-object. This is not normal.");
                continue;
            }
            BetterJsonObject elementJson = new BetterJsonObject(part.getAsJsonObject());

            Position position = Position.getPositionWithScaledPositioning((float) elementJson.optDouble("x"), (float) elementJson.optDouble("y"), (float) elementJson.optDouble("scale"));
            Color textCol = new Color(elementJson.optInt("color"));
            if (elementJson.optBoolean("rgb", false)) {
                textCol = new Color(elementJson.optInt("red", 255), elementJson.optInt("green", 255), elementJson.optInt("blue", 255), 255);
            }
            boolean chroma = elementJson.optBoolean("chroma", false);
            boolean shadow = elementJson.optBoolean("shadow", false);
            boolean useBg = elementJson.optBoolean("highlighted", false);

            JsonElement itemElement = elementJson.get("items");
            if (!itemElement.isJsonArray()) {
                getLogger().warn("Element Object does not contain valid item array.");
                continue;
            }

            int i = 0;
            final int changeY = (mc.fontRendererObj.FONT_HEIGHT + 4) / res.getScaledHeight();
            for (JsonElement jsonElement : itemElement.getAsJsonArray()) {
                if (!jsonElement.isJsonObject()) {
                    getLogger().warn("Item Json Array contains non-json-object. This is not normal.");
                    continue;
                }
                BetterJsonObject item = new BetterJsonObject(jsonElement.getAsJsonObject());

                String evergreenId = IDENTIFIER_MAP.get(item.optString("type", ""));
                if (evergreenId == null) {
                    failures++;
                    getLogger().error("Unknown Item Type");
                    continue;
                }

                Element element = manager.getNewElementInstance(evergreenId);
                element.getPosition().set(position);
                element.getPosition().setScaledY(element.getPosition().getYScaled() + (i * changeY));
                if (element instanceof TextElement) {
                    TextElement textElement = (TextElement) element;

                    textElement.setTextColor(textCol.getRed(), textCol.getGreen(), textCol.getBlue());
                    if (useBg) {
                        textElement.getPaddingLeft().set(1f);
                        textElement.getPaddingRight().set(1f);
                        textElement.getPaddingTop().set(1f);
                        textElement.getPaddingBottom().set(1f);
                    } else {
                        textElement.setBgColor(0, 0, 0, 0);
                    }
                    textElement.getChroma().set(chroma);
                    textElement.getTextMode().set((shadow ? TextElement.TextMode.SHADOW : TextElement.TextMode.NORMAL));
                }


                if (element instanceof ElementCoordinates) {
                    ElementCoordinates coords = (ElementCoordinates) element;
                    coords.accuracy.set(item.optInt("precision", 0));
                } else if (element instanceof ElementText) {
                    ElementText text = (ElementText) element;
                    text.text.set(item.optString("text", "None"));
                } else if (element instanceof ElementTime) {
                    ElementTime time = (ElementTime) element;
                    time.showSeconds.set(true);
                } else if (element instanceof ElementArmour) {
                    ElementArmour armour = (ElementArmour) element;
                    armour.item.set(item.optBoolean("hand", false));
                    armour.textDisplay.set((item.optBoolean("dur", false) ? "Durability" : "None"));
                } else if (element instanceof ElementCps) {
                    ElementCps cps = (ElementCps) element;
                    cps.button.set("Left");
                }

                manager.addElement(element);

                i++;
            }
        }
    }
}
