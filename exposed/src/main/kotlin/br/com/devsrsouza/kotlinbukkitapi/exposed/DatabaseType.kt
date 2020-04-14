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

private const val KEY_FILE = "{file}"

sealed class DatabaseType(
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

    abstract fun config(): HikariConfig

    abstract class FileDatabaseType(
            name: String,
            jdbc: String,
            driverClass: String,
            driverLink: String,
            val dataFolder: File,
            val file: String,
            val databaseExtension: String,
            val needFileCreation: Boolean
    ) : DatabaseType(name, jdbc, driverClass, driverLink) {

        private val realFile = File(dataFolder, "$file.$databaseExtension")

        override fun dataSource(): HikariDataSource {
            if(needFileCreation && !realFile.exists()) realFile.createNewFile()

            loadDependency()

            return HikariDataSource(config())
        }

        override fun config(): HikariConfig = HikariConfig().apply {
            driverClassName = driverClass
            jdbcUrl = jdbc.replace(KEY_FILE, realFile.path)
        }
    }

    abstract class RemoteDatabaseType(
            name: String,
            jdbc: String,
            driverClass: String,
            driverLink: String,
            val hostname: String,
            val port: Short,
            val database: String,
            val username: String,
            val password: String
    ) : DatabaseType(name, jdbc, driverClass, driverLink) {
        override fun dataSource(): HikariDataSource {
            loadDependency()

            return HikariDataSource(config())
        }

        override fun config(): HikariConfig = HikariConfig().apply {
            driverClassName = driverClass
            jdbcUrl = jdbc
            username = this@RemoteDatabaseType.username
            password = this@RemoteDatabaseType.password
        }
    }

    class H2(
            dataFolder: File,
            file: String
    ) : FileDatabaseType(
            "H2",
            "jdbc:h2:file:./$KEY_FILE",
            "org.h2.Driver",
            "https://repo1.maven.org/maven2/com/h2database/h2/1.4.199/h2-1.4.199.jar",
            dataFolder,
            file,
            "h2.db",
            false
    )

    class SQLite(
            dataFolder: File,
            file: String
    ) : FileDatabaseType(
            "SQLite",
            "jdbc:sqlite:./$KEY_FILE",
            "org.sqlite.JDBC",
            "https://bitbucket.org/xerial/sqlite-jdbc/downloads/sqlite-jdbc-3.23.1.jar",
            dataFolder,
            file,
            "sqlite.db",
            true
    ) {
        // https://github.com/brettwooldridge/HikariCP/issues/393#issuecomment-135580191
        override fun config(): HikariConfig {
            return super.config().apply {
                connectionTestQuery = "SELECT 1"
            }
        }
    }

    class MySQL(
            hostname: String,
            port: Short,
            database: String,
            username: String,
            password: String
    ) : RemoteDatabaseType(
            "MySQL",
            "jdbc:mysql://$hostname:$port/$database",
            "com.mysql.jdbc.Driver",
            "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.11/mysql-connector-java-8.0.11.jar",
            hostname, port, database,
            username, password
    )

    class PostgreSQL(
            hostname: String,
            port: Short,
            database: String,
            username: String,
            password: String
    ) : RemoteDatabaseType(
            "PostgreSQL",
            "jdbc:postgresql://$hostname:$port/$database",
            "org.postgresql.Driver",
            "https://jdbc.postgresql.org/download/postgresql-42.2.2.jre7.jar",
            hostname, port, database,
            username, password
    )

    class SQLServer(
            hostname: String,
            port: Short,
            database: String,
            username: String,
            password: String
    ) : RemoteDatabaseType(
            "SQLServer",
            "jdbc:sqlserver://$hostname:$port;databaseName=$database",
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            "https://repo1.maven.org/maven2/com/microsoft/sqlserver/mssql-jdbc/6.4.0.jre8/mssql-jdbc-6.4.0.jre8.jar",
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
                    Class.forName(driverClass)
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
        var n = input.read(buffer)
        while (-1 != n) {
            output.write(buffer, 0, n)

            n = input.read(buffer)
        }

        input.close()
        output.close()
    }
}