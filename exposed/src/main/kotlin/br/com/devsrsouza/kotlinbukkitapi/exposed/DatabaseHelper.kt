package br.com.devsrsouza.kotlinbukkitapi.exposed

import com.zaxxer.hikari.HikariDataSource
import org.bukkit.plugin.Plugin
import java.sql.SQLException

data class DatabaseConfig(
        var type: String = "H2",
        var hostname: String = "localhost",
        var port: Short = 3306,
        var database: String = "kbapi_database",
        var user: String = "root",
        var password: String = "12345"
)

fun databaseFrom(plugin: Plugin, config: DatabaseConfig) : Database {
    val type = Database.byName(config.type)
    if (type != null) {
        return when(type) {
            Database.H2::class -> Database.H2(
                    plugin,
                    config.database
            )
            Database.SQLite::class -> Database.SQLite(
                    plugin,
                    config.database
            )
            Database.MySQL::class -> Database.MySQL(
                    plugin,
                    config.hostname,
                    config.port,
                    config.database,
                    config.user,
                    config.password
            )
            Database.PostgreSQL::class -> Database.PostgreSQL(
                    plugin,
                    config.hostname,
                    config.port,
                    config.database,
                    config.user,
                    config.password
            )
            Database.SQLServer::class -> Database.SQLServer(
                    plugin,
                    config.hostname,
                    config.port,
                    config.database,
                    config.user,
                    config.password
            )
            else -> throw SQLException("SQL type not finded or supported.")
        }
    } else throw SQLException("SQL type not finded or supported.")
}