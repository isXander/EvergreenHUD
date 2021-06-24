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

package co.uk.isxander.evergreenhud.config;

import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.elements.ElementManager;
import co.uk.isxander.xanderlib.utils.Constants;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import co.uk.isxander.evergreenhud.EvergreenHUD;
import com.google.gson.JsonArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ElementConfig implements Constants {

    public static final int VERSION = 3;
    public static final File OLD_CONFIG_FOLDER = new File(EvergreenHUD.DATA_DIR, "elements");
    public static final File CONFIG_FILE = new File(EvergreenHUD.DATA_DIR, "elements.json");

    private final ElementManager manager;

    public ElementConfig(ElementManager manager) {
        this.manager = manager;
    }

    public BetterJsonObject generateJson() {
        BetterJsonObject root = new BetterJsonObject();
        root.addProperty("version", VERSION);

        JsonArray array = new JsonArray();
        for (Element element : manager.getCurrentElements()) {
            BetterJsonObject obj = new BetterJsonObject();
            obj.addProperty("type", element.getType());
            obj.add("settings", element.generateJson());
            array.add(obj.getData());
        }
        root.getData().add("elements", array);

        return root;
    }

    public void save() {
        BetterJsonObject root = generateJson();
        root.writeToFile(CONFIG_FILE);
    }

    public void load() {
        if (!CONFIG_FILE.exists() && OLD_CONFIG_FOLDER.exists()) {
            loadOld();

            save();
            return;
        }

        BetterJsonObject root;
        try {
            root = BetterJsonObject.getFromFile(CONFIG_FILE);
        } catch (Exception e) {
            save();
            return;
        }

        boolean v2 = false;
        if (root.optInt("version") == 2) {
            EvergreenHUD.LOGGER.warn("Converting Element Config v2 -> v3. Conversion issues may arise");
            v2 = true;
        } else if (root.optInt("version") != VERSION) {
            EvergreenHUD.LOGGER.warn("Resetting configuration! Older or newer config version detected.");
            EvergreenHUD.getInstance().notifyConfigReset();
            save();
            return;
        }

        JsonArray array = root.getData().getAsJsonArray("elements");
        boolean finalVer = v2;
        array.forEach((jsonElement) -> {
            BetterJsonObject elementConfig = new BetterJsonObject(jsonElement.getAsJsonObject());
            String id = elementConfig.optString("type");
            Element element = manager.getNewElementInstance(id);
            if (element != null) {
                BetterJsonObject settings = elementConfig.getObj("settings");
                if (finalVer) {
                    element.loadJsonOld(settings);
                } else {
                    element.loadJson(settings);
                }

                manager.addElement(element);
            } else {
                EvergreenHUD.LOGGER.warn("Found unknown element id: " + id + ". Skipped.");
            }
        });

        if (v2) save();
    }

    public void loadOld() {
        EvergreenHUD.LOGGER.info("Converting old configuration system.");

        List<Element> availableElements = new ArrayList<>();
        manager.getAvailableElements().forEach((name, clazz) ->
            availableElements.add(manager.getNewElementInstance(name))
        );

        for (File config : Objects.requireNonNull(OLD_CONFIG_FOLDER.listFiles())) {
            for (Element element : availableElements) {
                if (config.getName().equalsIgnoreCase(element.getMetadata().getName() + ".json")) {
                    BetterJsonObject root;
                    try {
                        root = BetterJsonObject.getFromFile(config);
                    } catch (Exception e) {
                        save();
                        return;
                    }

                    if (root.optBoolean("enabled")) {
                        element.loadJsonOld(root);

                        manager.addElement(element);
                    }

                    break;
                }
            }
            config.delete();
        }
        OLD_CONFIG_FOLDER.delete();
    }

}
