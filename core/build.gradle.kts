repositories {
    maven {
        name = "okkero"
        url = uri("http://nexus.okkero.com/repository/maven-releases/")
    }
}

dependencies {
    compile("br.com.devsrsouza:config4bukkit:1.0.0-SNAPSHOT")
    compile("com.typesafe:config:1.3.2")

    compileOnly("com.okkero.skedule:skedule:1.2.6")
}