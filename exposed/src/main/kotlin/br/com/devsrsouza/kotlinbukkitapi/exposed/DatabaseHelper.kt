package br.com.devsrsouza.kotlinbukkitapi.exposed

import java.io.File
import java.sql.SQLException

open class DatabaseTypeConfig(
        open var type: String = "H2",
        open var hostname: String = "localhost",
        open var port: Short = 3306,
        open var database: String = "kbapi_database",
        open var user: String = "root",
        open var password: String = "12345"
)

fun databaseTypeFrom(dataFolder: File, config: DatabaseTypeConfig) : DatabaseType {
    val type = DatabaseType.byName(config.type)
    if (type != null) {
        return when(type) {
            DatabaseType.H2::class -> DatabaseType.H2(
                    dataFolder,
                    config.database
            )
            DatabaseType.SQLite::class -> DatabaseType.SQLite(
                    dataFolder,
                    config.database
            )
            DatabaseType.MySQL::class -> DatabaseType.MySQL(
                    config.hostname,
                    config.port,
                    config.database,
                    config.user,
                    config.password
            )
            DatabaseType.PostgreSQL::class -> DatabaseType.PostgreSQL(
                    config.hostname,
                    config.port,
                    config.database,
                    config.user,
                    config.password
            )
            DatabaseType.SQLServer::class -> DatabaseType.SQLServer(
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