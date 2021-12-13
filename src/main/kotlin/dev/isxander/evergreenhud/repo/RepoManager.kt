/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.repo

import dev.isxander.evergreenhud.utils.http
import io.ktor.client.request.*
import org.bundleproject.libversion.Version

object RepoManager {
    private val jsonUrl = "https://dl.isxander.dev/mods/evergreenhud/info.json"

    suspend fun getResponse(): RepoResponse {
        return http.get(jsonUrl)
    }

}

data class RepoResponse(val latest: Map<String, Version>, val blacklisted: List<String>)
