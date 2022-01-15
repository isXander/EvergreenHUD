/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import dev.isxander.evergreenhud.utils.elementmeta.ElementListJson
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class ElementProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>,
): SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation(ElementMeta::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()

        if (!symbols.iterator().hasNext()) return emptyList()

        val elements = mutableListOf<ElementListJson>()
        for (classDeclaration in symbols) {
            val annotation = classDeclaration.annotations.first {
                it.shortName.asString() == ElementMeta::class.simpleName
            }

            val meta = annotation.arguments
                .associate { it.name!!.asString() to (it.value?.let(this::getPrimitive) ?: JsonNull) }
                .let(::JsonObject)

            elements.add(ElementListJson(classDeclaration.qualifiedName!!.asString(), meta))
        }

        val file = codeGenerator.createNewFile(
            dependencies = Dependencies.ALL_FILES,
            packageName = "",
            fileName = "evergreenhud-elements",
            extensionName = "json",
        )

        file.use { it.write(Json.encodeToString(elements).toByteArray()) }

        return symbols.filterNot { it.validate() }.toList()
    }

    private fun getPrimitive(any: Any): JsonPrimitive {
        return when (any) {
            is String -> JsonPrimitive(any)
            is Number -> JsonPrimitive(any)
            is Boolean -> JsonPrimitive(any)
            else -> error("Not json primitive!")
        }
    }

}
