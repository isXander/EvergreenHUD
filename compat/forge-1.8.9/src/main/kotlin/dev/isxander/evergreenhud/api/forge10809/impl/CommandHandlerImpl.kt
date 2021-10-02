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

package dev.isxander.evergreenhud.api.forge10809.impl

import dev.isxander.evergreenhud.api.impl.Command
import dev.isxander.evergreenhud.api.impl.UCommandHandler
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraftforge.client.ClientCommandHandler

class CommandHandlerImpl : UCommandHandler() {
    override fun registerCommand(command: Command) {
        ClientCommandHandler.instance.registerCommand(object : CommandBase() {
            override fun getCommandName(): String = command.invoke
            override fun getCommandUsage(sender: ICommandSender): String = "/$commandName"
            override fun getRequiredPermissionLevel(): Int = -1
            override fun processCommand(sender: ICommandSender, args: Array<out String>) = command.executor()
        })
    }
}
