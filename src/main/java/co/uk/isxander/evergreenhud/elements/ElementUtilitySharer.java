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

package co.uk.isxander.evergreenhud.elements;

import co.uk.isxander.xanderlib.utils.Constants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ElementUtilitySharer implements Constants {

    private final Map<Class<?>, Object> dataManagers;

    public ElementUtilitySharer() {
        this.dataManagers = new ConcurrentHashMap<>();
    }

    public <T> T register(Class<T> clazz) {
        try {
            if (this.dataManagers.containsKey(clazz)) {
                return (T) this.dataManagers.get(clazz);
            }
            T instance = clazz.newInstance();
            this.dataManagers.putIfAbsent(clazz, instance);
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T getManager(Class<T> clazz) {
        return (T) dataManagers.get(clazz);
    }

}
