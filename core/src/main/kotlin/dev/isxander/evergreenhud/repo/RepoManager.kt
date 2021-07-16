/*
 * Copyright (C) isXander [2019 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.repo

import com.google.gson.JsonArray
import com.google.gson.JsonPrimitive
import dev.isxander.evergreenhud.EvergreenInfo
import dev.isxander.evergreenhud.utils.HttpsUtils
import dev.isxander.evergreenhud.utils.JsonObjectExt
import java.lang.Exception

object RepoManager {

    private const val jsonUrl = "https://raw.githubusercontent.com/isXander/EvergreenHUD/main/blacklisted.json"

    fun getResponse(): RepoResponse {
        val out = try { HttpsUtils.getString(jsonUrl) }
        catch (e: Exception) { return RepoResponse(outdated = false, blacklisted = false) }

        val json = JsonObjectExt(out)

        val blacklisted = json["blacklisted", JsonArray()]!!.contains(JsonPrimitive(EvergreenInfo.MOD_REVISION))
        val outdated = json["latest", EvergreenInfo.MOD_REVISION]!!.equals(EvergreenInfo.MOD_REVISION, ignoreCase = true)
        return RepoResponse(outdated, blacklisted)
    }

}

data class RepoResponse(val outdated: Boolean, val blacklisted: Boolean)