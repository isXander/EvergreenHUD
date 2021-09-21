/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.api.forge10809.utils

import dev.isxander.evergreenhud.api.forge10809.impl.EntityImpl
import dev.isxander.evergreenhud.api.impl.UEntity
import net.minecraft.entity.Entity
import java.lang.IllegalArgumentException

fun getEntity(entity: UEntity): Entity {
    if (entity.isNull) throw IllegalArgumentException("Entity was null!")
    if (entity !is EntityImpl) throw IllegalArgumentException("Entity is wrong implementation!")
    return entity.entity
}