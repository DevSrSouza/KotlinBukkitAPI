object Dep {
    val spigot = "org.spigotmc:spigot-api:${Versions.spigotApi}"
    val bstats = "org.bstats:bstats-bukkit:${Versions.bstats}"
    val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.corouties}"
    val skedule = "com.okkero.skedule:skedule:${Versions.skedule}"
    val hikariCp = "com.zaxxer:HikariCP:${Versions.hikari}"
    val kaml = "com.charleskorn.kaml:kaml:${Versions.kaml}"
    val serializationCore = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}"


    object Exposed {
        val core = "org.jetbrains.exposed:exposed-core:${Versions.exposed}"
        val jdbc = "org.jetbrains.exposed:exposed-jdbc:${Versions.exposed}"
        val dao = "org.jetbrains.exposed:exposed-dao:${Versions.exposed}"
        val javaTime = "org.jetbrains.exposed:exposed-java-time:${Versions.exposed}"
    }

    object Versions {
        val kotlin = "1.4.10"
        val serialization = "1.0.0-RC"
        val exposed = "0.25.1"
        val hikari = "3.3.1"
        val bstats = "1.7"
        val spigotApi = "1.8.8-R0.1-SNAPSHOT"
        val kaml = "0.19.0"
        val corouties = "1.3.9"
        val skedule = "1.2.6"
        val shadow = "6.0.0"
    }
}