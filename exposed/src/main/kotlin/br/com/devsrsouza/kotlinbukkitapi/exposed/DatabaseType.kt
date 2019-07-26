package br.com.devsrsouza.kotlinbukkitapi.exposed

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.net.URLClassLoader
import java.sql.SQLException
import kotlin.reflect.KClass

sealed class DatabaseType(
        val plugin: Plugin,
        val name: String,
        val jdbc: String,
        val driverClass: String,
        val driverLink: String
) {

    companion object {
        val fileDatabases: Map<String, KClass<out DatabaseType>>
                = mapOf("h2" to H2::class, "sqlite" to SQLite::class)
        val remoteDatabases: Map<String, KClass<out DatabaseType>>
                = mapOf("mysql" to MySQL::class, "postgresql" to PostgreSQL::class, "sqlserver" to SQLServer::class)
        val databases: Map<String, KClass<out DatabaseType>> = fileDatabases + remoteDatabases

        fun byName(name: String) = databases[name.toLowerCase()]
    }

    abstract fun dataSource(): HikariDataSource

    abstract class FileDatabaseType(
            plugin: Plugin,
            name: String,
            jdbc: String,
            driverClass: String,
            driverLink: String,
            val file: String,
            val databaseExtension: String,
            val needFileCreation: Boolean
    ) : DatabaseType(plugin, name, jdbc, driverClass, driverLink) {

        override fun dataSource(): HikariDataSource {
            val file = File(plugin.dataFolder, "$file.$databaseExtension")
            if(needFileCreation && !file.exists()) file.createNewFile()

            loadDependency()

            return HikariDataSource(HikariConfig().apply {
                jdbcUrl = jdbc
            })
        }
    }

    abstract class RemoteDatabaseType(
            plugin: Plugin,
            name: String,
            jdbc: String,
            driverClass: String,
            driverLink: String,
            val hostname: String,
            val port: Short,
            val database: String,
            val username: String,
            val password: String
    ) : DatabaseType(plugin, name, jdbc, driverClass, driverLink) {
        override fun dataSource(): HikariDataSource {
            loadDependency()

            return HikariDataSource(HikariConfig().apply {
                jdbcUrl = jdbc
                username = this@RemoteDatabaseType.username
                password = this@RemoteDatabaseType.password
            })
        }
    }

    class H2(
            plugin: Plugin,
            file: String
    ) : FileDatabaseType(
            plugin,
            "H2",
            "jdbc:h2:file:$file.h2.db",
            "org.h2.Driver",
            "http://repo.apache.maven.org/maven2/com/microsoft/sqlserver/mssql-jdbc/6.4.0.jre8/mssql-jdbc-6.4.0.jre8.jar",
            file,
            "h2.db",
            false
    )

    class SQLite(
            plugin: Plugin,
            file: String
    ) : FileDatabaseType(
            plugin,
            "SQLite",
            "jdbc:sqlite:$file.sqlite.db",
            "org.sqlite.JDBC",
            "https://bitbucket.org/xerial/sqlite-jdbc/downloads/sqlite-jdbc-3.23.1.jar",
            file,
            "sqlite.db",
            true
    )

    class MySQL(
            plugin: Plugin,
            hostname: String,
            port: Short,
            database: String,
            username: String,
            password: String
    ) : RemoteDatabaseType(
            plugin,
            "MySQL",
            "jdbc:mysql://$hostname:$port/$database",
            "com.mysql.jdbc.Driver",
            "http://repo.apache.maven.org/maven2/mysql/mysql-connector-java/8.0.11/mysql-connector-java-8.0.11.jar",
            hostname, port, database,
            username, password
    )

    class PostgreSQL(
            plugin: Plugin,
            hostname: String,
            port: Short,
            database: String,
            username: String,
            password: String
    ) : RemoteDatabaseType(
            plugin,
            "PostgreSQL",
            "jdbc:postgresql://$hostname:$port/$database",
            "org.postgresql.Driver",
            "https://jdbc.postgresql.org/download/postgresql-42.2.2.jre7.jar",
            hostname, port, database,
            username, password
    )

    class SQLServer(
            plugin: Plugin,
            hostname: String,
            port: Short,
            database: String,
            username: String,
            password: String
    ) : RemoteDatabaseType(
            plugin,
            "SQLServer",
            "jdbc:sqlserver://$hostname:$port;databaseName=$database",
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            "http://repo.apache.maven.org/maven2/com/microsoft/sqlserver/mssql-jdbc/6.4.0.jre8/mssql-jdbc-6.4.0.jre8.jar",
            hostname, port, database,
            username, password
    )

    private val libsFolder = File("klibs").apply { mkdirs() }
    private val jarFile = File(libsFolder, "$name-Driver.jar")

    protected fun loadDependency() {

        try {
            Class.forName(driverClass)
        }catch (e: ClassNotFoundException) {
            if(jarFile.exists())  {
                loadDriver()
            } else {
                try {
                    downloadDriver()
                } catch (e: Exception) {
                    throw SQLException("Cant download the driver dependencie of $name")
                }
                try {
                    loadDriver()
                }catch (e: Exception) {
                    jarFile.delete()
                    throw SQLException("Cant load the driver dependencies of $name")
                }
            }
        }
    }

    private fun loadDriver() {
        val url = jarFile.toURI().toURL()
        val classLoader = DatabaseType::class.java.classLoader as URLClassLoader
        val method = URLClassLoader::class.java.getDeclaredMethod("addURL", URL::class.java)
        method.isAccessible = true
        method.invoke(classLoader, url)
    }

    private fun downloadDriver() {
        val input = URL(driverLink).openStream()
        if (jarFile.exists()) {
            if (jarFile.isDirectory)
                throw IOException("File '" + jarFile.name + "' is a directory")

            if (!jarFile.canWrite())
                throw IOException("File '" + jarFile.name + "' cannot be written")
        } else {
            val parent = jarFile.parentFile
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                throw IOException("File '" + jarFile.name + "' could not be created")
            }
        }

        val output = FileOutputStream(jarFile)

        val buffer = ByteArray(4096)
        var n = 0
        do {
            n = input.read(buffer)
            output.write(buffer, 0, n)
        } while (-1 != n)

        input.close()
        output.close()
    }
}