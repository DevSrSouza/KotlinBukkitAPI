repositories {
    maven("http://nexus.okkero.com/repository/maven-releases/")
    maven("https://repo.codemc.org/repository/maven-public")
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8")
    api("com.okkero.skedule:skedule:1.2.6")
    implementation("org.bstats:bstats-bukkit:1.7")
}