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

import com.google.gson.JsonArray
import com.google.gson.JsonPrimitive
import dev.isxander.evergreenhud.EvergreenInfo
import dev.isxander.evergreenhud.utils.HttpsUtils
import dev.isxander.evergreenhud.utils.JsonObjectExt
import java.lang.Exception

object RepoManager {

    private const val jsonUrl = "https://dl.isxander.dev/mods/evergreenhud/blacklisted.json"

    fun getResponse(): RepoResponse {
        val out = try { HttpsUtils.getString(jsonUrl) }
        catch (e: Exception) { return RepoResponse(outdated = false, blacklisted = false) }

        val json = JsonObjectExt(out)

        val blacklisted = json["blacklisted", JsonArray()]!!.contains(JsonPrimitive(EvergreenInfo.REVISION))

        val latestJson = json["latest", JsonObjectExt()]!!
        val major = latestJson["major", 1]
        val minor = latestJson["minor", 0]
        val patch = latestJson["patch", 0]
        val prerelease = latestJson["pre", -1]

        val outdated = EvergreenInfo.VERSION_MAJOR < major
                || EvergreenInfo.VERSION_MINOR < minor
                || EvergreenInfo.VERSION_PATCH < patch
                || EvergreenInfo.VERSION_PRERELEASE ?: -1 < prerelease
        return RepoResponse(outdated, blacklisted)
    }

}

data class RepoResponse(val outdated: Boolean, val blacklisted: Boolean)