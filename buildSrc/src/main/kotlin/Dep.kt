object Dep {
    val spigot = "org.spigotmc:spigot-api:${Versions.spigotApi}"
    val bstats = "org.bstats:bstats-bukkit:${Versions.bstats}"
    val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.corouties}"
    val skedule = "com.okkero.skedule:skedule:${Versions.skedule}"
    val hikariCp = "com.zaxxer:HikariCP:${Versions.hikari}"
    val kaml = "com.charleskorn.kaml:kaml:${Versions.kaml}"
    val serializationCore = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}"

    val snakeYml = "org.snakeyaml:snakeyaml-engine:${Versions.snakeYaml}"


    object Exposed {
        val core = "org.jetbrains.exposed:exposed-core:${Versions.exposed}"
        val jdbc = "org.jetbrains.exposed:exposed-jdbc:${Versions.exposed}"
        val dao = "org.jetbrains.exposed:exposed-dao:${Versions.exposed}"
        val javaTime = "org.jetbrains.exposed:exposed-java-time:${Versions.exposed}"
    }

    object Versions {
        val kotlin = "1.5.0"
        val serialization = "1.2.0"
        val exposed = "0.31.1"
        val hikari = "4.0.3"
        val bstats = "2.2.1"
        val spigotApi = "1.8.8-R0.1-SNAPSHOT"
        val kaml = "0.31.0"
        val corouties = "1.5.0-RC"
        val skedule = "1.2.6"
        val shadow = "6.1.0"

        val snakeYaml = "2.3"
    }
}