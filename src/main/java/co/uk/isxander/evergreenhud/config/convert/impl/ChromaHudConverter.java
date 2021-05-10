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
import co.uk.isxander.xanderlib.utils.Position;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChromaHudConverter extends ConfigConverter {

    public static final String CONFIG_FILE = "ChromaHUD.cfg";
    public static final Map<String, String> IDENTIFIER_MAP = new HashMap<String, String>() {{
        put("CORDS", "COORDS");
        put("ARROW_COUNT", null);
        put("POTION", null);
        put("PING", "PING");
        put("DIRECTION", "DIRECTION");
        put("CPS", "CPS");
        put("FPS", "FPS");
        put("TEXT", "TEXT");
        put("TIME", "TIME");
        put("ARMOUR_HUD", "ARMOUR");
        put("C_COUNTER", "ENTITY_COUNT"); // ik its not the same thing
    }};

    private final BetterJsonObject chromaHudConfig;
    private int failures;

    public ChromaHudConverter(File configFolder) {
        super(configFolder);
        this.failures = 0;
        try {
            this.chromaHudConfig = BetterJsonObject.getFromFile(new File(getConfigFolder(), CONFIG_FILE));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReportedException(CrashReport.makeCrashReport(e, "Could not process ChromaHUD configuration file."));
        }

    }

    @Override
    public void process(ElementManager manager) {
        processMain(manager);
        processElements(manager);

        manager.getMainConfig().save();
        manager.getElementConfig().save();

        Notifications.INSTANCE.pushNotification("EvergreenHUD", "ChromaHUD conversion has been completed.\n"
            + failures + " failures encountered.");
    }

    private void processMain(ElementManager manager) {
        manager.setEnabled(chromaHudConfig.optBoolean("enabled", true));
    }

    private void processElements(ElementManager manager) {
        JsonArray elementArray = chromaHudConfig.get("elements").getAsJsonArray();
        ScaledResolution res = new ScaledResolution(mc);

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
                element.setPosition(position);
                element.getPosition().setScaledY(element.getPosition().getYScaled() + (i * changeY));
                element.setTextColor(textCol.getRed(), textCol.getGreen(), textCol.getBlue());
                if (useBg) {
                    element.getPaddingWidth().set(1);
                    element.getPaddingHeight().set(1);
                } else {
                    element.setBgColor(0, 0, 0, 0);
                }
                element.useChroma().set(chroma);
                element.renderShadow().set(shadow);

                if (element instanceof ElementCoordinates) {
                    ElementCoordinates coords = (ElementCoordinates) element;
                    coords.type.set(item.optInt("state", 1) == 1 ? "Vertical" : "Horizontal");
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
