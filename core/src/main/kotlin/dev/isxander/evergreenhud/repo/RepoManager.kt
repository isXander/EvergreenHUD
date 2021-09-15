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

import com.github.zafarkhaja.semver.Version
import dev.isxander.evergreenhud.utils.jsonParser
import java.net.URL

object RepoManager {
    private val jsonUrl = URL("https://dl.isxander.dev/mods/evergreenhud/info.json")

    fun getResponse(): RepoResponse {
        val data = jsonParser.parse(jsonUrl)

        return RepoResponse(Version.valueOf(data["latest"]), data["blacklisted"])
    }

}

data class RepoResponse(val latest: Version, val blacklisted: List<String>)