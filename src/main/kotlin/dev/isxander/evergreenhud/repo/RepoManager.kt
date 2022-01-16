/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.repo

import dev.isxander.evergreenhud.utils.http
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import org.bundleproject.libversion.Version

object RepoManager {
    private const val jsonUrl = "https://dl.isxander.dev/mods/evergreenhud/info.json"

    suspend fun getResponse(): RepoResponse {
        return http.get(jsonUrl).body()
    }
}

@Serializable
data class RepoResponse(val latest: Map<ReleaseChannel, Version>, val blacklisted: List<String>)
