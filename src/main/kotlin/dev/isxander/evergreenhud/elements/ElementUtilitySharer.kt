/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements

import dev.isxander.evergreenhud.utils.logger
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@Suppress("UNCHECKED_CAST")
class ElementUtilitySharer {
    var dataManagers: ConcurrentHashMap<KClass<*>, Any> = ConcurrentHashMap()
        private set
    var managerUsers: ConcurrentHashMap<KClass<*>, HashSet<Any>> = ConcurrentHashMap()
        private set

    inline fun <reified T : Any> register(o: Any): T? {
        try {
            val clazz = T::class
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
            logger.info("Unregistered Utility Class: " + clazz.simpleName)
            dataManagers.remove(clazz)
            managerUsers.remove(clazz)
        }
    }

    inline fun <reified T : Any> get(): T {
        return dataManagers[T::class] as T
    }

}
