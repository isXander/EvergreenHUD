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
