package me.numilani.ghosthunt.objects

import org.bukkit.Bukkit
import java.sql.Connection
import java.sql.SQLException

data class Arena(
    var Id: Int?,
    var name: String,
    var GhostStartPts: List<String> = ArrayList(),
    var PlayerStartPts: List<String> = ArrayList(),
    var LobbyStartPts: List<String> = ArrayList()
) {
    fun toDTO(): ArenaDTO {
        return ArenaDTO(
            Id,
            name,
            GhostStartPts.joinToString { "|" },
            PlayerStartPts.joinToString { "|" },
            LobbyStartPts.joinToString { "|" })
    }
}

data class ArenaDTO(
    var Id: Int?,
    var name: String = "",
    var GhostStartPts: String = "",
    var PlayerStartPts: String = "",
    var LobbyStartPts: String = ""
) {
    fun toArena(): Arena {
        return Arena(
            Id,
            name,
            GhostStartPts.split("|").toList(),
            PlayerStartPts.split("|").toList(),
            LobbyStartPts.split("|").toList()
        )
    }

    fun save(conn: Connection): Boolean {
        var s = conn.createStatement()
        if (Id == null) {
            try{
                var x =
                    s.executeUpdate("INSERT INTO Arenas (Name, GhostStartPts, PlayerStartPts, LobbyStartPts) values ('$name', '$GhostStartPts', '$PlayerStartPts', '$LobbyStartPts')")
                s.close()
                return x >= 1
            } catch (ex: SQLException){
                Bukkit.getLogger().warning(ex.message)
                throw Exception("Couldn't save new arena. Likely an arena with that name already exists, check logs for details.")
            }
        } else {
            try{
                var x =
                    s.executeUpdate("UPDATE Arenas SET Name = '$name', GhostStartPts = '$GhostStartPts', PlayerStartPts = '$PlayerStartPts', LobbyStartPts = '$LobbyStartPts' where Id = $Id")
                s.close()
                return x >= 1
            } catch (ex: SQLException){
                Bukkit.getLogger().warning(ex.message)
                throw Exception("Couldn't update arena, check data and try again (see logs for details)")
            }
        }
    }
}
