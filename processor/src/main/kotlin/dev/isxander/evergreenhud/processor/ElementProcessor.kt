/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.processor

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.json.JsonFormat
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import dev.isxander.evergreenhud.annotations.ElementMeta

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

        val elements = mutableListOf<Config>()
        for (classDeclaration in symbols) {
            val annotation = classDeclaration.annotations.first {
                it.shortName.asString() == ElementMeta::class.simpleName
            }

            val json = JsonFormat.newConfig()

            val meta = json.createSubConfig()
            annotation.arguments.forEach {
                meta.add(it.name?.asString(), it.value)
            }
            json.add("metadata", meta)
            json.add("class", classDeclaration.qualifiedName?.asString())

            elements.add(json)
        }

        val file = codeGenerator.createNewFile(
            dependencies = Dependencies(true),
            packageName = "",
            fileName = "evergreenhud-elements",
            extensionName = "json",
        )

        val config = JsonFormat.newConfig().apply { add("elements", elements) }
        JsonFormat.fancyInstance().createWriter().write(config, file)

        file.close()

        return symbols.filterNot { it.validate() }.toList()
    }

}
