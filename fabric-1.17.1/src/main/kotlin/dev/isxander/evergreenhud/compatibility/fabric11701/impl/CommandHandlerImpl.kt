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

package dev.isxander.evergreenhud.compatibility.fabric11701.impl

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.isxander.evergreenhud.compatibility.universal.impl.Command
import dev.isxander.evergreenhud.compatibility.universal.impl.UCommandHandler
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource

class CommandHandlerImpl : UCommandHandler() {
    override fun registerCommand(command: Command) {
        ClientCommandManager.DISPATCHER.register(LiteralArgumentBuilder.literal<FabricClientCommandSource?>(command.invoke).executes {
            command.executor()
            return@executes 0
        })
    }
}