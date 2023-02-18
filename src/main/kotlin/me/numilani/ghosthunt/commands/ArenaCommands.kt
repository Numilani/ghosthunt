package me.numilani.ghosthunt.commands

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.suggestions.Suggestions
import cloud.commandframework.context.CommandContext
import me.numilani.ghosthunt.Ghosthunt
import me.numilani.ghosthunt.objects.Arena
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ArenaCommands(var plugin: Ghosthunt) {

    @Suggestions("ArenaEditSuggestions")
    fun ArenaEditSuggestions(ctx: CommandContext<Player>, input:String): List<String> {
        var completions = listOf<String>("ghost-spawn", "survivor-spawn", "lobby-spawn")
        return completions
    }

    @CommandMethod("gh arena new <arenaName>")
    fun CreateArena(sender: CommandSender, @Argument("arenaName") arenaName: String) {
        try{
            var x = Arena(null, arenaName).toDTO().save(plugin.getDbConnection())
            sender.sendMessage(ChatColor.DARK_GREEN.toString() + "Arena $arenaName created!")
        } catch(ex: Exception){
            sender.sendMessage(ChatColor.DARK_RED.toString() + "${ex.message}")
        }
    }

    @CommandMethod("gh arena list")
    fun GetArenas(sender: CommandSender) {
        var arenasList = ""
        var queryResult = plugin.getDbConnection().createStatement().executeQuery("SELECT Name FROM Arenas")
        while (queryResult.next()) {
            arenasList += "${queryResult.getString("Name")},"
        }
        if (arenasList.length > 1) {
            sender.sendMessage("Available Arenas: ${arenasList.removeSuffix(",")}")
        } else sender.sendMessage("No Arenas Available!")
    }

    @CommandMethod("gh arena setpos <positiontype>")
    fun AddArenaPosition(sender: CommandSender, @Argument(value = "positiontype", suggestions = "ArenaEditSuggestions") positiontype: String){
        when(positiontype){
            "ghost-spawn" -> {}
            "survivor-spawn" -> {}
            "lobby-spawn" -> {}
            else -> sender.sendMessage(ChatColor.DARK_RED.toString() + "Unknown position type")
        }
    }

//    @CommandMethod("gh arena clearpos <positiontype>")
//    fun ClearArenaPosition()
}
