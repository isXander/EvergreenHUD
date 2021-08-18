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

package dev.isxander.evergreenhud.repo

import com.asarkar.semver.SemVer
import com.uchuhimo.konf.Config

object RepoManager {

    private const val jsonUrl = "https://dl.isxander.dev/mods/evergreenhud/info.json"

    fun getResponse(): RepoResponse {
        val data = Config { addSpec(ResponseSpec) }
            .from.json.url(jsonUrl)

        return RepoResponse(SemVer.parse(data[ResponseSpec.latest]), data[ResponseSpec.blacklisted])
    }

}

data class RepoResponse(val latest: SemVer, val blacklisted: List<String>)