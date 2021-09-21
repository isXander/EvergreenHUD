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
            dependencies = Dependencies(false),
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
