repositories {
    maven {
        name = "okkero"
        url = uri("http://nexus.okkero.com/repository/maven-releases/")
    }
    maven {
        name = "codemc"
        url = uri("https://repo.codemc.org/repository/maven-public")
    }
}

dependencies {
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8")

    compile("com.okkero.skedule:skedule:1.2.6")

    compile("org.bstats:bstats-bukkit:1.7")
}