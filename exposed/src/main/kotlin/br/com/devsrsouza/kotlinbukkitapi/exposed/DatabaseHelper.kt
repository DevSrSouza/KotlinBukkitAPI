package br.com.devsrsouza.kotlinbukkitapi.exposed

import org.bukkit.plugin.Plugin
import java.sql.SQLException

open class DatabaseTypeConfig(
        open var type: String = "H2",
        open var hostname: String = "localhost",
        open var port: Short = 3306,
        open var database: String = "kbapi_database",
        open var user: String = "root",
        open var password: String = "12345"
)

fun databaseTypeFrom(plugin: Plugin, config: DatabaseTypeConfig) : DatabaseType {
    val type = DatabaseType.byName(config.type)
    if (type != null) {
        return when(type) {
            DatabaseType.H2::class -> DatabaseType.H2(
                    plugin,
                    config.database
            )
            DatabaseType.SQLite::class -> DatabaseType.SQLite(
                    plugin,
                    config.database
            )
            DatabaseType.MySQL::class -> DatabaseType.MySQL(
                    plugin,
                    config.hostname,
                    config.port,
                    config.database,
                    config.user,
                    config.password
            )
            DatabaseType.PostgreSQL::class -> DatabaseType.PostgreSQL(
                    plugin,
                    config.hostname,
                    config.port,
                    config.database,
                    config.user,
                    config.password
            )
            DatabaseType.SQLServer::class -> DatabaseType.SQLServer(
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