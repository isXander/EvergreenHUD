/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.elements

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@Suppress("UNCHECKED_CAST")
class ElementUtilitySharer {

    private var dataManagers: ConcurrentHashMap<KClass<*>, Any> = ConcurrentHashMap()
    private var managerUsers: ConcurrentHashMap<KClass<*>, HashSet<Any>> = ConcurrentHashMap()

    fun <T : Any> register(clazz: KClass<T>, o: Any): T? {
        try {
            if (managerUsers.containsKey(clazz)) {
                managerUsers[clazz]!!.add(o)
            } else {
                managerUsers[clazz] = hashSetOf(o)
            }
            if (dataManagers.containsKey(clazz)) {
                return dataManagers[clazz] as T
            }
            val instance = clazz.createInstance()
            dataManagers.putIfAbsent(clazz, instance)
            return instance
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun unregisterAllForObject(o: Any) {
        val toRemove: ArrayList<KClass<*>> = ArrayList()
        managerUsers.forEach { (k: KClass<*>, v: HashSet<Any>) ->
            if (v.contains(o)) {
                if (v.size == 1) {
                    toRemove.add(k)
                }
                v.remove(o)
            }
        }
        for (clazz in toRemove) {
            LOGGER.info("Unregistered Utility Class: " + clazz.simpleName)
            EvergreenHUD.EVENT_BUS.unregister(dataManagers[clazz]!!)
            dataManagers.remove(clazz)
            managerUsers.remove(clazz)
        }
    }

    fun <T : Any> getManager(clazz: KClass<T>): T {
        return dataManagers[clazz] as T
    }

}