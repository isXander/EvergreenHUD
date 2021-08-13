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

package dev.isxander.evergreenhud.compatibility.forge10809.impl

import dev.isxander.evergreenhud.compatibility.forge10809.utils.getEntity
import dev.isxander.evergreenhud.compatibility.universal.impl.UEntity
import dev.isxander.evergreenhud.compatibility.universal.impl.UPotion
import dev.isxander.evergreenhud.compatibility.universal.impl.UPotions
import net.minecraft.entity.EntityLiving
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import java.lang.IllegalArgumentException

class PotionsImpl : UPotions() {
    override val registeredPotions: List<UPotion> =
        Potion.potionTypes.map { UPotion(it.id, 0, 0, false, it.name) }

    override fun giveEntityEffect(entity: UEntity, potion: UPotion) {
        val entity = getEntity(entity) as? EntityLiving ?: throw IllegalArgumentException("Entity is not living!")
        entity.addPotionEffect(PotionEffect(potion.id, potion.duration, potion.amplifier))
    }
}