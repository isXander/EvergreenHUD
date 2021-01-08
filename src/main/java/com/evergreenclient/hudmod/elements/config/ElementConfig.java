/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.config;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.settings.Setting;
import com.evergreenclient.hudmod.settings.impl.BooleanSetting;
import com.evergreenclient.hudmod.settings.impl.DoubleSetting;
import com.evergreenclient.hudmod.settings.impl.IntegerSetting;
import com.evergreenclient.hudmod.utils.Alignment;
import com.evergreenclient.hudmod.utils.json.BetterJsonObject;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.File;

public class ElementConfig {

    private final Element element;
    public final File configFile;


    public ElementConfig(Element e) {
        this.element = e;
        this.configFile = new File(Minecraft.getMinecraft().mcDataDir, "config/evergreenhud/" + element.getMetadata().getName() + ".json");
    }

    public void save() {
        BetterJsonObject root = new BetterJsonObject();
        root.addProperty("enabled", element.isEnabled());
        root.addProperty("x", element.getPosition().x);
        root.addProperty("y", element.getPosition().y);
        root.addProperty("scale", element.getPosition().scale);
        root.addProperty("title", element.showTitle());
        root.addProperty("brackets", element.showBrackets());
        root.addProperty("inverted", element.isInverted());
        root.addProperty("textColor", element.getTextColor().getRGB());
        root.addProperty("bgColor", element.getBgColor().getRGB());
        root.addProperty("shadow", element.renderShadow());
        root.addProperty("alignment", element.getAlignment().ordinal());
        BetterJsonObject custom = new BetterJsonObject();
        for (Setting s : element.getCustomSettings()) {
            if (s instanceof BooleanSetting)
                custom.addProperty(s.getJsonKey(), ((BooleanSetting)s).get());
            else if (s instanceof IntegerSetting)
                custom.addProperty(s.getJsonKey(), ((IntegerSetting)s).get());
            else if (s instanceof DoubleSetting)
                custom.addProperty(s.getJsonKey(), ((DoubleSetting)s).get());
        }
        root.add("custom", custom);
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
        element.setEnabled(root.optBoolean("enabled"));
        element.getPosition().x = root.optInt("x");
        element.getPosition().y = root.optInt("y");
        element.getPosition().scale = root.optInt("scale");
        element.setTitle(root.optBoolean("title"));
        element.setBrackets(root.optBoolean("brackets"));
        element.setInverted(root.optBoolean("inverted"));
        element.setTextColor(new Color(root.optInt("textColor")));
        element.setBgColor(new Color(root.optInt("bgColor")));
        element.setShadow(root.optBoolean("shadow"));
        element.setAlignment(Alignment.values()[root.optInt("alignment")]);
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
                    break;
                }
            }
        }
    }

}
