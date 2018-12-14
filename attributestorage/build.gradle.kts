repositories {
    maven {
        name = "perfectdreams"
        url = uri("https://repo.perfectdreams.net/")
    }
}

dependencies {
    compileOnly(project(":core"))
    compile("com.comphenix.attribute:AttributeStorage:0.0.2-SNAPSHOT")
}