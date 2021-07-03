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

import co.uk.isxander.evergreenhud.EvergreenHUD;
import co.uk.isxander.xanderlib.utils.Constants;
import com.google.common.collect.Sets;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class ElementUtilitySharer implements Constants {

    private final Map<Class<?>, Object> dataManagers;
    private final Map<Class<?>, Set<Object>> managerUsers;

    public ElementUtilitySharer() {
        this.dataManagers = new ConcurrentHashMap<>();
        this.managerUsers = new ConcurrentHashMap<>();
    }

    public <T> T register(Class<T> clazz, Object o) {
        try {
            if (managerUsers.containsKey(clazz)) {
                managerUsers.get(clazz).add(o);
            } else {
                managerUsers.put(clazz, Sets.newHashSet(o));
            }

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

    public void unregisterAllForObject(Object o) {
        List<Class<?>> toRemove = new ArrayList<>();
        managerUsers.forEach((k, v) -> {
            if (v.contains(o)) {
                if (v.size() == 1) {
                    toRemove.add(k);
                }
                v.remove(o);
            }
        });

        for (Class<?> clazz : toRemove) {
            EvergreenHUD.LOGGER.info("Unregistered Utility Class: " + clazz.getSimpleName());

            MinecraftForge.EVENT_BUS.unregister(dataManagers.get(clazz));
            dataManagers.remove(clazz);
            managerUsers.remove(clazz);
        }
    }

    public <T> T getManager(Class<T> clazz) {
        return (T) dataManagers.get(clazz);
    }

}
