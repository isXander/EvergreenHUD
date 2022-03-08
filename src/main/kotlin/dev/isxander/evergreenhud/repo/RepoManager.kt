/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.repo

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.utils.http
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import net.minecraft.SharedConstants
import org.bundleproject.libversion.Version

object RepoManager {
    private fun url(loader: String, minecraft: String) =
        "https://api.isxander.dev/updater/latest/evergreenhud?loader=$loader&minecraft=$minecraft&version=2.0.0-alpha.5"

    suspend fun getResponse(): RepoResponse {
        return http.get(url(EvergreenHUD.LOADER, SharedConstants.getGameVersion().name)).body()
    }
}

@Serializable
data class RepoResponse(val success: Boolean, val latest: Version? = null)
