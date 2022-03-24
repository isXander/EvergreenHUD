/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.metrics

import com.mojang.authlib.minecraft.UserApiService
import dev.isxander.evergreenhud.mixins.AccessorMinecraftClient
import dev.isxander.evergreenhud.utils.http
import dev.isxander.evergreenhud.utils.logger
import dev.isxander.evergreenhud.utils.mc
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import net.fabricmc.loader.api.FabricLoader

object UniqueUsersMetric {
    fun url(uuid: String) =
        "https://api.isxander.dev/metric/put/evergreenhud?type=users&uuid=$uuid"

    suspend fun putApi() {
        if ((mc as AccessorMinecraftClient).userApiService == UserApiService.OFFLINE) {
            if (!FabricLoader.getInstance().isDevelopmentEnvironment)
                logger.warn("Minecraft appears to be cracked! Please just buy Minecraft!!")
            return
        }

        try {
            val url = url(mc.session.profile.id.toString())
            val response = http.get(url).body<GenericSuccessResponse>()
            if (!response.success) {
                logger.error("Metric API could not be called: ${response.error}")
                return
            }

            logger.debug("Metric API called successfully.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Serializable
    data class GenericSuccessResponse(val success: Boolean, val error: String? = null)
}
