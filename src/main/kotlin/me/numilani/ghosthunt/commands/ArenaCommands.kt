package me.numilani.ghosthunt.commands

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.suggestions.Suggestions
import cloud.commandframework.context.CommandContext
import me.numilani.ghosthunt.Ghosthunt
import me.numilani.ghosthunt.objects.Arena
import me.numilani.ghosthunt.objects.ArenaDTO
import net.md_5.bungee.api.ChatColor
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import javax.json.JsonObject

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

    @CommandMethod("gh arena info <arenaname>")
    fun GetArenaDetails(sender: CommandSender, @Argument("arenaname") arenaname: String){
        var arena = ArenaDTO.getByName(plugin.getDbConnection(), arenaname);
        if (arena == null) {
            sender.sendMessage("No arena '$arenaname' found")
            return
        }
        sender.sendMessage("ARENA: $arenaname")
        sender.sendMessage("LOBBY SPAWNPOINTS: ${arena.LobbyStartPts}")
        sender.sendMessage("GHOST SPAWNPOINTS: ${arena.GhostStartPts}")
        sender.sendMessage("SURVIVOR SPAWNPOINTS: ${arena.PlayerStartPts}")
    }

    @CommandMethod("gh arena <arenaname> setpos <positiontype>")
    fun AddArenaPosition(sender: CommandSender, @Argument("arenaname") arenaname: String,  @Argument(value = "positiontype", suggestions = "ArenaEditSuggestions") positiontype: String){
        var arena = ArenaDTO.getByName(plugin.getDbConnection(), arenaname)?.toArena();
        var playerSender = sender as Player
        var location = playerSender.getLocation().serialize().toString()
        when(positiontype){
            "ghost-spawn" -> {arena?.GhostStartPts?.add(location); arena?.toDTO()?.save(plugin.getDbConnection()); sender.sendMessage("Ghost spawnpoint added")}
            "survivor-spawn" -> {arena?.PlayerStartPts?.add(location); arena?.toDTO()?.save(plugin.getDbConnection()); sender.sendMessage("Survivor spawnpoint added")}
            "lobby-spawn" -> {arena?.LobbyStartPts?.add(location); arena?.toDTO()?.save(plugin.getDbConnection()); sender.sendMessage("Lobby spawnpoint added")}
            else -> sender.sendMessage(ChatColor.DARK_RED.toString() + "Unknown position type")
        }
    }

    @CommandMethod("gh arena <arenaname> clearpos <positiontype> [index]")
    fun ClearArenaPosition(sender: CommandSender, @Argument("arenaname") arenaname: String, @Argument(value = "positiontype", suggestions = "ArenaEditSuggestions") positiontype: String, @Argument("index") index: Int?){
        var arena = ArenaDTO.getByName(plugin.getDbConnection(), arenaname)?.toArena();
        when(positiontype){
            "ghost-spawn" -> {arena?.GhostStartPts?.removeAt(index ?: arena.GhostStartPts.lastIndex); arena?.toDTO()?.save(plugin.getDbConnection()); sender.sendMessage("Ghost spawnpoint removed")}
            "survivor-spawn" -> {arena?.PlayerStartPts?.removeAt(index ?: arena.PlayerStartPts.lastIndex); arena?.toDTO()?.save(plugin.getDbConnection()); sender.sendMessage("Survivor spawnpoint removed")}
            "lobby-spawn" -> {arena?.LobbyStartPts?.removeAt(index ?: arena.LobbyStartPts.lastIndex); arena?.toDTO()?.save(plugin.getDbConnection()); sender.sendMessage("Lobby spawnpoint removed")}
            else -> sender.sendMessage(ChatColor.DARK_RED.toString() + "Unknown position type")
        }
    }
}
