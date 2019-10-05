repositories {
    maven {
        name = "okkero"
        url = uri("http://nexus.okkero.com/repository/maven-releases/")
    }
}

dependencies {
    compile("br.com.devsrsouza:json4bukkit:1.0.0-SNAPSHOT")

    compileOnly("com.okkero.skedule:skedule:1.2.6")
}