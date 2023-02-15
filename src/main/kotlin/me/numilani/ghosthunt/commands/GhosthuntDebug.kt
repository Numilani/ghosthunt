package me.numilani.ghosthunt.commands

import cloud.commandframework.Command
import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandMethod
import me.numilani.ghosthunt.Ghosthunt
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GhosthuntDebug(ghosthunt: Ghosthunt) {
    var plugin: Ghosthunt = ghosthunt

    @CommandMethod("ghd")
    fun testCommand(sender: Player){
        sender.sendMessage("Your name is ${sender.name}!")
    }

    @CommandMethod("gh arena list")
    fun GetArenas(sender: CommandSender){
        var arenasList = ""
        var queryResult = plugin.getDbConnection().createStatement().executeQuery("SELECT Name FROM Arenas")
        while (queryResult.next()){
            arenasList += "${queryResult.getString("Name")},"
        }
        if (arenasList.length > 1){
            sender.sendMessage("Available Arenas: ${arenasList.removeSuffix(",")}")
        }
        else sender.sendMessage("No Arenas Available!")
    }

    @CommandMethod("gh arena new <arenaName>")
    fun CreateArena(sender: CommandSender, @Argument("arenaName") arenaName: String){
        var statement = plugin.getDbConnection().createStatement();
        var doesExist = statement.executeQuery("SELECT Name FROM Arenas WHERE Name = '$arenaName'")
        if (doesExist.fetchSize > 1){
            sender.sendMessage(ChatColor.DARK_RED.toString() + "An arena with that name already exists!")
        }
        else{
            var insResult = statement.executeUpdate("INSERT INTO Arenas (Name) VALUES ('$arenaName')")
            if (insResult < 1){
                sender.sendMessage(ChatColor.DARK_RED.toString() + "Failed to create arena! (Probably a SQLite error)")
            }
            else{
                sender.sendMessage(ChatColor.DARK_GREEN.toString() + "Arena $arenaName created!")
            }
        }
    }


}