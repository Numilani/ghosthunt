package me.numilani.ghosthunt

import com.bergerkiller.bukkit.common.PluginBase
import com.bergerkiller.bukkit.common.cloud.CloudSimpleHandler
import me.numilani.ghosthunt.commands.ArenaCommands
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class Ghosthunt() : PluginBase() {
    lateinit var DbConn: Connection
    var Handler: CloudSimpleHandler = CloudSimpleHandler()

    override fun enable() {
        // setup and connect to DB storage
        DbConn = try {
            val dbfile = "${dataFolder}${File.separatorChar}sample.db"
            var dbExists = File(dbfile).exists()
            if (!dbExists){
                InitializeDatabase(dbfile)
            }
            DriverManager.getConnection("jdbc:sqlite:$dbfile")
        } catch (e: SQLException) {
            DbConn.close()
            throw RuntimeException(e)
        }

        // Parse and register all commands
        Handler.enable(this)
        Handler.parser.parse(ArenaCommands(this))

    }

    fun InitializeDatabase(dbfn:String){
        var conn = DriverManager.getConnection("jdbc:sqlite:$dbfn")
        var s = conn.createStatement()
        s.execute("CREATE TABLE Arenas (Id INTEGER PRIMARY KEY, Name TEXT UNIQUE, GhostStartPts TEXT, PlayerStartPts TEXT, LobbyStartPts TEXT)")
        conn.close()
    }

    override fun disable() {
        DbConn.close()
    }

    override fun command(sender: CommandSender?, command: String?, args: Array<out String>?): Boolean {
        return true;
    }

    override fun getMinimumLibVersion(): Int {
        return 11902
    }

    /**
     * @return Connection, or null
     */
    fun getDbConnection():Connection{
        return DbConn
    }

}