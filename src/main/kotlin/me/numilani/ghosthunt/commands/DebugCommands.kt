package me.numilani.ghosthunt.commands

import cloud.commandframework.annotations.CommandMethod
import me.numilani.ghosthunt.Ghosthunt
import me.numilani.ghosthunt.extensions.LocationExtensions
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DebugCommands(var plugin: Ghosthunt) {

    @CommandMethod("ghdb getloc")
    fun GetLocation(sender: CommandSender){
        var player = sender as Player
        var serialize = player.getLocation().serialize().toString()
        player.sendMessage("Serialize + toString: $serialize")
        var loc = LocationExtensions.fromString(serialize)
        loc.y = loc.y + 5
        player.teleport(loc)
    }
}