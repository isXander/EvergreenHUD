/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.config;

import com.evergreenclient.hudmod.EvergreenHUD;
import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.settings.Setting;
import com.evergreenclient.hudmod.settings.impl.*;
import com.evergreenclient.hudmod.utils.Alignment;
import com.evergreenclient.hudmod.utils.BetterJsonObject;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ElementConfig {

    private static final int VERSION = 1;

    private final Element element;
    public final File configFile;
    private final Map<String, Object> customElements;

    public ElementConfig(Element e) {
        this.element = e;
        this.configFile = new File(Minecraft.getMinecraft().mcDataDir, "config/evergreenhud/elements/" + element.getMetadata().getName() + ".json");
        this.customElements = new HashMap<>();

        // Config has moved directories. So we don't need to reset all configs, just test if the old config is there and move it
        File oldLocation = new File(Minecraft.getMinecraft().mcDataDir, "config/evergreenhud/" + element.getMetadata().getName() + ".json");
        if (oldLocation.exists()) {
            try {
                FileUtils.moveFile(oldLocation, configFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private BetterJsonObject generateJson() {
        BetterJsonObject root = new BetterJsonObject();
        root.addProperty("version", VERSION);

        root.addProperty("enabled", element.isEnabled());
        root.addProperty("x", element.getPosition().getXScaled());
        root.addProperty("y", element.getPosition().getYScaled());
        root.addProperty("scale", element.getPosition().getScale());
        root.addProperty("title", element.showTitle());
        root.addProperty("brackets", element.showBrackets());
        root.addProperty("inverted", element.isInverted());
        root.addProperty("chroma", element.useChroma());
        root.addProperty("shadow", element.renderShadow());
        root.addProperty("alignment", element.getAlignment().ordinal());

        BetterJsonObject textCol = new BetterJsonObject();
        textCol.addProperty("r", element.getTextColor().getRed());
        textCol.addProperty("g", element.getTextColor().getGreen());
        textCol.addProperty("b", element.getTextColor().getBlue());
        root.add("textColor", textCol);

        BetterJsonObject bgCol = new BetterJsonObject();
        bgCol.addProperty("r", element.getBgColor().getRed());
        bgCol.addProperty("g", element.getBgColor().getGreen());
        bgCol.addProperty("b", element.getBgColor().getBlue());
        bgCol.addProperty("a", element.getBgColor().getAlpha());
        bgCol.addProperty("padding_width", element.getPaddingWidth());
        bgCol.addProperty("padding_height", element.getPaddingHeight());
        root.add("bgColor", bgCol);

        BetterJsonObject custom = new BetterJsonObject();
        for (Setting s : element.getCustomSettings()) {
            if (s instanceof BooleanSetting)
                custom.addProperty(s.getJsonKey(), ((BooleanSetting)s).get());
            else if (s instanceof IntegerSetting)
                custom.addProperty(s.getJsonKey(), ((IntegerSetting)s).get());
            else if (s instanceof DoubleSetting)
                custom.addProperty(s.getJsonKey(), ((DoubleSetting)s).get());
            else if (s instanceof ArraySetting)
                custom.addProperty(s.getJsonKey(), ((ArraySetting) s).getIndex());
            else if (s instanceof StringSetting)
                custom.addProperty(s.getJsonKey(), ((StringSetting)s).get());
        }
        root.add("custom", custom);

        BetterJsonObject manual = new BetterJsonObject();
        customElements.forEach((k, v) -> {
            if (v instanceof String) {
                manual.addProperty(k, (String)v);
            } else if (v instanceof Boolean) {
                manual.addProperty(k, (Boolean)v);
            } else if (v instanceof Number) {
                manual.addProperty(k, (Number)v);
            }
        });
        root.add("manual", manual);

        return root;
    }

    public void save() {
        BetterJsonObject root = generateJson();
        root.writeToFile(configFile);
    }

    public void load() {
        BetterJsonObject root;
        try {
            root = BetterJsonObject.getFromFile(configFile);
        } catch (Exception e) {
            save();
            return;
        }
        // new config version so we need to reset the config
        if (root.optInt("version") != VERSION) {
            element.getLogger().warn("Resetting configuration! Older or newer config version detected.");
            EvergreenHUD.getInstance().notifyConfigReset();
            save();
            return;
        }

        element.setEnabled(root.optBoolean("enabled"));
        element.getPosition().setScaledX(root.optFloat("x"));
        element.getPosition().setScaledY(root.optFloat("y"));
        element.getPosition().setScale(root.optFloat("scale", 1.0f));
        element.setTitle(root.optBoolean("title", true));
        element.setBrackets(root.optBoolean("brackets", false));
        element.setInverted(root.optBoolean("inverted", false));
        element.setChroma(root.optBoolean("chroma", false));
        element.setShadow(root.optBoolean("shadow", true));
        element.setAlignment(Alignment.values()[root.optInt("alignment", 0)]);

        BetterJsonObject textColor = new BetterJsonObject(root.get("textColor").getAsJsonObject());
        element.setTextColor(new Color(textColor.optInt("r", 255), textColor.optInt("g", 255), textColor.optInt("b", 255)));

        BetterJsonObject bgColor = new BetterJsonObject(root.get("bgColor").getAsJsonObject());
        element.setBgColor(new Color(bgColor.optInt("r", 255), bgColor.optInt("g", 255), bgColor.optInt("b", 255), bgColor.optInt("a", 255)));
        element.setPaddingWidth(bgColor.optFloat("padding_width", 4));
        element.setPaddingHeight(bgColor.optFloat("padding_height", 4));

        BetterJsonObject custom = new BetterJsonObject(root.get("custom").getAsJsonObject());
        for (String key : custom.getAllKeys()) {
            for (Setting s : element.getCustomSettings()) {
                if (s.getJsonKey().equals(key)) {
                    if (s instanceof BooleanSetting)
                        ((BooleanSetting) s).set(custom.optBoolean(key));
                    else if (s instanceof IntegerSetting)
                        ((IntegerSetting) s).set(custom.optInt(key));
                    else if (s instanceof DoubleSetting)
                        ((DoubleSetting) s).set(custom.optDouble(key));
                    else if (s instanceof ArraySetting)
                        ((ArraySetting) s).set(custom.optInt(key));
                    else if (s instanceof StringSetting)
                        ((StringSetting) s).set(custom.optString(key));
                    break;
                }
            }
        }
    }

}
