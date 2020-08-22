package br.com.devsrsouza.kotlinbukkitapi.exposed

import kotlinx.serialization.Serializable
import java.io.File
import java.sql.SQLException

interface DatabaseTypeConfigHeader {
    val type: String
    val hostname: String
    val port: Short
    val database: String
    val user: String
    val password: String
}

@Serializable
data class DatabaseTypeConfig(
        override var type: String = "H2",
        override var hostname: String = "localhost",
        override var port: Short = 3306,
        override var database: String = "kbapi_database",
        override var user: String = "root",
        override var password: String = "12345"
) : DatabaseTypeConfigHeader

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