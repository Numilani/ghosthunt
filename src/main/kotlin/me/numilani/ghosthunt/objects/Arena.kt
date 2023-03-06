package me.numilani.ghosthunt.objects

import org.bukkit.Bukkit
import java.sql.Connection
import java.sql.SQLException

data class Arena(
    var Id: Int?,
    var name: String,
    var GhostStartPts: MutableList<String> = mutableListOf(),
    var PlayerStartPts: MutableList<String> = mutableListOf(),
    var LobbyStartPts: MutableList<String> = mutableListOf()
) {
    fun toDTO(): ArenaDTO {
        return ArenaDTO(
            Id,
            name,
            GhostStartPts.joinToString { ";" },
            PlayerStartPts.joinToString { ";" },
            LobbyStartPts.joinToString { ";" })
    }
}

data class ArenaDTO(
    var Id: Int?,
    var name: String = "",
    var GhostStartPts: String = "",
    var PlayerStartPts: String = "",
    var LobbyStartPts: String = ""
) {
    companion object{
        fun getById(conn: Connection, Id: Int): ArenaDTO? {
            var record: ArenaDTO? = null

            var result = conn.createStatement().executeQuery("SELECT Id, Name, GhostStartPts, PlayerStartPts, LobbyStartPts FROM Arenas WHERE Id = $Id")
            while (result.next()){
                var id = result.getInt(1)
                var name = result.getString(2)
                var ghostPts = result.getString(3)
                var playerPts = result.getString(4)
                var lobbyPts = result.getString(5)
                record = ArenaDTO(id, name, ghostPts, playerPts, lobbyPts)
            }
            return record
        }

        fun getByName(conn: Connection, Name: String): ArenaDTO? {
            var record: ArenaDTO? = null

            var result = conn.createStatement().executeQuery("SELECT Id, Name, GhostStartPts, PlayerStartPts, LobbyStartPts FROM Arenas WHERE Name = '$Name'")
            while (result.next()){
                var id = result.getInt(1)
                var name = result.getString(2)
                var ghostPts = result.getString(3)
                var playerPts = result.getString(4)
                var lobbyPts = result.getString(5)
                record = ArenaDTO(id, name, ghostPts, playerPts, lobbyPts)
            }
            return record
        }
    }
    fun toArena(): Arena {
        var returnValue = Arena(Id, name)
        if (GhostStartPts.length > 0) GhostStartPts.split(";").toMutableList()
        if (PlayerStartPts.length > 0) PlayerStartPts.split(";").toMutableList()
        if (LobbyStartPts.length > 0) LobbyStartPts.split(";").toMutableList()
        return returnValue
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
